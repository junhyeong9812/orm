package com.benchmark.orm.domain.product.service;

import com.benchmark.orm.domain.product.dto.*;
import com.benchmark.orm.domain.product.entity.Brand;
import com.benchmark.orm.domain.product.entity.Category;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.mapper.BrandMapper;
import com.benchmark.orm.domain.product.mapper.CategoryMapper;
import com.benchmark.orm.domain.product.mapper.ProductMapper;
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
public class ProductMyBatisService {

    private final ProductMapper productMapper;
    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 모든 상품 조회 (간단 버전)
     */
    public List<ProductSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<Product> products = productMapper.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 모든 상품 조회 (상세 버전)
     */
    public List<ProductResponseDto> findAllDetailed() {
        long startTime = System.currentTimeMillis();
        List<Product> products = productMapper.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findAllDetailed - 실행시간: {}ms, 결과 수: {}", endTime - startTime, products.size());

        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 상품 조회
     */
    public ProductResponseDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        Product product = productMapper.findById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findById - 실행시간: {}ms", endTime - startTime);

        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }
        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 상품명으로 조회
     */
    public ProductResponseDto findByName(String name) {
        long startTime = System.currentTimeMillis();
        Product product = productMapper.findByName(name);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findByName - 실행시간: {}ms", endTime - startTime);

        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. 이름: " + name);
        }
        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 가격 범위로 조회
     */
    public List<ProductSimpleDto> findByPriceBetween(int minPrice, int maxPrice) {
        long startTime = System.currentTimeMillis();
        List<Product> products = productMapper.findByPriceBetween(minPrice, maxPrice);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findByPriceBetween - 실행시간: {}ms, 결과 수: {}",
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
        List<Product> products = productMapper.findByBrandId(brandId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findByBrandId - 실행시간: {}ms, 결과 수: {}",
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
        List<Product> products = productMapper.findByCategoryId(categoryId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findByCategoryId - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 조회
     */
    public ProductPageDto<ProductSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        List<Product> products = productMapper.findAllWithPagingAndSorting(offset, size, sortBy, sortDirection);

        // 전체 개수 조회 (실제로는 별도 쿼리 필요)
        List<Product> allProducts = productMapper.findAll();
        long totalElements = allProducts.size();

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findAllWithPaging - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, products.size(), totalElements);

        List<ProductSimpleDto> content = products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());

        return ProductPageDto.of(content, page, size, totalElements);
    }

    /**
     * 정렬 조회
     */
    public List<ProductSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();
        List<Product> products = productMapper.findAllWithSorting(sortBy, sortDirection);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, products.size());

        return products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 정보와 함께 조회
     */
    public ProductResponseDto findProductWithImages(Long productId) {
        long startTime = System.currentTimeMillis();
        Product product = productMapper.findProductWithImages(productId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product findProductWithImages - 실행시간: {}ms", endTime - startTime);

        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + productId);
        }
        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 검색 조건으로 상품 검색
     */
    public ProductPageDto<ProductSimpleDto> searchProducts(ProductSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        List<Product> products = productMapper.searchProducts(searchDto, offset, size, sortBy, sortDirection);
        int totalCount = productMapper.countBySearchDto(searchDto);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product searchProducts - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, products.size(), totalCount);

        List<ProductSimpleDto> content = products.stream()
                .map(ProductSimpleDto::from)
                .collect(Collectors.toList());

        return ProductPageDto.of(content, page, size, totalCount);
    }

    /**
     * 상품 생성
     */
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        Product product = requestDto.toEntity();
        productMapper.insert(product);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product createProduct - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(product);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        Product existingProduct = productMapper.findById(id);
        if (existingProduct == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }

        // 기존 상품 정보 업데이트
        if (requestDto.getName() != null) {
            existingProduct.updateInfo(requestDto.getName(),
                    requestDto.getPrice() != 0 ? requestDto.getPrice() : existingProduct.getPrice());
        }

        productMapper.update(existingProduct);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product updateProduct - 실행시간: {}ms", endTime - startTime);

        return ProductResponseDto.fromEntity(existingProduct);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void deleteProduct(Long id) {
        long startTime = System.currentTimeMillis();

        Product product = productMapper.findById(id);
        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + id);
        }

        productMapper.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Product deleteProduct - 실행시간: {}ms", endTime - startTime);
    }
}