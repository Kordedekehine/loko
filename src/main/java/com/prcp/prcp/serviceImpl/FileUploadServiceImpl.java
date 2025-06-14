package com.prcp.prcp.serviceImpl;

import com.prcp.prcp.dto.FileRecordRequestDto;
import com.prcp.prcp.dto.FileSummaryRequest;
import com.prcp.prcp.dto.FileSummaryResponse;
import com.prcp.prcp.dto.UserResponseDto;
import com.prcp.prcp.entity.FileRecord;
import com.prcp.prcp.enums.ApprovalStage;
import com.prcp.prcp.enums.FileStatus;
import com.prcp.prcp.enums.Roles;
import com.prcp.prcp.repository.ApprovalRepository;
import com.prcp.prcp.repository.FileUploadRepository;
import com.prcp.prcp.service.FileUploadService;
import com.prcp.prcp.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {


    private final FileUploadRepository fileUploadRepository;

    private final ApprovalRepository approvalRepo;

   private final UserService userService;


    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository, ApprovalRepository approvalRepo, UserService userService) {
        this.fileUploadRepository = fileUploadRepository;
        this.approvalRepo = approvalRepo;
        this.userService = userService;
    }

    public FileSummaryResponse uploadFile(FileRecordRequestDto fileRecordRequestDto) {
        try {
            UserResponseDto userDetails = userService.getCurrentUser();
            log.info(userDetails.getUsername());
            log.info(userDetails.getId().toString());
            String uploadDir = "uploads/" + userDetails.getState();
            Files.createDirectories(Paths.get(uploadDir));

            String originalFilename = fileRecordRequestDto.getFile().getOriginalFilename();
            String fileExtension = "";
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("Invalid file name");
            }
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }

            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            String filePath = uploadDir + "/" + uniqueFilename;

            fileRecordRequestDto.getFile().transferTo(Paths.get(filePath));

            FileRecord fileRecord = new FileRecord();
            fileRecord.setFileName(uniqueFilename);
            fileRecord.setFilePath(filePath);
            fileRecord.setState(userDetails.getState());
            fileRecord.setUserId(Long.parseLong(userDetails.getId().toString()));
            fileRecord.setUploadedAt(LocalDateTime.now());
            fileRecord.setCountryCode(userDetails.getCountryCode());
            fileRecord.setAccountType(fileRecordRequestDto.getAccountType());
            fileRecord.setComment(fileRecordRequestDto.getComment());
            fileRecord.setStatus(FileStatus.PENDING);

            fileUploadRepository.save(fileRecord);

            FileSummaryResponse response = new FileSummaryResponse();
            response.setFileName(fileRecord.getFileName());
            response.setUploadedAt(fileRecord.getUploadedAt());
            response.setStatus(String.valueOf(fileRecord.getStatus()));
            response.setAccountType(fileRecord.getAccountType());
            response.setCountryCode(fileRecord.getCountryCode());

           // response.setCurrentStage(String.valueOf(fileRecord.getCurrentStage()));
            response.setState(fileRecord.getState());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public FileSummaryResponse approve(Long fileId, String comment) {

        UserResponseDto userDetails = userService.getCurrentUser();
        FileRecord file = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!userDetails.getRole().equalsIgnoreCase(file.getApprovalStage())) {
            throw new RuntimeException("You are not allowed to approve this file at this stage");
        }

        file.setStatus(FileStatus.valueOf(FileStatus.APPROVED.name()));
        file.setComment(comment);

        file.setApprovalStage(getNextStage(userDetails.getRole()));

         fileUploadRepository.save(file);

        FileSummaryResponse response = new FileSummaryResponse();

        BeanUtils.copyProperties(file,response);

       return response;
    }

    private String getNextStage(String currentStage) {
        return switch (currentStage.toUpperCase()) {
            case "INITIATOR" -> "INTERNAL_CONTROL";
            case "INTERNAL_CONTROL" -> "HEAD";
            case "HEAD" -> "COO";
            default -> "COO";
        };
    }


    public FileSummaryResponse reject(Long fileId, String comment) {

        UserResponseDto userDetails = userService.getCurrentUser();
        FileRecord file = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!userDetails.getRole().equalsIgnoreCase(file.getApprovalStage())) {
            throw new RuntimeException("You are not allowed to reject this file");
        }

        file.setStatus(FileStatus.valueOf(FileStatus.REJECTED.name()));
        file.setComment(comment);

        fileUploadRepository.save(file);

        FileSummaryResponse response = new FileSummaryResponse();

        BeanUtils.copyProperties(file,response);

        return response;
    }

    public FileSummaryResponse reinitiate(Long id) {
        FileRecord file = fileUploadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        file.setStatus(FileStatus.PENDING);
        file.setApprovalStage(ApprovalStage.INITIATOR.name());

        fileUploadRepository.save(file);

        FileSummaryResponse response = new FileSummaryResponse();

        BeanUtils.copyProperties(file,response);

        return response;
    }

    public FileSummaryResponse pushToCore(Long id) {
        FileRecord file = fileUploadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!"COO".equalsIgnoreCase(file.getApprovalStage())) {
            throw new RuntimeException("Only COO can push file to core");
        }

        file.setStatus(FileStatus.PUSHED);
        fileUploadRepository.save(file);

        FileSummaryResponse response = new FileSummaryResponse();

        BeanUtils.copyProperties(file,response);

        return response;
    }




//    public Page<FileRecord> getSummary(
//            String country,
//            String accountType,
//            LocalDate startDate,
//            LocalDate endDate,
//            int page,
//            int size
//    ) {
//        return fileUploadRepository.findByCountryCodeAndAccountTypeAndDateOfUploadBetween(
//                country,
//                accountType,
//                startDate.atStartOfDay(),
//                endDate.atTime(23, 59),
//                PageRequest.of(page, size)
//        );
//    }

    public FileSummaryResponse getById(Long id) {
        FileRecord fileRecord = fileUploadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return toDto(fileRecord);
    }

    private FileSummaryResponse toDto(FileRecord fileRecord) {
        FileSummaryResponse dto = new FileSummaryResponse();
        dto.setId(fileRecord.getId());
        dto.setFileName(fileRecord.getFileName());
        dto.setCountryCode(fileRecord.getCountryCode());
        dto.setAccountType(fileRecord.getAccountType());
        return dto;
    }


    public Page<FileRecord> getPaginatedFiles(FileSummaryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("dateOfUpload").descending());

        return fileUploadRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStatus() != null)
                predicates.add(cb.equal(root.get("status"), request.getStatus()));

            if (request.getAccountType() != null)
                predicates.add(cb.equal(root.get("accountType"), request.getAccountType()));

            if (request.getFromDate() != null && request.getToDate() != null)
                predicates.add(cb.between(root.get("dateOfUpload"), request.getFromDate(), request.getToDate()));

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }


}


