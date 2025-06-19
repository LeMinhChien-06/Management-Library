package com.example.management.dto.request.borrowing;

import lombok.Data;

import java.util.List;
@Data
public class ApproveRequest {
    private List<Long> bookIds;
}
