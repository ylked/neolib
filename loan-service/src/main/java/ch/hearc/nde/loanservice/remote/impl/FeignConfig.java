package ch.hearc.nde.loanservice.remote.impl;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    ErrorDecoder errorDecoder() {
        return new FeignExceptionDecoder();
    }
}