package com.prcp.prcp.controller;

import com.prcp.prcp.dto.FileRecordRequestDto;
import com.prcp.prcp.dto.FileSummaryRequest;
import com.prcp.prcp.dto.FileSummaryResponse;
import com.prcp.prcp.dto.PagedFileResponseDto;
import com.prcp.prcp.entity.FileRecord;
import com.prcp.prcp.repository.FileUploadRepository;
import com.prcp.prcp.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/prcp")
@Slf4j
public class FileController {

    private final FileUploadService fileUploadService;

    private final FileUploadRepository fileUploadRepository;


    public FileController(FileUploadService fileUploadService, FileUploadRepository fileUploadRepository) {
        this.fileUploadService = fileUploadService;
        this.fileUploadRepository = fileUploadRepository;
    }


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

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {

        FileRecord file = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found with ID: " + fileId));

        Path filePath = Paths.get(file.getFilePath());
        log.info("Trying to read file at: {}", filePath);

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File is unreadable or missing at path: " + filePath);
            }

            String contentType = "application/octet-stream";
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException e) {
                log.warn("Could not determine file type. Using default.");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path format", e);
        }
    }



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
    public ResponseEntity<?> reinitiate(@PathVariable Long id) {
        return ResponseEntity.ok(fileUploadService.reinitiate(id));
    }

    @PostMapping("/{id}/push")
    public ResponseEntity<?> push(@PathVariable Long id) {
        return ResponseEntity.ok(fileUploadService.pushToCore(id));
    }


//    @PostMapping("/summary")
//    public ResponseEntity<Page<FileRecord>> summary(@RequestBody FileSummaryRequest request) {
//        Page<FileRecord> results = fileUploadService.getPaginatedFiles(request);
//        return ResponseEntity.ok(results);
//    }

}
