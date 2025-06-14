package com.prcp.prcp.controller;

import com.prcp.prcp.dto.FileRecordRequestDto;
import com.prcp.prcp.dto.FileSummaryRequest;
import com.prcp.prcp.dto.FileSummaryResponse;
import com.prcp.prcp.dto.PagedFileResponseDto;
import com.prcp.prcp.entity.FileRecord;
import com.prcp.prcp.service.FileUploadService;
import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/prcp")
@PreAuthorize("hasAnyRole('STATE_REP', 'INTERNAL_CONTROL', 'HEAD', 'COO')")
public class FileController {

    private final FileUploadService fileUploadService;


    public FileController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

//    @PostMapping("/upload")
//    public ResponseEntity<FileSummaryResponse> uploadFile(@ModelAttribute FileRecordRequestDto fileRecordRequestDto) {
//        FileSummaryResponse fileRecord = fileUploadService.uploadFile(fileRecordRequestDto);
//        return ResponseEntity.ok(fileRecord);
//
//    }

    @PostMapping("/upload")
    public ResponseEntity<FileSummaryResponse> uploadFile(
            @RequestParam("fileName") String fileName,
            @RequestParam("file") MultipartFile file,
            @RequestParam("accountType") String accountType
    ) {
        FileRecordRequestDto dto = new FileRecordRequestDto();
        dto.setFileName(fileName);
        dto.setFile(file);
        dto.setAccountType(accountType);

        FileSummaryResponse response = fileUploadService.uploadFile(dto);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/summary")
//    public ResponseEntity<Page<FileRecord>> getSummary(
//            @RequestParam String country,
//            @RequestParam String accountType,
//            @RequestParam String startDate,
//            @RequestParam String endDate,
//            @RequestParam int page,
//            @RequestParam int size
//    ) {
//        return ResponseEntity.ok(
//                fileUploadService.getSummary(
//                        country,
//                        accountType,
//                        LocalDate.parse(startDate),
//                        LocalDate.parse(endDate),
//                        page,
//                        size
//                )
//        );
//    }


    @GetMapping("/{id}")
    public ResponseEntity<FileSummaryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fileUploadService.getById(id));
    }


    @PostMapping("/{fileId}/approve")
    public ResponseEntity<?> approve(@PathVariable Long fileId, @RequestBody String comment) {
        return ResponseEntity.ok(fileUploadService.approve(fileId, comment));
    }

    @PostMapping("/{fileId}/reject")
    public ResponseEntity<?> reject(@PathVariable Long fileId, @RequestBody String comment) {
        return ResponseEntity.ok(fileUploadService.reject(fileId, comment));
    }

    @PostMapping("/{id}/reinitiate")
    @PreAuthorize("hasRole('INITIATOR')")
    public ResponseEntity<?> reinitiate(@PathVariable Long id) {
        return ResponseEntity.ok(fileUploadService.reinitiate(id));
    }

    @PostMapping("/{id}/push")
    @PreAuthorize("hasRole('COO')")
    public ResponseEntity<?> push(@PathVariable Long id) {
        return ResponseEntity.ok(fileUploadService.pushToCore(id));
    }


//    @PostMapping("/summary")
//    public ResponseEntity<Page<FileRecord>> summary(@RequestBody FileSummaryRequest request) {
//        Page<FileRecord> results = fileUploadService.getPaginatedFiles(request);
//        return ResponseEntity.ok(results);
//    }

}
