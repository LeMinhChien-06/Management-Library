package com.example.management.utils;

import com.example.management.constants.MessageCode;

public class ValidationErrorMapper {

    public static String mapFieldErrors(String fieldName, String messageCode) {
        try{
            MessageCode msgCode = MessageCode.fromCode(messageCode);
            return String.format("%s: %s", fieldName, msgCode.getMessage());
        }catch (IllegalArgumentException e){
            return String.format("%s: %s", fieldName, messageCode);
        }
    }

    public static String mapPropertyPath(String propertyPath, String messageCode) {
        try{
            MessageCode msgCode = MessageCode.fromCode(messageCode);
            return String.format("%s: %s", propertyPath, msgCode.getMessage());
        }catch (IllegalArgumentException e){
            return String.format("%s: %s", propertyPath, messageCode);
        }
    }

    public static MessageCode getMessageCodeOrDefault(String code){
        try {
            return MessageCode.fromCode(code);
        }catch (IllegalArgumentException e){
            return MessageCode.VALIDATION_ERROR;
        }
    }
}
