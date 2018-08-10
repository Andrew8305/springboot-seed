package com.wind.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ServerInternalErrorException extends RuntimeException {
    public ServerInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
