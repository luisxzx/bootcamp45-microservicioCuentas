package com.nttdata.bootcamp.s01accountservice.infraestructure.webClients;

import com.nttdata.bootcamp.s01accountservice.model.ClientDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;


import java.util.List;

@Component
public class ClientRestClient {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    private static final Logger log = LoggerFactory.getLogger(ClientRestClient.class);
    @Value("${ntt.data.bootcamp.s01-client-service}")
    private String clientServiceUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<ClientDTO> getClientById(String clientId) {
        WebClient webClient = webClientBuilder.baseUrl(clientServiceUrl).build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/clients/{clientId}")
                        .build(clientId))
                .retrieve()
                .bodyToMono(ClientDTO.class);
    }

    public Flux<ClientDTO> bulkRetrieveClients(List<String> clientIds) {
        WebClient webClient = webClientBuilder.baseUrl(clientServiceUrl).build();

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("myCircuit");

        return webClient.post()
                .uri("/clients/bulk-retrieve")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(clientIds))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class).flatMap(errorMessage -> {
                    log.error("Error al recuperar clientes: {}", errorMessage);
                    return Mono.error(new RuntimeException("Error al recuperar clientes: " + errorMessage));
                }))
                .bodyToFlux(ClientDTO.class)
                .doOnError(e -> log.error("Error en bulkRetrieveClients: ", e))
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
    }


}
