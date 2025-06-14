package com.prcp.prcp.dto;

import lombok.Data;

@Data
public class ProductRequestDto {

    private String productName;
    private String binRange;

    private String countryCode;

    private String createdBy;

}
