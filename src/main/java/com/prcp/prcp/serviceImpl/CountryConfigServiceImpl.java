package com.prcp.prcp.serviceImpl;

import com.prcp.prcp.dto.CountryConfigRequestDto;
import com.prcp.prcp.dto.CountryConfigResponse;
import com.prcp.prcp.entity.CountryConfig;
import com.prcp.prcp.repository.CountryConfigRepository;
import com.prcp.prcp.service.CountryConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CountryConfigServiceImpl implements CountryConfigService {

    @Autowired
    private CountryConfigRepository countryConfigRepository;


    @Override
    public CountryConfigResponse createCountryConfig(CountryConfigRequestDto countryConfigRequestDto) {

        CountryConfig config = CountryConfig.builder()
                .countryCode(countryConfigRequestDto.getCountryCode())
                .countryName(countryConfigRequestDto.getCountryName())
                .currency(countryConfigRequestDto.getCurrency())
                .coreBankingAcct(countryConfigRequestDto.getCoreBankingAcct())
                .createdBy(countryConfigRequestDto.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .build();

        CountryConfig savedConfig = countryConfigRepository.save(config);

        CountryConfigResponse response = new CountryConfigResponse();
        BeanUtils.copyProperties(savedConfig, response);

        return response;
    }

    @Override
    public Page<CountryConfigResponse> listAll(Pageable pageable) {

        Page<CountryConfig> countryConfigs = countryConfigRepository.findAll(pageable);
        return countryConfigs.map(config -> {
            CountryConfigResponse response = new CountryConfigResponse();
            BeanUtils.copyProperties(config, response);
            return response;
        });
    }


}
