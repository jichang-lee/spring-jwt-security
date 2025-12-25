package com.jwt.security.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MasterException extends RuntimeException{

    public final Map<String,String> validation = new HashMap<>();

    public abstract int getStatusCode();

    public MasterException(String message) {
        super(message);
    }

    public void addValidation(String message, String fieldName) {
        validation.put(message, fieldName);
    }
}
