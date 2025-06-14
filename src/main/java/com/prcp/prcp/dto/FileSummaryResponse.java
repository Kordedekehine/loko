package com.prcp.prcp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileSummaryResponse {

    private Long id;
    private String fileName;
    private String accountType;
    private String countryCode;
    private String comment;
    private long size;
    private String state;
    private String status;
    private String approvalStage; // POOL, TRANSIT, REVENUE
    private String currentStage;
    private LocalDateTime uploadedAt;

}
