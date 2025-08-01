package com.baerchen.central.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAspectJAutoProxy
@EnableJpaRepositories
@SpringBootApplication
public class CentralAuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralAuthenticationServiceApplication.class, args);
	}

}
