package com.nttdata.bootcamp.s01accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class S01AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(S01AccountServiceApplication.class, args);
	}

}
