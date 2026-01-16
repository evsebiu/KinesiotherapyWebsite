package com.example.KinetoWebsite;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@OpenAPIDefinition(
        info = @Info(
                title = "Kineto Landing Page API",
                version = "1.0",
                description = "API for a Kineto massage booking website."
        )
)

@SpringBootApplication

public class KinetoWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(KinetoWebsiteApplication.class, args);
	}


}
