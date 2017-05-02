package com.liuwp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * http://www.baeldung.com/securing-a-restful-web-service-with-spring-security
 * https://malalanayake.wordpress.com/2014/06/30/stateless-spring-security-on-rest-api/
 */
@SpringBootApplication
public class SpringSecurityRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityRestApplication.class, args);
	}
}
