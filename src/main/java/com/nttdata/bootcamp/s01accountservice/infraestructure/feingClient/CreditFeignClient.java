package com.nttdata.bootcamp.s01accountservice.infraestructure.feingClient;

import com.nttdata.bootcamp.s01accountservice.model.CreditCardDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "credit-service", url = "${ntt.data.bootcamp.s01-credit-service}")
public interface CreditFeignClient {

    @GetMapping("/credit-cards/by-client/{clientId}")
    List<CreditCardDetails> getCreditCardsByClientId(@PathVariable("clientId") String clientId);
}