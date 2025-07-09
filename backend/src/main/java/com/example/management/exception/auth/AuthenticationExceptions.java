package com.example.management.exception.auth;

import com.example.management.constants.MessageCode;
import com.example.management.exception.AppException;

public class AuthenticationExceptions {

    public static AppException tokenNotCreated(){
        return new AppException(MessageCode.TOKEN_NOT_CREATED);
    }

    public static AppException tokenExpired() {
        return new AppException(MessageCode.TOKEN_EXPIRED);
    }

    public static AppException tokenInvalid() {
        return new AppException(MessageCode.TOKEN_INVALID);
    }

    public static AppException unauthorized() {
        return new AppException(MessageCode.UNAUTHORIZED);
    }

}
