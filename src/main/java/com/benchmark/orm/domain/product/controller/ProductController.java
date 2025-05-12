package com.benchmark.orm.domain.product.controller;

import com.benchmark.orm.domain.product.dto.ProductRequestDto;
import com.benchmark.orm.domain.product.dto.ProductResponseDto;
import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import com.benchmark.orm.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 상품 관련 API 컨트롤러
 * 각 ORM 기술별 성능 비교를 위한 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * JPA로 상품 생성
     * @param productRequestDto 상품 요청 DTO
     * @return 생성된 상품 정보
     */
    @PostMapping("/jpa")
    public ResponseEntity<ProductResponseDto> createProductJpa(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto savedProduct = productService.saveProductJpa(productRequestDto);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * MyBatis로 상품 생성
     * @param productRequestDto 상품 요청 DTO
     * @return 결과 메시지
     */
    @PostMapping("/mybatis")
    public ResponseEntity<Map<String, String>> createProductMyBatis(@RequestBody ProductRequestDto productRequestDto) {
        String result = productService.saveProductMyBatis(productRequestDto);
        return ResponseEntity.ok(Map.of("message", result));
    }

    /**
     * JPA로 ID별 상품 조회
     * @param id 상품 ID
     * @return 상품 정보
     */
    @GetMapping("/jpa/{id}")
    public ResponseEntity<ProductResponseDto> getProductByIdJpa(@PathVariable Long id) {
        return productService.findProductByIdJpa(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * MyBatis로 ID별 상품 조회
     * @param id 상품 ID
     * @return 상품 정보
     */
    @GetMapping("/mybatis/{id}")
    public ResponseEntity<ProductResponseDto> getProductByIdMyBatis(@PathVariable Long id) {
        ProductResponseDto product = productService.findProductByIdMyBatis(id);
        return product != null
                ? ResponseEntity.ok(product)
                : ResponseEntity.notFound().build();
    }

    /**
     * JPA로 모든 상품 조회
     * @return 상품 목록
     */
    @GetMapping("/jpa")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsJpa() {
        return ResponseEntity.ok(productService.findAllProductsJpa());
    }

    /**
     * MyBatis로 모든 상품 조회
     * @return 상품 목록
     */
    @GetMapping("/mybatis")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsMyBatis() {
        return ResponseEntity.ok(productService.findAllProductsMyBatis());
    }

    /**
     * JPQL로 상품명별 상품 조회
     * @param name 상품명
     * @return 상품 정보
     */
    @GetMapping("/jpql/name/{name}")
    public ResponseEntity<ProductResponseDto> getProductByNameJpql(@PathVariable String name) {
        return productService.findProductByNameJpql(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 상품명별 상품 조회
     * @param name 상품명
     * @return 상품 정보
     */
    @GetMapping("/querydsl/name/{name}")
    public ResponseEntity<ProductResponseDto> getProductByNameQueryDsl(@PathVariable String name) {
        return productService.findProductByNameQueryDsl(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 가격 범위별 상품 조회
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 상품 목록
     */
    @GetMapping("/jpql/price-range")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRangeJpql(
            @RequestParam int minPrice,
            @RequestParam int maxPrice) {
        return ResponseEntity.ok(productService.findProductsByPriceBetweenJpql(minPrice, maxPrice));
    }

    /**
     * QueryDSL로 가격 범위별 상품 조회
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 상품 목록
     */
    @GetMapping("/querydsl/price-range")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRangeQueryDsl(
            @RequestParam int minPrice,
            @RequestParam int maxPrice) {
        return ResponseEntity.ok(productService.findProductsByPriceBetweenQueryDsl(minPrice, maxPrice));
    }

    /**
     * JPQL로 브랜드 ID별 상품 조회
     * @param brandId 브랜드 ID
     * @return 상품 목록
     */
    @GetMapping("/jpql/brand/{brandId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByBrandIdJpql(@PathVariable Long brandId) {
        return ResponseEntity.ok(productService.findProductsByBrandIdJpql(brandId));
    }

    /**
     * QueryDSL로 브랜드 ID별 상품 조회
     * @param brandId 브랜드 ID
     * @return 상품 목록
     */
    @GetMapping("/querydsl/brand/{brandId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByBrandIdQueryDsl(@PathVariable Long brandId) {
        return ResponseEntity.ok(productService.findProductsByBrandIdQueryDsl(brandId));
    }

    /**
     * JPQL로 카테고리 ID별 상품 조회
     * @param categoryId 카테고리 ID
     * @return 상품 목록
     */
    @GetMapping("/jpql/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoryIdJpql(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findProductsByCategoryIdJpql(categoryId));
    }

    /**
     * QueryDSL로 카테고리 ID별 상품 조회
     * @param categoryId 카테고리 ID
     * @return 상품 목록
     */
    @GetMapping("/querydsl/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoryIdQueryDsl(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findProductsByCategoryIdQueryDsl(categoryId));
    }

    /**
     * JPA로 페이징된 상품 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 상품 정보
     */
    @GetMapping("/jpa/paging")
    public ResponseEntity<Page<ProductResponseDto>> getProductsWithPagingJpa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.findProductsWithPagingJpa(PageRequest.of(page, size)));
    }

    /**
     * QueryDSL로 페이징된 상품 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 상품 정보
     */
    @GetMapping("/querydsl/paging")
    public ResponseEntity<Page<ProductResponseDto>> getProductsWithPagingQueryDsl(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.findProductsWithPagingQueryDsl(PageRequest.of(page, size)));
    }

    /**
     * MyBatis로 페이징된 상품 조회
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 페이징된 상품 정보
     */
    @GetMapping("/mybatis/paging")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithPagingMyBatis(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(productService.findProductsWithPagingMyBatis(offset, limit));
    }

    /**
     * JPA로 정렬된 상품 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 상품 정보
     */
    @GetMapping("/jpa/sorting")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithSortingJpa(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(productService.findProductsWithSortingJpa(sort));
    }

    /**
     * QueryDSL로 정렬된 상품 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 상품 정보
     */
    @GetMapping("/querydsl/sorting")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithSortingQueryDsl(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(productService.findProductsWithSortingQueryDsl(sort));
    }

    /**
     * MyBatis로 정렬된 상품 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 상품 정보
     */
    @GetMapping("/mybatis/sorting")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithSortingMyBatis(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(productService.findProductsWithSortingMyBatis(sortBy, direction));
    }

    /**
     * JPA로 페이징 및 정렬된 상품 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 페이징 및 정렬된 상품 정보
     */
    @GetMapping("/jpa/paging-sorting")
    public ResponseEntity<Page<ProductResponseDto>> getProductsWithPagingAndSortingJpa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return ResponseEntity.ok(productService.findProductsWithPagingAndSortingJpa(
                PageRequest.of(page, size, Sort.by(sortDirection, sortBy))));
    }

    /**
     * MyBatis로 페이징 및 정렬된 상품 조회
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 페이징 및 정렬된 상품 정보
     */
    @GetMapping("/mybatis/paging-sorting")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithPagingAndSortingMyBatis(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(productService.findProductsWithPagingAndSortingMyBatis(
                offset, limit, sortBy, direction));
    }

    /**
     * JPQL로 브랜드 정보와 함께 상품 조회
     * @param id 상품 ID
     * @return 브랜드 정보가 포함된 상품 정보
     */
    @GetMapping("/jpql/{id}/with-brand")
    public ResponseEntity<ProductResponseDto> getProductWithBrandJpql(@PathVariable Long id) {
        return productService.findProductWithBrandJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 브랜드 정보와 함께 상품 조회
     * @param id 상품 ID
     * @return 브랜드 정보가 포함된 상품 정보
     */
    @GetMapping("/querydsl/{id}/with-brand")
    public ResponseEntity<ProductResponseDto> getProductWithBrandQueryDsl(@PathVariable Long id) {
        return productService.findProductWithBrandQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 카테고리 정보와 함께 상품 조회
     * @param id 상품 ID
     * @return 카테고리 정보가 포함된 상품 정보
     */
    @GetMapping("/jpql/{id}/with-category")
    public ResponseEntity<ProductResponseDto> getProductWithCategoryJpql(@PathVariable Long id) {
        return productService.findProductWithCategoryJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 카테고리 정보와 함께 상품 조회
     * @param id 상품 ID
     * @return 카테고리 정보가 포함된 상품 정보
     */
    @GetMapping("/querydsl/{id}/with-category")
    public ResponseEntity<ProductResponseDto> getProductWithCategoryQueryDsl(@PathVariable Long id) {
        return productService.findProductWithCategoryQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 이미지 정보와 함께 상품 조회
     * @param id 상품 ID
     * @return 이미지 정보가 포함된 상품 정보
     */
    @GetMapping("/jpql/{id}/with-images")
    public ResponseEntity<ProductResponseDto> getProductWithImagesJpql(@PathVariable Long id) {
        return productService.findProductWithImagesJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 이미지 정보와 함께 상품 조회
     * @param id 상품 ID
     * @return 이미지 정보가 포함된 상품 정보
     */
    @GetMapping("/querydsl/{id}/with-images")
    public ResponseEntity<ProductResponseDto> getProductWithImagesQueryDsl(@PathVariable Long id) {
        return productService.findProductWithImagesQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 모든 상세 정보와 함께 상품 조회
     * @param id 상품 ID
     * @return 모든 상세 정보가 포함된 상품 정보
     */
    @GetMapping("/querydsl/{id}/with-all-details")
    public ResponseEntity<ProductResponseDto> getProductWithAllDetailsQueryDsl(@PathVariable Long id) {
        return productService.findProductWithAllDetailsQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 키워드 기반 상품 검색
     * @param keyword 검색 키워드
     * @return 검색된 상품 목록
     */
    @GetMapping("/jpql/search")
    public ResponseEntity<List<ProductResponseDto>> searchProductsByKeywordJpql(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProductsByKeywordJpql(keyword));
    }

    /**
     * 검색 조건으로 상품 검색
     * @param searchDto 검색 조건 DTO
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 (기본값: id)
     * @param direction 정렬 방향 (기본값: asc)
     * @param type 검색 타입 (jpql, querydsl, mybatis 중 선택, 기본값: querydsl)
     * @return 검색된 상품 목록
     */
    @PostMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestBody ProductSearchDto searchDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "querydsl") String type) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        switch (type.toLowerCase()) {
            case "jpql":
                Page<ProductResponseDto> jpqlResult = productService.searchProductsJpql(
                        searchDto,
                        PageRequest.of(page, size, Sort.by(sortDirection, sortBy))
                );
                return ResponseEntity.ok(jpqlResult);

            case "querydsl":
                Page<ProductResponseDto> queryDslResult = productService.searchProductsQueryDsl(
                        searchDto,
                        PageRequest.of(page, size, Sort.by(sortDirection, sortBy))
                );
                return ResponseEntity.ok(queryDslResult);

            case "mybatis":
                Page<ProductResponseDto> mybatisResult = productService.searchProductsMyBatis(
                        searchDto,
                        page * size,  // offset
                        size,         // limit
                        sortBy,
                        direction
                );
                return ResponseEntity.ok(mybatisResult);

            default:
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid search type. Use 'jpql', 'querydsl', or 'mybatis'"));
        }
    }

    /**
     * JPA로 상품 정보 업데이트
     * @param id 상품 ID
     * @param productRequestDto 업데이트할 상품 DTO
     * @return 업데이트된 상품 정보
     */
    @PutMapping("/jpa/{id}")
    public ResponseEntity<ProductResponseDto> updateProductJpa(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {
        try {
            ProductResponseDto updatedProduct = productService.updateProductJpa(id, productRequestDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * MyBatis로 상품 정보 업데이트
     * @param id 상품 ID
     * @param productRequestDto 업데이트할 상품 DTO
     * @return 결과 메시지
     */
    @PutMapping("/mybatis/{id}")
    public ResponseEntity<Map<String, String>> updateProductMyBatis(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {
        try {
            String result = productService.updateProductMyBatis(id, productRequestDto);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * JPA로 상품 삭제
     * @param id 상품 ID
     * @return 결과 메시지
     */
    @DeleteMapping("/jpa/{id}")
    public ResponseEntity<Map<String, String>> deleteProductJpa(@PathVariable Long id) {
        try {
            String result = productService.deleteProductJpa(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * MyBatis로 상품 삭제
     * @param id 상품 ID
     * @return 결과 메시지
     */
    @DeleteMapping("/mybatis/{id}")
    public ResponseEntity<Map<String, String>> deleteProductMyBatis(@PathVariable Long id) {
        try {
            String result = productService.deleteProductMyBatis(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}