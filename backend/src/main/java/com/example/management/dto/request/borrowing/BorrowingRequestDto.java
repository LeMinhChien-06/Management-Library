package com.example.management.dto.request.borrowing;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BorrowingRequestDto {
    private Long userId;
    private List<Long> bookIds;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "ERR_REQUIRED_DUE_DATE")
    private LocalDate dueDate;
}