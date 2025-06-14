package com.prcp.prcp.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFileRequestDto {

    private MultipartFile file;
    private String state;
}
