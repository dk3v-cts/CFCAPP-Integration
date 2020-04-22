package com.cognizant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class WebSocketChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSocketChatApplication.class, args);
	}
}
