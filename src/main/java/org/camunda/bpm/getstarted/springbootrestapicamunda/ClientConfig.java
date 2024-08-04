package org.camunda.bpm.getstarted.springbootrestapicamunda;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    // rest template bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // make a task client to make connection with camunda external engine
    @Bean
    public ExternalTaskClient externalTaskClient() {
        return ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(20000)
                .build();
    }
}
