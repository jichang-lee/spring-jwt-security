package com.jwt.security.exception;

import java.util.Map;

public class EmailAlreadyExistsException extends MasterException{

    public static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public EmailAlreadyExistsException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 409;
    }

}
