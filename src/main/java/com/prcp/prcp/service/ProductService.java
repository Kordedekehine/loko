package com.prcp.prcp.service;

import com.prcp.prcp.dto.CountryConfigResponse;
import com.prcp.prcp.dto.ProductRequestDto;
import com.prcp.prcp.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

   ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    Page<ProductResponseDto> listAll(Pageable pageable);

}
