package com.prcp.prcp.dto;

import lombok.Data;

@Data
public class CountryConfigRequestDto {

    private String countryCode;
    private String countryName;
    private String currency;
    private String coreBankingAcct;

    private String createdBy;

}
