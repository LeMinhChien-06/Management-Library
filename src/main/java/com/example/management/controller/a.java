package com.example.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class a {
    @Operation(summary = "Danh sách tất cả chiến dịch điển hình")
    @GetMapping
    public String aa() {
        return "b";
    }
}
