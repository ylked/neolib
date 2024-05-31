package ch.hearc.nde.loanservice.remote.impl;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;

public class FeignExceptionDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new BadRequestException();
            case 404 -> new Error400Exception("Not found");
            default -> new Exception("Exception while calling service");
        };
    }
}