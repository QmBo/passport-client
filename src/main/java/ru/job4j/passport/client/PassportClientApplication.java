package ru.job4j.passport.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * PassportClientApplication class.
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 26.11.2021
 */
@SpringBootApplication
public class PassportClientApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PassportClientApplication.class, args);
	}

	/**
	 * Gets client.
	 *
	 * @param builder the builder
	 * @return the client
	 */
	@Bean
	public RestTemplate getClient(RestTemplateBuilder builder) {
		return builder.build();
	}
}
