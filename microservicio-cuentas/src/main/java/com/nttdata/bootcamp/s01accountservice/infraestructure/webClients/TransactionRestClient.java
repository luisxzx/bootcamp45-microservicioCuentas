package com.nttdata.bootcamp.s01accountservice.infraestructure.webClients;

import com.nttdata.bootcamp.s01accountservice.model.CommissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TransactionRestClient {

    @Value("${ntt.data.bootcamp.s01-transaction-service}")
    private String transactionServiceUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Void> registerCommission(CommissionDTO commissionDTO) {
        WebClient webClient = webClientBuilder.baseUrl(transactionServiceUrl).build();
        return webClient.post()
                .uri("/transactions/commissions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commissionDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
