package com.nttdata.bootcamp.s01accountservice.application;
import java.math.BigDecimal;
import java.util.List;
import com.nttdata.bootcamp.s01accountservice.model.AccountCreateInput;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;
import com.nttdata.bootcamp.s01accountservice.model.TransactionInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
/**
 * AccountService.
 * @param accountId busqueda por id.
 * @return un AccountDetails.
 */
    Mono<AccountDetails> accountsAccountIdGet(String accountId);

	/**
	 * AccountService.
	 * @param requestBody el cuerpo.
	 * @return una lista AccountDetails.
	 */
	Flux<AccountDetails> accountsByListPost(Flux<String> requestBody);

	/**
	 * AccountService.
	 * @param accountCreateInput type.
	 * @return un AccountDetails.
	 */
	Mono<AccountDetails> accountsPost(Mono<AccountCreateInput> accountCreateInput);

	/**
	 * AccountService.
	 * @param requestBody lista del cuerpo.
	 * @param accountId variable.
	 * @return un AccountDetails.
	 */
	Mono<AccountDetails> accountsAccountIdAddSignersPost(String accountId, Flux<String> requestBody);

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	Mono<AccountDetails> accountsDepositPost(Mono<TransactionInput> transactionInput);

	/**
	 * AccountService.
	 * @param transactionInput variable.
	 * @return un AccountDetails.
	 */
	Mono<AccountDetails> accountsWithdrawPost(Mono<TransactionInput> transactionInput);

	Flux<AccountDetails> findByFirstOwnerClient(String clientId);

	Mono<Void> updateAccountBalance(String accountId, BigDecimal newBalance);



}
