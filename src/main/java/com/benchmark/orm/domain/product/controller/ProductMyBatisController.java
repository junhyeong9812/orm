package com.benchmark.orm.domain.product.controller;

import com.benchmark.orm.domain.product.dto.*;
import com.benchmark.orm.domain.product.service.ProductMyBatisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/mybatis/product")
@RequiredArgsConstructor
public class ProductMyBatisController {

    private final ProductMyBatisService productMyBatisService;

    /**
     * 모든 상품 조회 (간단 버전)
     */
    @GetMapping
    public ResponseEntity<List<ProductSimpleDto>> getAllProducts() {
        log.info("[MyBatis] GET /api/mybatis/product - 모든 상품 조회 요청");
        List<ProductSimpleDto> products = productMyBatisService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * 모든 상품 조회 (상세 버전)
     */
    @GetMapping("/detailed")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsDetailed() {
        log.info("[MyBatis] GET /api/mybatis/product/detailed - 모든 상품 상세 조회 요청");
        List<ProductResponseDto> products = productMyBatisService.findAllDetailed();
        return ResponseEntity.ok(products);
    }

    /**
     * ID로 상품 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        log.info("[MyBatis] GET /api/mybatis/product/{} - ID로 상품 조회 요청", id);
        ProductResponseDto product = productMyBatisService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품명으로 조회
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductResponseDto> getProductByName(@PathVariable String name) {
        log.info("[MyBatis] GET /api/mybatis/product/name/{} - 상품명으로 조회 요청", name);
        ProductResponseDto product = productMyBatisService.findByName(name);
        return ResponseEntity.ok(product);
    }

    /**
     * 가격 범위로 조회
     */
    @GetMapping("/price")
    public ResponseEntity<List<ProductSimpleDto>> getProductsByPriceRange(
            @RequestParam int minPrice,
            @RequestParam int maxPrice) {
        log.info("[MyBatis] GET /api/mybatis/product/price - 가격 범위 조회 요청: {} ~ {}", minPrice, maxPrice);
        List<ProductSimpleDto> products = productMyBatisService.findByPriceBetween(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * 브랜드 ID로 조회
     */
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductSimpleDto>> getProductsByBrandId(@PathVariable Long brandId) {
        log.info("[MyBatis] GET /api/mybatis/product/brand/{} - 브랜드 ID로 조회 요청", brandId);
        List<ProductSimpleDto> products = productMyBatisService.findByBrandId(brandId);
        return ResponseEntity.ok(products);
    }

    /**
     * 카테고리 ID로 조회
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductSimpleDto>> getProductsByCategoryId(@PathVariable Long categoryId) {
        log.info("[MyBatis] GET /api/mybatis/product/category/{} - 카테고리 ID로 조회 요청", categoryId);
        List<ProductSimpleDto> products = productMyBatisService.findByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 페이징 조회
     */
    @GetMapping("/paging")
    public ResponseEntity<ProductPageDto<ProductSimpleDto>> getProductsWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[MyBatis] GET /api/mybatis/product/paging - 페이징 조회 요청: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);
        ProductPageDto<ProductSimpleDto> products = productMyBatisService.findAllWithPaging(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(products);
    }

    /**
     * 정렬 조회
     */
    @GetMapping("/sorting")
    public ResponseEntity<List<ProductSimpleDto>> getProductsWithSorting(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[MyBatis] GET /api/mybatis/product/sorting - 정렬 조회 요청: sortBy={}, sortDirection={}",
                sortBy, sortDirection);
        List<ProductSimpleDto> products = productMyBatisService.findAllWithSorting(sortBy, sortDirection);
        return ResponseEntity.ok(products);
    }

    /**
     * 이미지 정보와 함께 조회
     */
    @GetMapping("/{id}/with-images")
    public ResponseEntity<ProductResponseDto> getProductWithImages(@PathVariable Long id) {
        log.info("[MyBatis] GET /api/mybatis/product/{}/with-images - 이미지 정보와 함께 조회 요청", id);
        ProductResponseDto product = productMyBatisService.findProductWithImages(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 검색 조건으로 상품 검색
     */
    @GetMapping("/search")
    public ResponseEntity<ProductPageDto<ProductSimpleDto>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        log.info("[MyBatis] GET /api/mybatis/product/search - 검색 요청: keyword={}, minPrice={}, maxPrice={}, brandId={}, categoryId={}",
                keyword, minPrice, maxPrice, brandId, categoryId);

        ProductSearchDto searchDto = ProductSearchDto.builder()
                .keyword(keyword)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .brandId(brandId)
                .categoryId(categoryId)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        ProductPageDto<ProductSimpleDto> products = productMyBatisService.searchProducts(searchDto, page, size);
        return ResponseEntity.ok(products);
    }

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        log.info("[MyBatis] POST /api/mybatis/product - 상품 생성 요청: {}", requestDto.getName());
        ProductResponseDto product = productMyBatisService.createProduct(requestDto);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto requestDto) {
        log.info("[MyBatis] PUT /api/mybatis/product/{} - 상품 수정 요청", id);
        ProductResponseDto product = productMyBatisService.updateProduct(id, requestDto);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("[MyBatis] DELETE /api/mybatis/product/{} - 상품 삭제 요청", id);
        productMyBatisService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}