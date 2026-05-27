package com.generation153.harmonyfree.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	
	@Bean
    public WebClient webClient(JamendoProperties props) {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(5 * 1024 * 1024)) // 5 MB
                .build();

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .exchangeStrategies(strategies)
                .build();
    }

}
