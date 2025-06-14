package com.prcp.prcp.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagedFileResponseDto {

    private List<FileSummaryResponse> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
