package com.example.management.exception.book;

import com.example.management.constants.MessageCode;
import com.example.management.exception.AppException;

public class BookExceptions {

    public static AppException bookNotFound() {
        return new AppException(MessageCode.BOOK_NOT_FOUND);
    }
}
