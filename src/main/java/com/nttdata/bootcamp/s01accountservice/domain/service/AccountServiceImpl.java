package com.nttdata.bootcamp.s01accountservice.domain.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.nttdata.bootcamp.s01accountservice.common.AccountNotFoundException;
import com.nttdata.bootcamp.s01accountservice.infraestructure.feingClient.CreditFeignClient;
import com.nttdata.bootcamp.s01accountservice.infraestructure.feingClient.TransactionFeignClient;
import com.nttdata.bootcamp.s01accountservice.model.*;
import com.nttdata.bootcamp.s01accountservice.infraestructure.webClients.ClientRestClient;
import com.nttdata.bootcamp.s01accountservice.infraestructure.webClients.CreditRestClient;
import com.nttdata.bootcamp.s01accountservice.infraestructure.webClients.TransactionRestClient;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nttdata.bootcamp.s01accountservice.domain.document.AccountDocument;
import com.nttdata.bootcamp.s01accountservice.common.AccountCreationException;
import com.nttdata.bootcamp.s01accountservice.infraestructure.feingClient.ClienteFeignClient;
import com.nttdata.bootcamp.s01accountservice.common.AccountMapper;
import com.nttdata.bootcamp.s01accountservice.domain.repository.AccountMongoRepository;
import com.nttdata.bootcamp.s01accountservice.application.AccountService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
/**
 * injeccion clienteFeignClient.
 */
	@Autowired
	private ClienteFeignClient clienteFeignClient;

	/**
	 * injeccion CreditFeignClient.
	 */
	@Autowired
	private CreditFeignClient creditFeignClient;


	@Autowired
	private ClientRestClient clienteWebClient;

	@Autowired
	private CreditRestClient creditWebClient;

	@Autowired
	private TransactionRestClient transactionWebClient;


	/**
	 * injeccion TransactionFeignClient.
	 */
	@Autowired
	private TransactionFeignClient transactionFeignClient;

	/**
	 * injeccion AccountMongoRepository.
	 */
	@Autowired
	private AccountMongoRepository accountMongoRepository;

	/**
	 * AccountService.
	 * @param accountId busqueda por id.
	 * @return un AccountDetails.
	 */
	public Mono<AccountDetails> accountsAccountIdGet(final String accountId) {
		return accountMongoRepository.findById(new ObjectId(accountId))
				.map(AccountMapper::mapDocumentToDto)
				.switchIfEmpty(Mono.error(new AccountNotFoundException("Account not found.")));
	}

	/**
	 * AccountService.
	 * @param requestBody el cuerpo.
	 * @return una lista AccountDetails.
	 */
	@Override
	public Flux<AccountDetails> accountsByListPost(final Flux<String> requestBody) {
		return requestBody
				.map(ObjectId::new)
				.collectList()
				.flatMapMany(ids -> accountMongoRepository.findByOwnerClientsContains(ids));
	}


	/**
	 * AccountService.
	 * @param accountCreateInputMono type.
	 * @return un AccountDetails.
	 */
	@Override
	public Mono<AccountDetails> accountsPost(Mono<AccountCreateInput> accountCreateInputMono) {
		return accountCreateInputMono
				.flatMap(accountCreateInput -> clienteWebClient.bulkRetrieveClients(accountCreateInput.getOwnerClients())
		  .collectList()
		  .flatMap(dtoList -> {

			  // Vamos a buscar las tarjetas de crédito para todos los clientes
			  Flux<CreditCardDetails> allCreditCardDetailsFlux = Flux.fromIterable(dtoList)
					  .flatMap(clientDTO -> creditWebClient.getCreditCardsByClientId(clientDTO.getId()));

			  return allCreditCardDetailsFlux.collectList().flatMap(allCreditCardDetails -> {

				  Predicate<AccountCreateInput> isAhorroAccount = input -> input.getType().equals(AccountCreateInput.TypeEnum.AHORRO);
				  Predicate<ClientDTO> isPersonalClient = client -> client.getTipoClienteId() != null && "64fe8a8e319b680fa66823b7".equalsIgnoreCase(client.getTipoClienteId());
				  Predicate<ClientDTO> isEmpresarialClient = client -> client.getTipoClienteId() != null && "64fe8ac6319b680fa66823bb".equalsIgnoreCase(client.getTipoClienteId());

				  List<ClientDTO> personalClients = dtoList.stream().filter(isPersonalClient).collect(Collectors.toList());
				  List<ClientDTO> empresarialClients = dtoList.stream().filter(isEmpresarialClient).collect(Collectors.toList());

				  AccountDocument toBeSavedAccount = AccountMapper.mapCreateInputToDocument(accountCreateInput);

				  // Lógica para cliente personal
				  if (!personalClients.isEmpty()) {

					  return Flux.fromIterable(personalClients)
							  .flatMap(personalClient -> accountMongoRepository.findByOwnerClientsContainsAndType(new ObjectId(personalClient.getId()), accountCreateInput.getType()))
							  .collectList()
							  .flatMap(accountsOfType -> {

								  if (!accountsOfType.isEmpty()) {
									  return Mono.error(new AccountCreationException("El cliente personal ya tiene una cuenta de este tipo."));
								  }

								  if (isAhorroAccount.test(accountCreateInput)) {
									  if (allCreditCardDetails.isEmpty()) {
										  return Mono.error(new AccountCreationException("No tienes tarjetas de crédito asociadas a este cliente."));
									  }
									  toBeSavedAccount.setBalance(new BigDecimal(500));
								  }

								  return accountMongoRepository.save(toBeSavedAccount).map(AccountMapper::mapDocumentToDto);
							  });
				  }

				  // Lógica para cliente empresarial
				  if (!empresarialClients.isEmpty()) {
					  if (allCreditCardDetails.isEmpty()) {
						  return Mono.error(new AccountCreationException("No hay tarjetas de crédito asociadas al cliente empresarial."));
					  }

					  if (isAhorroAccount.test(accountCreateInput) || accountCreateInput.getType().equals(AccountCreateInput.TypeEnum.PLAZOFIJO)) {
						  return Mono.error(new AccountCreationException("Un cliente empresarial no puede tener una cuenta de ahorro o de plazo fijo."));
					  }

					  if (accountCreateInput.getSignClients().size() >= 4) {
						  return Mono.error(new AccountCreationException("Una cuenta empresarial no puede tener más de 4 firmantes autorizados."));
					  }

					  return accountMongoRepository.findByOwnerClientsContainsAndType(new ObjectId(empresarialClients.get(0).getId()), AccountCreateInput.TypeEnum.CORRIENTE)
							  .collectList()
							  .flatMap(existingCorrienteAccounts -> {
								  if (!existingCorrienteAccounts.isEmpty()) {
									  return Mono.error(new AccountCreationException("Un cliente empresarial ya tiene una cuenta corriente."));
								  }
								  return accountMongoRepository.save(toBeSavedAccount).map(AccountMapper::mapDocumentToDto);
							  });
				  }

				  return Mono.error(new AccountCreationException("No se pudo crear la cuenta."));
			  });
		  }));
	}


	/**
	 * AccountService.
	 * @param requestBody lista del cuerpo.
	 * @param accountId variable.
	 * @return un AccountDetails.
	 */
	@Override
	public Mono<AccountDetails> accountsAccountIdAddSignersPost(final String accountId, Flux<String> requestBody) {
		Predicate<Set<String>> moreThan4Elements = (set) -> set.size() >= 4;

		return accountMongoRepository.findById(new ObjectId(accountId))
				.switchIfEmpty(Mono.error(new AccountCreationException("La cuenta solicitada no existe")))
				.flatMap(accountDocument -> {
					// Obtener el conjunto de firmantes únicos combinando los existentes y los nuevos
					return requestBody.collectList()
							.map(newSigners -> {
								Set<String> uniqueIds = new HashSet<>(accountDocument.getSignClients());
								uniqueIds.addAll(newSigners);
								return uniqueIds;
							})
							.flatMap(uniqueIds -> {
								if (moreThan4Elements.test(uniqueIds)) {
									return Mono.error(new AccountCreationException("Una cuenta empresarial no puede tener más de 4 firmantes autorizados."));
								} else {
									accountDocument.setSignClients(new ArrayList<>(uniqueIds));
									return accountMongoRepository.save(accountDocument);
								}
							})
							.map(AccountMapper::mapDocumentToDto);
				});
	}


	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	@Override
	public Mono<AccountDetails> accountsDepositPost(final Mono<TransactionInput> transactionInput) {
		return transaction(transactionInput, true);
	}

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	@Override
	public Mono<AccountDetails> accountsWithdrawPost(final Mono<TransactionInput> transactionInput) {
		return transaction(transactionInput, false);
	}

	/**
	 * AccountService.
	 * @param transactionInputMono variable.
	 * @param isDeposit variable.
	 * @return AccountMapper.mapDocumentToDto(account).
	 */
	private Mono<AccountDetails> transaction(final Mono<TransactionInput> transactionInputMono, final boolean isDeposit) {
		return transactionInputMono.flatMap(transactionInput -> accountMongoRepository.findById(transactionInput.getAccountId())
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found.")))
				.flatMap(account -> {
					if (account.getLastTransactionDate() != null
							&& !account.getLastTransactionDate().getMonth().equals(LocalDate.now().getMonth())) {
						account.setTransactionCount(0);
					}

					BigDecimal amount = transactionInput.getAmount();
					Predicate<AccountDocument> isBeyondFreeTransactionLimit = acc -> acc.getTransactionCount() > 20;

					// Usando AtomicReference para commissionAmount
					AtomicReference<BigDecimal> commissionAmountRef = new AtomicReference<>(BigDecimal.ZERO);

					if (isDeposit) {
						if (isBeyondFreeTransactionLimit.test(account)) {
							commissionAmountRef.set(amount.multiply(new BigDecimal("0.02")));
							amount = amount.subtract(commissionAmountRef.get());
						}
						account.setBalance(account.getBalance().add(amount));
					} else {
						if (isBeyondFreeTransactionLimit.test(account)) {
							commissionAmountRef.set(amount.multiply(new BigDecimal("0.02")));
							amount = amount.add(commissionAmountRef.get());
						}
						if (account.getBalance().compareTo(amount) < 0) {
							return Mono.error(new IllegalArgumentException("Insufficient funds."));
						}
						account.setBalance(account.getBalance().subtract(amount));
					}

					account.setTransactionCount(account.getTransactionCount() + 1);
					return accountMongoRepository.save(account)
							.flatMap(savedAccount -> {
								if (commissionAmountRef.get().compareTo(BigDecimal.ZERO) > 0) {
									CommissionDTO commissionDTO = new CommissionDTO();
									commissionDTO.setAccountId(account.getId());
									commissionDTO.setTransactionType(isDeposit ? CommissionDTO.TransactionTypeEnum.DEPOSIT : CommissionDTO.TransactionTypeEnum.WITHDRAW);
									commissionDTO.setTransactionDate(OffsetDateTime.now());
									commissionDTO.setComision(commissionAmountRef.get());

									return transactionWebClient.registerCommission(commissionDTO)
											.onErrorResume(e -> {
												System.err.println("Error al registrar la comisión con el microservicio: " + e.getMessage());
												return Mono.empty();
											})
											.thenReturn(savedAccount);
								}
								return Mono.just(savedAccount);
							})
							.map(AccountMapper::mapDocumentToDto);
				}));
	}

	@Override
	public Flux<AccountDetails> findByFirstOwnerClient(String clientId) {
		return accountMongoRepository.findByFirstOwnerClient(clientId)
				.map(AccountMapper::mapDocumentToDto);
	}

	@Override
	public Mono<Void> updateAccountBalance(String accountId, BigDecimal newBalance) {
		if (!ObjectId.isValid(accountId)) {
			return Mono.error(new IllegalArgumentException("ID de cuenta inválido: " + accountId));
		}
		return accountMongoRepository.findById(new ObjectId(accountId))
				.flatMap(account -> {
					account.setBalance(newBalance);
					return accountMongoRepository.save(account);
				})
				.then();
	}








}
