package com.prcp.prcp.repository;

import com.prcp.prcp.dto.FileSummaryResponse;
import com.prcp.prcp.entity.FileRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileRecord, Long>, JpaSpecificationExecutor<FileRecord> {

    Page<FileRecord> findByCountryCode(String countryCode, Pageable pageable);

//    Page<FileRecord> findByCountryCodeAndAccountTypeAndDateOfUploadBetween(
//            String countryCode,
//            String accountType,
//            LocalDateTime startDate,
//            LocalDateTime endDate,
//            Pageable pageable
//    );


    Optional<FileRecord> findById(Long id);
    //FileRecord findById(Long id);
}
