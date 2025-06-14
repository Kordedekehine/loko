package com.prcp.prcp.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class FileRecordRequestDto {

    private String fileName;
    private MultipartFile file;
    private String state;

    private String status;

    private String accountType; // POOL, TRANSIT, REVENUE

    private String countryCode;

    private String comment;

    private Long userId;

    private LocalDateTime uploadedAt;
}
