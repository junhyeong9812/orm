package com.benchmark.orm.domain.product.controller;

import com.benchmark.orm.domain.product.dto.*;
import com.benchmark.orm.domain.product.service.ProductIndexJpaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/jpa/product-index")
@RequiredArgsConstructor
public class ProductIndexJpaController {

    private final ProductIndexJpaService productIndexJpaService;

    /**
     * 모든 상품 조회
     */
    @GetMapping
    public ResponseEntity<List<ProductIndexSimpleDto>> getAllProducts() {
        log.info("[JPA-Index] GET /api/jpa/product-index - 모든 상품 조회 요청");
        List<ProductIndexSimpleDto> products = productIndexJpaService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * ID로 상품 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductIndexSimpleDto> getProductById(@PathVariable Long id) {
        log.info("[JPA-Index] GET /api/jpa/product-index/{} - ID로 상품 조회 요청", id);
        ProductIndexSimpleDto product = productIndexJpaService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품명으로 조회
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductIndexSimpleDto> getProductByName(@PathVariable String name) {
        log.info("[JPA-Index] GET /api/jpa/product-index/name/{} - 상품명으로 조회 요청", name);
        ProductIndexSimpleDto product = productIndexJpaService.findByName(name);
        return ResponseEntity.ok(product);
    }

    /**
     * 가격 범위로 조회
     */
    @GetMapping("/price")
    public ResponseEntity<List<ProductIndexSimpleDto>> getProductsByPriceRange(
            @RequestParam int minPrice,
            @RequestParam int maxPrice) {
        log.info("[JPA-Index] GET /api/jpa/product-index/price - 가격 범위 조회 요청: {} ~ {}", minPrice, maxPrice);
        List<ProductIndexSimpleDto> products = productIndexJpaService.findByPriceBetween(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * 브랜드 ID로 조회
     */
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductIndexSimpleDto>> getProductsByBrandId(@PathVariable Long brandId) {
        log.info("[JPA-Index] GET /api/jpa/product-index/brand/{} - 브랜드 ID로 조회 요청", brandId);
        List<ProductIndexSimpleDto> products = productIndexJpaService.findByBrandId(brandId);
        return ResponseEntity.ok(products);
    }

    /**
     * 카테고리 ID로 조회
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductIndexSimpleDto>> getProductsByCategoryId(@PathVariable Long categoryId) {
        log.info("[JPA-Index] GET /api/jpa/product-index/category/{} - 카테고리 ID로 조회 요청", categoryId);
        List<ProductIndexSimpleDto> products = productIndexJpaService.findByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 페이징 조회
     */
    @GetMapping("/paging")
    public ResponseEntity<Page<ProductIndexSimpleDto>> getProductsWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[JPA-Index] GET /api/jpa/product-index/paging - 페이징 조회 요청: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);
        Page<ProductIndexSimpleDto> products = productIndexJpaService.findAllWithPaging(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(products);
    }

    /**
     * 정렬 조회
     */
    @GetMapping("/sorting")
    public ResponseEntity<List<ProductIndexSimpleDto>> getProductsWithSorting(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[JPA-Index] GET /api/jpa/product-index/sorting - 정렬 조회 요청: sortBy={}, sortDirection={}",
                sortBy, sortDirection);
        List<ProductIndexSimpleDto> products = productIndexJpaService.findAllWithSorting(sortBy, sortDirection);
        return ResponseEntity.ok(products);
    }

    /**
     * 브랜드 정보와 함께 조회
     */
    @GetMapping("/{id}/with-brand")
    public ResponseEntity<ProductIndexSimpleDto> getProductWithBrand(@PathVariable Long id) {
        log.info("[JPA-Index] GET /api/jpa/product-index/{}/with-brand - 브랜드 정보와 함께 조회 요청", id);
        ProductIndexSimpleDto product = productIndexJpaService.findProductIndexWithBrand(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 카테고리 정보와 함께 조회
     */
    @GetMapping("/{id}/with-category")
    public ResponseEntity<ProductIndexSimpleDto> getProductWithCategory(@PathVariable Long id) {
        log.info("[JPA-Index] GET /api/jpa/product-index/{}/with-category - 카테고리 정보와 함께 조회 요청", id);
        ProductIndexSimpleDto product = productIndexJpaService.findProductIndexWithCategory(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 이미지 정보와 함께 조회
     */
    @GetMapping("/{id}/with-images")
    public ResponseEntity<ProductIndexSimpleDto> getProductWithImages(@PathVariable Long id) {
        log.info("[JPA-Index] GET /api/jpa/product-index/{}/with-images - 이미지 정보와 함께 조회 요청", id);
        ProductIndexSimpleDto product = productIndexJpaService.findProductIndexWithImages(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 모든 상세 정보와 함께 조회
     */
    @GetMapping("/{id}/with-all-details")
    public ResponseEntity<ProductIndexSimpleDto> getProductWithAllDetails(@PathVariable Long id) {
        log.info("[JPA-Index] GET /api/jpa/product-index/{}/with-all-details - 모든 상세 정보와 함께 조회 요청", id);
        ProductIndexSimpleDto product = productIndexJpaService.findProductIndexWithAllDetails(id);
        return ResponseEntity.ok(product);
    }

    /**
     * 검색 조건으로 상품 검색
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductIndexSimpleDto>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        log.info("[JPA-Index] GET /api/jpa/product-index/search - 검색 요청: keyword={}, minPrice={}, maxPrice={}, brandId={}, categoryId={}",
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

        Page<ProductIndexSimpleDto> products = productIndexJpaService.searchProductIndexs(searchDto, page, size);
        return ResponseEntity.ok(products);
    }

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<ProductIndexSimpleDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        log.info("[JPA-Index] POST /api/jpa/product-index - 상품 생성 요청: {}", requestDto.getName());
        ProductIndexSimpleDto product = productIndexJpaService.createProductIndex(requestDto);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductIndexSimpleDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto requestDto) {
        log.info("[JPA-Index] PUT /api/jpa/product-index/{} - 상품 수정 요청", id);
        ProductIndexSimpleDto product = productIndexJpaService.updateProductIndex(id, requestDto);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("[JPA-Index] DELETE /api/jpa/product-index/{} - 상품 삭제 요청", id);
        productIndexJpaService.deleteProductIndex(id);
        return ResponseEntity.ok().build();
    }
}