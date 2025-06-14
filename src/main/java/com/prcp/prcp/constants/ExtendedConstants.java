package com.prcp.prcp.constants;

import lombok.Data;

@Data
public class ExtendedConstants {

    public enum ResponseStatus {
        API_SUCCESS_STATUS("success", "Request processed successfully"),
        API_FAIL_STATUS("fail", "Failed processing request"),
        API_ERROR_STATUS("error", "Error processing request");
        private String code;
        private String description;

        ResponseStatus(String code, String description) {
            this.code = code;
            this.description = description;

        }

    }


    public enum ResponseCode {
        SUCCESS("00", "SUCCESS"),
        FAILED("99", "FAILED"),
        UNKNOWN("01", "Status unknown, please wait for settlement report"),
        INVALID_TRANSACTION("12", "Invalid transaction"),;


        private String code;
        private String description;

        ResponseCode(String code, String description) {
            this.code = code;
            this.description = description;
        }
  }

}

