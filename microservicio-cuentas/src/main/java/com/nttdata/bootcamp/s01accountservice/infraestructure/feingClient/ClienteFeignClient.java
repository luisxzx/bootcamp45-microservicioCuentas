package com.nttdata.bootcamp.s01accountservice.infraestructure.feingClient;



import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nttdata.bootcamp.s01accountservice.model.ClientDTO;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "client-service", url = "${ntt.data.bootcamp.s01-client-service}")
public interface ClienteFeignClient {

	@GetMapping("/clients/{clientId}")
	ClientDTO getClientById(@PathVariable("clientId") String clientId);

	@PostMapping("/clients/bulk-retrieve")
	List<ClientDTO> bulkRetrieveClients(@RequestBody List<String> clientIds);
}

