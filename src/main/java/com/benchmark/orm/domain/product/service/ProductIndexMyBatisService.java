package com.benchmark.orm.domain.product.service;

import com.benchmark.orm.domain.product.dto.*;
import com.benchmark.orm.domain.product.entity.ProductIndex;
import com.benchmark.orm.domain.product.mapper.ProductIndexMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductIndexMyBatisService {

    private final ProductIndexMapper productIndexMapper;

    /**
     * 모든 상품 조회 (간단 버전)
     */
    public List<ProductIndexSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<ProductIndex> products = productIndexMapper.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ID로 상품 조회
     */
    public ProductIndexSimpleDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexMapper.findById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findById - 실행시간: {}ms", endTime - startTime);

        if (productIndex == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }
        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 상품명으로 조회
     */
    public ProductIndexSimpleDto findByName(String name) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexMapper.findByName(name);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findByName - 실행시간: {}ms", endTime - startTime);

        if (productIndex == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. 이름: " + name);
        }
        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 가격 범위로 조회
     */
    public List<ProductIndexSimpleDto> findByPriceBetween(int minPrice, int maxPrice) {
        long startTime = System.currentTimeMillis();
        List<ProductIndex> products = productIndexMapper.findByPriceBetween(minPrice, maxPrice);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findByPriceBetween - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 브랜드 ID로 조회
     */
    public List<ProductIndexSimpleDto> findByBrandId(Long brandId) {
        long startTime = System.currentTimeMillis();
        List<ProductIndex> products = productIndexMapper.findByBrandId(brandId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findByBrandId - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 ID로 조회
     */
    public List<ProductIndexSimpleDto> findByCategoryId(Long categoryId) {
        long startTime = System.currentTimeMillis();
        List<ProductIndex> products = productIndexMapper.findByCategoryId(categoryId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findByCategoryId - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 조회
     */
    public ProductPageDto<ProductIndexSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        List<ProductIndex> products = productIndexMapper.findAllWithPagingAndSorting(offset, size, sortBy, sortDirection);

        // 전체 개수 조회
        List<ProductIndex> allProducts = productIndexMapper.findAll();
        long totalElements = allProducts.size();

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findAllWithPaging - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, products.size(), totalElements);

        List<ProductIndexSimpleDto> content = products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());

        return ProductPageDto.of(content, page, size, totalElements);
    }

    /**
     * 정렬 조회
     */
    public List<ProductIndexSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();
        List<ProductIndex> products = productIndexMapper.findAllWithSorting(sortBy, sortDirection);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 검색 조건으로 상품 검색
     */
    public ProductPageDto<ProductIndexSimpleDto> searchProductIndexs(ProductSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        List<ProductIndex> products = productIndexMapper.searchProductIndexs(searchDto, offset, size, sortBy, sortDirection);
        int totalCount = productIndexMapper.countBySearchDto(searchDto);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex searchProductIndexs - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, products.size(), totalCount);

        List<ProductIndexSimpleDto> content = products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());

        return ProductPageDto.of(content, page, size, totalCount);
    }

    /**
     * 상품 생성
     */
    @Transactional
    public ProductIndexSimpleDto createProductIndex(ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        // ProductRequestDto를 ProductIndex로 변환하는 로직 필요
        ProductIndex productIndex = convertToProductIndex(requestDto);
        productIndexMapper.insert(productIndex);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex createProductIndex - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public ProductIndexSimpleDto updateProductIndex(Long id, ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        ProductIndex existingProduct = productIndexMapper.findById(id);
        if (existingProduct == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }

        // 기존 상품 정보 업데이트
        if (requestDto.getName() != null) {
            existingProduct.updateInfo(requestDto.getName(),
                    requestDto.getPrice() != 0 ? requestDto.getPrice() : existingProduct.getPrice());
        }

        productIndexMapper.update(existingProduct);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex updateProductIndex - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(existingProduct);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void deleteProductIndex(Long id) {
        long startTime = System.currentTimeMillis();

        ProductIndex productIndex = productIndexMapper.findById(id);
        if (productIndex == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }

        productIndexMapper.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis-Index] ProductIndex deleteProductIndex - 실행시간: {}ms", endTime - startTime);
    }

    /**
     * ProductRequestDto를 ProductIndex로 변환하는 헬퍼 메서드
     */
    private ProductIndex convertToProductIndex(ProductRequestDto requestDto) {
        return ProductIndex.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .build();
    }
}