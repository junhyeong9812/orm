package com.benchmark.orm.domain.product.service;

import com.benchmark.orm.domain.product.dto.*;
import com.benchmark.orm.domain.product.entity.ProductIndex;
import com.benchmark.orm.domain.product.repository.ProductIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductIndexJpaService {

    private final ProductIndexRepository productIndexRepository;

    /**
     * 모든 상품 조회 (간단 버전)
     */
    public List<ProductIndexSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<ProductIndex> products = productIndexRepository.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ID로 상품 조회
     */
    public ProductIndexSimpleDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + id));
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findById - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 상품명으로 조회
     */
    public ProductIndexSimpleDto findByName(String name) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. 이름: " + name));
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findByName - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 가격 범위로 조회
     */
    public List<ProductIndexSimpleDto> findByPriceBetween(int minPrice, int maxPrice) {
        long startTime = System.currentTimeMillis();
        List<ProductIndex> products = productIndexRepository.findByPriceBetween(minPrice, maxPrice);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findByPriceBetween - 실행시간: {}ms, 결과 수: {}",
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
        List<ProductIndex> products = productIndexRepository.findByBrandId(brandId);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findByBrandId - 실행시간: {}ms, 결과 수: {}",
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
        List<ProductIndex> products = productIndexRepository.findByCategoryId(categoryId);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findByCategoryId - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 조회
     */
    public Page<ProductIndexSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductIndex> products = productIndexRepository.findAllWithPaging(pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findAllWithPaging - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.getTotalElements());

        return products.map(ProductIndexSimpleDto::from);
    }

    /**
     * 정렬 조회
     */
    public List<ProductIndexSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        List<ProductIndex> products = productIndexRepository.findAllWithSorting(sort);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductIndexSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 브랜드 정보와 함께 조회
     */
    public ProductIndexSimpleDto findProductIndexWithBrand(Long productIndexId) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexRepository.findProductIndexWithBrand(productIndexId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productIndexId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findProductIndexWithBrand - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 카테고리 정보와 함께 조회
     */
    public ProductIndexSimpleDto findProductIndexWithCategory(Long productIndexId) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexRepository.findProductIndexWithCategory(productIndexId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productIndexId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findProductIndexWithCategory - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 이미지 정보와 함께 조회
     */
    public ProductIndexSimpleDto findProductIndexWithImages(Long productIndexId) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexRepository.findProductIndexWithImages(productIndexId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productIndexId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findProductIndexWithImages - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 모든 상세 정보와 함께 조회
     */
    public ProductIndexSimpleDto findProductIndexWithAllDetails(Long productIndexId) {
        long startTime = System.currentTimeMillis();
        ProductIndex productIndex = productIndexRepository.findProductIndexWithAllDetails(productIndexId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productIndexId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex findProductIndexWithAllDetails - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(productIndex);
    }

    /**
     * 검색 조건으로 상품 검색
     */
    public Page<ProductIndexSimpleDto> searchProductIndexs(ProductSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductIndex> products = productIndexRepository.searchProductIndexs(searchDto, pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex searchProductIndexs - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.getTotalElements());

        return products.map(ProductIndexSimpleDto::from);
    }

    /**
     * 상품 생성
     */
    @Transactional
    public ProductIndexSimpleDto createProductIndex(ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        ProductIndex productIndex = convertToProductIndex(requestDto);
        ProductIndex savedProductIndex = productIndexRepository.save(productIndex);

        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex createProductIndex - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(savedProductIndex);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public ProductIndexSimpleDto updateProductIndex(Long id, ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        ProductIndex productIndex = productIndexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + id));

        if (requestDto.getName() != null) {
            productIndex.updateInfo(requestDto.getName(),
                    requestDto.getPrice() != 0 ? requestDto.getPrice() : productIndex.getPrice());
        }

        ProductIndex savedProductIndex = productIndexRepository.save(productIndex);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex updateProductIndex - 실행시간: {}ms", endTime - startTime);

        return ProductIndexSimpleDto.from(savedProductIndex);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void deleteProductIndex(Long id) {
        long startTime = System.currentTimeMillis();

        if (!productIndexRepository.existsById(id)) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }

        productIndexRepository.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[JPA-Index] ProductIndex deleteProductIndex - 실행시간: {}ms", endTime - startTime);
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