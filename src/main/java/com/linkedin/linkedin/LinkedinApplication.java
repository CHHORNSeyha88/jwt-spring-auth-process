package com.linkedin.linkedin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer"
)
@OpenAPIDefinition(info = @Info(title = "Linkedin API",description = "API documentation for the Linkedin application",version = "1.0"))
public class LinkedinApplication {
	public static void main(String[] args) {
		SpringApplication.run(LinkedinApplication.class, args);
	}
}
