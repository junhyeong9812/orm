package com.benchmark.orm.domain.product.service;

import com.benchmark.orm.domain.product.dto.*;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.repository.ProductRepository;
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
public class ProductJpaService {

    private final ProductRepository productRepository;

    /**
     * 모든 상품 조회 (간단 버전)
     */
    public List<ProductSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepository.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 모든 상품 조회 (상세 버전)
     */
    public List<ProductResponseDto> findAllDetailed() {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepository.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findAllDetailed - 실행시간: {}ms, 결과 수: {}", endTime - startTime, products.size());

        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 상품 조회
     */
    public ProductResponseDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + id));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findById - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 상품명으로 조회
     */
    public ProductResponseDto findByName(String name) {
        long startTime = System.currentTimeMillis();
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. 이름: " + name));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findByName - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 가격 범위로 조회
     */
    public List<ProductSimpleDto> findByPriceBetween(int minPrice, int maxPrice) {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findByPriceBetween - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 브랜드 ID로 조회
     */
    public List<ProductSimpleDto> findByBrandId(Long brandId) {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepository.findByBrandId(brandId);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findByBrandId - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 ID로 조회
     */
    public List<ProductSimpleDto> findByCategoryId(Long categoryId) {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepository.findByCategoryId(categoryId);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findByCategoryId - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 조회
     */
    public Page<ProductSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = productRepository.findAllWithPaging(pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findAllWithPaging - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.getTotalElements());

        return products.map(ProductSimpleDto::from);
    }

    /**
     * 정렬 조회
     */
    public List<ProductSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        List<Product> products = productRepository.findAllWithSorting(sort);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 브랜드 정보와 함께 조회
     */
    public ProductResponseDto findProductWithBrand(Long productId) {
        long startTime = System.currentTimeMillis();
        Product product = productRepository.findProductWithBrand(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findProductWithBrand - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 카테고리 정보와 함께 조회
     */
    public ProductResponseDto findProductWithCategory(Long productId) {
        long startTime = System.currentTimeMillis();
        Product product = productRepository.findProductWithCategory(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findProductWithCategory - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 이미지 정보와 함께 조회
     */
    public ProductResponseDto findProductWithImages(Long productId) {
        long startTime = System.currentTimeMillis();
        Product product = productRepository.findProductWithImages(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findProductWithImages - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 모든 상세 정보와 함께 조회
     */
    public ProductResponseDto findProductWithAllDetails(Long productId) {
        long startTime = System.currentTimeMillis();
        Product product = productRepository.findProductWithAllDetails(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + productId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product findProductWithAllDetails - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 검색 조건으로 상품 검색
     */
    public Page<ProductSimpleDto> searchProducts(ProductSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = productRepository.searchProducts(searchDto, pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product searchProducts - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.getTotalElements());

        return products.map(ProductSimpleDto::from);
    }

    /**
     * 상품 생성
     */
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        Product product = requestDto.toEntity();
        Product savedProduct = productRepository.save(product);

        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product createProduct - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(savedProduct);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + id));

        // 엔티티 업데이트
        if (requestDto.getName() != null) {
            product.updateInfo(requestDto.getName(),
                    requestDto.getPrice() != 0 ? requestDto.getPrice() : product.getPrice());
        }

        Product savedProduct = productRepository.save(product);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product updateProduct - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(savedProduct);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void deleteProduct(Long id) {
        long startTime = System.currentTimeMillis();

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }

        productRepository.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Product deleteProduct - 실행시간: {}ms", endTime - startTime);
    }
}