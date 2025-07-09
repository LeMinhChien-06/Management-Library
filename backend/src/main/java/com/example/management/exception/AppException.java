package com.example.management.exception;

import com.example.management.constants.MessageCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final MessageCode messageCode;

    public AppException(MessageCode messageCode) {
        super(messageCode.getMessage());
        this.messageCode = messageCode;
    }

    public String getErrorCode() {
        return messageCode.getCode();
    }

    public int getStatusCode() {
        return messageCode.getStatusCode();
    }

}
