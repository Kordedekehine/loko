package com.prcp.prcp.repository;

import com.prcp.prcp.entity.CountryConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryConfigRepository extends JpaRepository<CountryConfig, Long> {

    Optional<CountryConfig> findByCountryCode(String code);

    Page<CountryConfig> findAll(Pageable pageable);
}
