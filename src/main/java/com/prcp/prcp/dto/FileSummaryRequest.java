package com.prcp.prcp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FileSummaryRequest {

    private int page = 0;
    private int size = 10;
    private String status;
    private String accountType; // filter with (POOL, TRANSIT, REVENUE)
    private String countryCode;
    private LocalDate fromDate;
    private LocalDate toDate;
}
