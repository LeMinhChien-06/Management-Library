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

    public static AppException exceedsMaxBorrowLimit() {
        return new AppException(MessageCode.MAX_BORROW_LIMIT);
    }

    public static AppException booksAlreadyBorrowed() {
        return new AppException(MessageCode.ALREADY_BORROW);
    }

    public static AppException invalidDueDate() {
        return new AppException(MessageCode.INVALID_DUE_DATE);
    }

    public static AppException invalidStatus() {
        return new AppException(MessageCode.INVALID_STATUS);
    }

    public static AppException borrowingNotFound() {
        return new AppException(MessageCode.BORROW_NOT_FOUND);
    }

    public static AppException noBooksToReturn() {
        return new AppException(MessageCode.BORROW_NOT_FOUND);
    }
}
