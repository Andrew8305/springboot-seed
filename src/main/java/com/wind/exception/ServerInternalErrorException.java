package com.wind.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerInternalErrorException extends RuntimeException {
    public ServerInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
