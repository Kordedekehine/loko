package com.prcp.prcp.controller;

import com.prcp.prcp.dto.CountryConfigRequestDto;
import com.prcp.prcp.dto.CountryConfigResponse;
import com.prcp.prcp.dto.ProductRequestDto;
import com.prcp.prcp.dto.ProductResponseDto;
import com.prcp.prcp.service.CountryConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config")
@PreAuthorize("hasAnyRole('INITIATOR')")
public class CountryConfigController {

    @Autowired
    private CountryConfigService countryConfigService;

    @PostMapping("/create")
    public ResponseEntity<CountryConfigResponse> createProduct(@RequestBody CountryConfigRequestDto countryConfigRequestDto) {
        CountryConfigResponse response = countryConfigService.createCountryConfig(countryConfigRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/getAll")
    public ResponseEntity<Page<CountryConfigResponse>> listAllConfigs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CountryConfigResponse> response = countryConfigService.listAll(pageable);
        return ResponseEntity.ok(response);
    }
}
