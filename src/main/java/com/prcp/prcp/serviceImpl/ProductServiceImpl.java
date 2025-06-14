package com.prcp.prcp.serviceImpl;

import com.prcp.prcp.dto.CountryConfigResponse;
import com.prcp.prcp.dto.ProductRequestDto;
import com.prcp.prcp.dto.ProductResponseDto;
import com.prcp.prcp.entity.Product;
import com.prcp.prcp.repository.ProductRepository;
import com.prcp.prcp.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {

        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .binRange(productRequestDto.getBinRange())
                .countryCode(productRequestDto.getCountryCode())
                .createdBy(productRequestDto.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);

        ProductResponseDto productResponseDto = new ProductResponseDto();
        BeanUtils.copyProperties(savedProduct, productResponseDto);
        return productResponseDto;
    }

    @Override
    public Page<ProductResponseDto> listAll(Pageable pageable) {

        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> {
            ProductResponseDto response = new ProductResponseDto();
            BeanUtils.copyProperties(product, response);
            return response;
        });
    }


}
