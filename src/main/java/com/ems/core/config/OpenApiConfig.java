package com.ems.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	 OpenAPI customeOpenApiConfig() {	
		return new OpenAPI().
				info(new Info()
						.title("EMS API")
						.description("API for managing employees in the system")
						.version("1.0.0"));
		
	}
	
}
