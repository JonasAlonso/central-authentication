package com.baerchen.central.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class CentralAuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralAuthenticationServiceApplication.class, args);
	}

}
