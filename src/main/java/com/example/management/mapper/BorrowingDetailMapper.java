package com.example.management.mapper;

import com.example.management.dto.response.books.BookInfo;
import com.example.management.dto.response.borrowing.BorrowingDetailResponseDto;
import com.example.management.entity.Borrowing;
import com.example.management.entity.BorrowingDetail;
import com.example.management.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowingDetailMapper {

    private final BookMapper bookMapper;

    public BorrowingDetailResponseDto borrowBooks(User user, Borrowing borrowing) {
        List<BookInfo> bookInfos = borrowing.getBorrowingDetails().stream()
                .map(borrowingDetail -> bookMapper.toBookInfo(borrowingDetail.getBook()))
                .toList();

        return BorrowingDetailResponseDto.builder()
                .borrowingId(borrowing.getId())
                .userId(user.getId())
                .userFullName(user.getFullName())
                .dueDate(borrowing.getDueDate())
                .books(bookInfos)
                .build();
    }


}
