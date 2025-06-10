package com.example.management.exception.categories;

import com.example.management.constants.MessageCode;
import com.example.management.exception.AppException;

public class CategoryExceptions {

    public static AppException categoryNotFound() {
        return new AppException(MessageCode.CATEGORY_NOT_FOUND);
    }
}
