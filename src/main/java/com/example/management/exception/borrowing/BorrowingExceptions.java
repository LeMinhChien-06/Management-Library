package com.example.management.exception.borrowing;

import com.example.management.constants.MessageCode;
import com.example.management.exception.AppException;

public class BorrowingExceptions {

    public static AppException borrowBooks() {
        return new AppException(MessageCode.BORROW_BOOK_MAX);
    }

    public static AppException bookOutOfStock(Long bookId) {
        return new AppException(MessageCode.BOOk_OUT_OF_STOCK);
    }
}
