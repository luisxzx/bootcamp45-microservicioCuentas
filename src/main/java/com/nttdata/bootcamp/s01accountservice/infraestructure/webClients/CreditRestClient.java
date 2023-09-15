package com.nttdata.bootcamp.s01accountservice.infraestructure.webClients;

import com.nttdata.bootcamp.s01accountservice.model.CreditCardDetails;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class CreditRestClient {

    private static final Logger log = LoggerFactory.getLogger(CreditRestClient.class);
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    @Value("${ntt.data.bootcamp.s01-credit-service}")
    private String creditServiceUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Flux<CreditCardDetails> getCreditCardsByClientId(String clientId) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitbrakerCreditCards");

        WebClient webClient = webClientBuilder.baseUrl(creditServiceUrl).build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/credit-cards/by-client/{clientId}")
                        .build(clientId))
                .retrieve()
                .bodyToFlux(CreditCardDetails.class)
                .doOnError(e -> {
                    // Aquí puedes manejar el error como lo requieras, por ejemplo, loguearlo.
                    log.error("Error al obtener tarjetas de crédito para el cliente: {}", clientId, e);
                })
                .transform(CircuitBreakerOperator.of(circuitBreaker));  // Aplicar el circuit breaker
    }


}