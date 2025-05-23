package com.bright.pagingdemo.service.impl;

import com.bright.pagingdemo.dto.request.ProductRequestDto;
import com.bright.pagingdemo.dto.response.ProductResponseDto;
import com.bright.pagingdemo.exception.product.DuplicateProductException;
import com.bright.pagingdemo.mapper.ProductMapper;
import com.bright.pagingdemo.model.Product;
import com.bright.pagingdemo.repository.ProductRepository;
import com.bright.pagingdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        if (productRepository.findByName(dto.name()).isPresent()) {
            throw new DuplicateProductException("Product name already exist");
        }
        Product product = productMapper.productRequestDtoToProduct(dto);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductResponseDto(savedProduct);
    }

    @Override
    public Page<ProductResponseDto> searchProducts(int page, int pageSize, String sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(
                page,                                       //page number
                pageSize,                                   //page size
                Sort.Direction.fromString(sortDirection),   //sorting direction
                sortBy                                      //sort by which field
        );
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::productToProductResponseDto);
    }

    @Override
    public Page<ProductResponseDto> searchProductsByCategory(String category, int page, int pageSize, String sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(
                page,                                       //page number
                pageSize,                                   //page size
                Sort.Direction.fromString(sortDirection),   //sorting direction
                sortBy                                      //sort by which field
        );
        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        return productPage.map(productMapper::productToProductResponseDto);
    }
}
