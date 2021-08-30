package com.ibm.mediaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({"com.ibm.authorization","com.ibm.mediaservice"})
@EnableJpaRepositories("com.ibm.authorization.repository")
@EntityScan("com.ibm.authorization.model")
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MediaserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaserviceApplication.class, args);
	}

}
