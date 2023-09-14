package com.nttdata.bootcamp.s01accountservice.infraestructure.feingClient;


import com.nttdata.bootcamp.s01accountservice.model.CommissionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "transaction-service", url = "${ntt.data.bootcamp.s01-transaction-service}")
public interface TransactionFeignClient {

    @PostMapping("/transactions/commissions")
    void registerCommission(CommissionDTO comisionDTO);




}