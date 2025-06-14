package com.prcp.prcp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class BaseResponse <T>{

    private String status;

    private String message;

    @JsonProperty(value = "data")
    private T data;

    private Map<String, Object> _meta;
    private Map<String, Object> _links;


}
