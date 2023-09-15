package com.nttdata.bootcamp.s01accountservice.api.apiDelegateimpl;
import com.nttdata.bootcamp.s01accountservice.model.TransactionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.nttdata.bootcamp.s01accountservice.api.AccountsApiDelegate;
import com.nttdata.bootcamp.s01accountservice.model.AccountCreateInput;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;
import com.nttdata.bootcamp.s01accountservice.application.AccountService;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccoutApiDelegateImpl implements AccountsApiDelegate {
	/**
	 * Para acceder a accountService.
	 */
	@Autowired
	private AccountService accountService;

	/**
	 * Método para guardar una transacción.
	 * @param accountId parametro de AccountDetails.
	 * @return accountsAccountIdGet del accountId.
	 */
    @Override
    public Mono<ResponseEntity<AccountDetails>> accountsAccountIdGet(final String accountId, ServerWebExchange exchange) {
        return accountService.accountsAccountIdGet(accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
	/**
	 * Método para guardar una transacción.
	 * @param requestBody parametro de AccountDetails.
	 * @return accountsByListPost del requestBody.
	 */
    @Override
    public Mono<ResponseEntity<Flux<AccountDetails>>> accountsByListPost(final Flux<String> requestBody, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(accountService.accountsByListPost(requestBody)));
    }


    /**
	 * Método para guardar una transacción.
	 * @param clientId parametro de AccountDetails.
	 * @return accountsClientIdGet del clientId.
	 */
	@Override
	public Mono<ResponseEntity<AccountDetails>> accountsClientIdGet(String clientId, ServerWebExchange exchange) {
		return Mono.fromSupplier(() -> AccountsApiDelegate.super.accountsClientIdGet(clientId, exchange).block());
	}

	/**
	 * Método para guardar una transacción.
	 * @param accountCreateInput parametro de AccountDetails.
	 * @return accountsPost del accountCreateInput.
	 */
    @Override
    public Mono<ResponseEntity<AccountDetails>> accountsPost(final Mono<AccountCreateInput> accountCreateInput, ServerWebExchange exchange) {
        return accountService.accountsPost(accountCreateInput)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }


    /**
	 * Método para guardar una transacción.
	 * @param accountId parametro para buscar.
	 * @param requestBody lista del body.
	 * @return accountsAccountIdAddSignersPost de los accountId, requestBody
	 */
    @Override
    public Mono<ResponseEntity<AccountDetails>> accountsAccountIdAddSignersPost(final String accountId, final Flux<String> requestBody, ServerWebExchange exchange) {
        return accountService.accountsAccountIdAddSignersPost(accountId, requestBody)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

	/**
	 * Método para guardar una transacción.
	 * @param transactionInput parametro de AccountDetails.
	 * @return accountsDepositPost del transactionInput.
	 */
	@Override
	public Mono<ResponseEntity<AccountDetails>> accountsDepositPost(Mono<TransactionInput> transactionInput, ServerWebExchange exchange) {
		return accountService.accountsDepositPost(transactionInput)
				.map(ResponseEntity::ok)
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}

	/**
	 * Método para guardar una transacción.
	 * @param transactionInput parametro de AccountDetails.
	 * @return accountsWithdrawPost del transactionInput.
	 */
	@Override
	public Mono<ResponseEntity<AccountDetails>> accountsWithdrawPost(Mono<TransactionInput> transactionInput, ServerWebExchange exchange) {
		return accountService.accountsWithdrawPost(transactionInput)
				.map(ResponseEntity::ok)
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}
}


