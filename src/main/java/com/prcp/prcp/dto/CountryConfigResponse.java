package com.prcp.prcp.dto;

import lombok.Data;

@Data
public class CountryConfigResponse {

    private Long id;
    private String countryCode;
    private String countryName;
    private String currency;
    private String coreBankingAcct;

    private String createdBy;
    private String createdAt;

}
