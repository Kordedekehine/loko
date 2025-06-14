package com.prcp.prcp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponseDto {

    private Long id;

    private String productName;
    private String binRange;

    private String countryCode;

    private LocalDateTime createdAt;
    private String createdBy;
}
