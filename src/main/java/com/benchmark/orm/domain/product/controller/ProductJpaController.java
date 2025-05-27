package com.benchmark.orm.domain.product.controller;

import com.benchmark.orm.domain.product.dto.*;
import com.benchmark.orm.domain.product.service.ProductJpaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/jpa/product")
@RequiredArgsConstructor
public class ProductJpaController {

    private final ProductJpaService productJpaService;

    /**
     * 모든 상품 조회 (간단 버전)
     */
    @GetMapping
    public ResponseEntity<List<ProductSimpleDto>> getAllProducts() {
        log.info("[JPA] GET /api/jpa/product - 모든 상품 조회 요청");
        List<ProductSimpleDto> products = productJpaService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * 모든 상품 조회 (상세 버전)
     */
    @GetMapping("/detailed")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsDetailed() {
        log.info("[JPA] GET /api/jpa/product/detailed - 모든 상품 상세 조회 요청");
        List<ProductResponseDto> products = productJpaService.findAllDetailed();
        return ResponseEntity.ok(products);
    }

    /**
     * ID로 상품 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/product/{} - ID로 상품 조회 요청", id);
        ProductResponseDto product = productJpaService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품명으로 조회
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductResponseDto> getProductByName(@PathVariable String name) {
        log.info("[JPA] GET /api/jpa/product/name/{} - 상품명으로 조회 요청", name);
        ProductResponseDto product = productJpaService.findByName(name);
        return ResponseEntity.ok(product);
    }

    /**
     * 가격 범위로 조회
     */
    @GetMapping("/price")
    public ResponseEntity<List<ProductSimpleDto>> getProductsByPriceRange(
            @RequestParam int minPrice,
            @RequestParam int maxPrice) {
        log.info("[JPA] GET /api/jpa/product/price - 가격 범위 조회 요청: {} ~ {}", minPrice, maxPrice);
        List<ProductSimpleDto> products = productJpaService.findByPriceBetween(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * 브랜드 ID로 조회
     */
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductSimpleDto>> getProductsByBrandId(@PathVariable Long brandId) {
        log.info("[JPA] GET /api/jpa/product/brand/{} - 브랜드 ID로 조회 요청", brandId);
        List<ProductSimpleDto> products = productJpaService.findByBrandId(brandId);
        return ResponseEntity.ok(products);
    }

    /**
     * 카테고리 ID로 조회
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductSimpleDto>> getProductsByCategoryId(@PathVariable Long categoryId) {
        log.info("[JPA] GET /api/jpa/product/category/{} - 카테고리 ID로 조회 요청", categoryId);
        List<ProductSimpleDto> products = productJpaService.findByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 페이징 조회
     */
    @GetMapping("/paging")
    public ResponseEntity<Page<ProductSimpleDto>> getProductsWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[JPA] GET /api/jpa/product/paging - 페이징 조회 요청: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);
        Page<ProductSimpleDto> products = productJpaService.findAllWithPaging(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(products);
    }

    /**
     * 정렬 조회
     */
    @GetMapping("/sorting")
    public ResponseEntity<List<ProductSimpleDto>> getProductsWithSorting(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[JPA] GET /api/jpa/product/sorting - 정렬 조회 요청: sortBy={}, sortDirection={}",
                sortBy, sortDirection);
        List<ProductSimpleDto> products = productJpaService.findAllWithSorting(sortBy, sortDirection);
        return ResponseEntity.ok(products);
    }

    /**
     * 브랜드 정보와 함께 조회
     */
    @GetMapping("/{id}/with-brand")
    public ResponseEntity<ProductResponseDto> getProductWithBrand(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/product/{}/with-brand - 브랜드 정보와 함께 조회 요청", id);
        ProductResponseDto product = productJpaService.findProductWithBrand(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 카테고리 정보와 함께 조회
     */
    @GetMapping("/{id}/with-category")
    public ResponseEntity<ProductResponseDto> getProductWithCategory(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/product/{}/with-category - 카테고리 정보와 함께 조회 요청", id);
        ProductResponseDto product = productJpaService.findProductWithCategory(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 이미지 정보와 함께 조회
     */
    @GetMapping("/{id}/with-images")
    public ResponseEntity<ProductResponseDto> getProductWithImages(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/product/{}/with-images - 이미지 정보와 함께 조회 요청", id);
        ProductResponseDto product = productJpaService.findProductWithImages(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 모든 상세 정보와 함께 조회
     */
    @GetMapping("/{id}/with-all-details")
    public ResponseEntity<ProductResponseDto> getProductWithAllDetails(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/product/{}/with-all-details - 모든 상세 정보와 함께 조회 요청", id);
        ProductResponseDto product = productJpaService.findProductWithAllDetails(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 검색 조건으로 상품 검색
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductSimpleDto>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        log.info("[JPA] GET /api/jpa/product/search - 검색 요청: keyword={}, minPrice={}, maxPrice={}, brandId={}, categoryId={}",
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

        Page<ProductSimpleDto> products = productJpaService.searchProducts(searchDto, page, size);
        return ResponseEntity.ok(products);
    }

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        log.info("[JPA] POST /api/jpa/product - 상품 생성 요청: {}", requestDto.getName());
        ProductResponseDto product = productJpaService.createProduct(requestDto);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto requestDto) {
        log.info("[JPA] PUT /api/jpa/product/{} - 상품 수정 요청", id);
        ProductResponseDto product = productJpaService.updateProduct(id, requestDto);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("[JPA] DELETE /api/jpa/product/{} - 상품 삭제 요청", id);
        productJpaService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}