package com.prcp.prcp.entity;

import com.prcp.prcp.enums.ApprovalStage;
import com.prcp.prcp.enums.FileStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private String state;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    @Enumerated(EnumType.STRING)
    private ApprovalStage currentStage;

    private String accountType; // POOL, TRANSIT, REVENUE

    private String countryCode;

    private String comment;

    private Long userId;

    private String approvalStage;

    private LocalDateTime uploadedAt;
}
