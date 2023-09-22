package com.wy.music.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author:wy
 * @Date: 2023/9/1  16:52
 * @Version 1.0
 */
@Configuration
public class RestTemplateConfig {

    private Integer READ_TIME_OUT = 5000;
    private Integer CONNECT_TIME_OUT = 15000;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(READ_TIME_OUT);
        factory.setConnectTimeout(CONNECT_TIME_OUT);
        return factory;
    }
}
