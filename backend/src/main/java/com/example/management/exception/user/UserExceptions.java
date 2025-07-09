package com.example.management.exception.user;

import com.example.management.constants.MessageCode;
import com.example.management.exception.AppException;

public class UserExceptions {

    public static AppException userNotFound() {
        return new AppException(MessageCode.USER_NOT_FOUND);
    }

    public static AppException usernameNotExists() {
        return new AppException(MessageCode.USERNAME_NOT_EXIST);
    }

    // Người dùng đã tồn tại
    public static AppException userAlreadyExists() {
        return new AppException(MessageCode.USER_ALREADY_EXISTS);
    }

    // email đã tồn tại
    public static AppException emailAlreadyExists() {
        return new AppException(MessageCode.EMAIL_ALREADY_EXISTS);
    }


    public static AppException phoneAlreadyExists() {
        return new AppException(MessageCode.PHONE_ALREADY_EXISTS);
    }

    // Tên đã tồn tại
    public static AppException usernameAlreadyExists() {
        return new AppException(MessageCode.USERNAME_ALREADY_EXISTS);
    }

    public static AppException invalidCredentials() {
        return new AppException(MessageCode.INVALID_CREDENTIALS);
    }

    public static AppException accountDisabled() {
        return new AppException(MessageCode.ACCOUNT_DISABLED);
    }

    public static AppException accountLocked() {
        return new AppException(MessageCode.ACCOUNT_LOCKED);
    }

    public static  AppException passwordInvalid() {
        return new AppException(MessageCode.PASSWORD_INVALID);
    }

    public static AppException permissionDenied() {
        return new AppException(MessageCode.PERMISSION_DENIED);
    }

}
