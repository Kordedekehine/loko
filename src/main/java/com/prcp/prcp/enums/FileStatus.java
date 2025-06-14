package com.prcp.prcp.enums;

public enum FileStatus {

    PENDING("P"),
    APPROVED("Y"),
    REJECTED("F"),
    PUSHED("PUSHED");

    private final String code;

    FileStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

