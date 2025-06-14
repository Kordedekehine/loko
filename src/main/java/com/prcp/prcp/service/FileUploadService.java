package com.prcp.prcp.service;

import com.prcp.prcp.dto.FileRecordRequestDto;
import com.prcp.prcp.dto.FileSummaryRequest;
import com.prcp.prcp.dto.FileSummaryResponse;
import com.prcp.prcp.entity.FileRecord;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface FileUploadService {

     FileSummaryResponse uploadFile(FileRecordRequestDto fileRecordRequestDto);

   //  String approveFile(Long fileId);

    // Page<FileRecord> getSummary(String country, String accountType, LocalDate startDate, LocalDate endDate, int page, int size);

     Page<FileRecord> getPaginatedFiles(FileSummaryRequest request);

     FileSummaryResponse approve(Long fileId, String comment);

     FileSummaryResponse reject(Long fileId, String comment);

     FileSummaryResponse reinitiate(Long id);

     FileSummaryResponse getById(Long id);

     FileSummaryResponse pushToCore(Long id);
}
