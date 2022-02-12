package com.cargo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.cargo.models.FileStorageProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class CargoApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(CargoApplication.class, args);
	}

}
