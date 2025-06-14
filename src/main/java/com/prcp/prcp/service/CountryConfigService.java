package com.prcp.prcp.service;

import com.prcp.prcp.dto.CountryConfigRequestDto;
import com.prcp.prcp.dto.CountryConfigResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CountryConfigService {

    CountryConfigResponse createCountryConfig(CountryConfigRequestDto countryConfigRequestDto);

    Page<CountryConfigResponse> listAll(Pageable pageable);
}
