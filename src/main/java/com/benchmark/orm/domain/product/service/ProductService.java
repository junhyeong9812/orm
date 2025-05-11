package com.benchmark.orm.domain.product.service;

import com.benchmark.orm.domain.product.dto.ProductRequestDto;
import com.benchmark.orm.domain.product.dto.ProductResponseDto;
import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * 상품 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface ProductService {

    /**
     * JPA를 사용하여 상품 저장
     * @param productDto 저장할 상품 DTO
     * @return 저장된 상품 응답 DTO
     */
    ProductResponseDto saveProductJpa(ProductRequestDto productDto);

    /**
     * MyBatis를 사용하여 상품 저장
     * @param productDto 저장할 상품 DTO
     * @return 결과 메시지
     */
    String saveProductMyBatis(ProductRequestDto productDto);

    /**
     * JPA를 사용하여 ID로 상품 조회
     * @param id 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductByIdJpa(Long id);

    /**
     * MyBatis를 사용하여 ID로 상품 조회
     * @param id 상품 ID
     * @return 상품 응답 DTO
     */
    ProductResponseDto findProductByIdMyBatis(Long id);

    /**
     * JPA를 사용하여 모든 상품 조회
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findAllProductsJpa();

    /**
     * MyBatis를 사용하여 모든 상품 조회
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findAllProductsMyBatis();

    /**
     * JPQL을 사용하여 상품명으로 상품 조회
     * @param name 상품명
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductByNameJpql(String name);

    /**
     * QueryDSL을 사용하여 상품명으로 상품 조회
     * @param name 상품명
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductByNameQueryDsl(String name);

    /**
     * JPQL을 사용하여 가격 범위로 상품 조회
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsByPriceBetweenJpql(int minPrice, int maxPrice);

    /**
     * QueryDSL을 사용하여 가격 범위로 상품 조회
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsByPriceBetweenQueryDsl(int minPrice, int maxPrice);

    /**
     * JPQL을 사용하여 브랜드 ID로 상품 조회
     * @param brandId 브랜드 ID
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsByBrandIdJpql(Long brandId);

    /**
     * QueryDSL을 사용하여 브랜드 ID로 상품 조회
     * @param brandId 브랜드 ID
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsByBrandIdQueryDsl(Long brandId);

    /**
     * JPQL을 사용하여 카테고리 ID로 상품 조회
     * @param categoryId 카테고리 ID
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsByCategoryIdJpql(Long categoryId);

    /**
     * QueryDSL을 사용하여 카테고리 ID로 상품 조회
     * @param categoryId 카테고리 ID
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsByCategoryIdQueryDsl(Long categoryId);

    /**
     * JPA를 사용하여 페이징으로 상품 조회
     * @param pageable 페이징 정보
     * @return 페이징된 상품 응답 DTO 객체
     */
    Page<ProductResponseDto> findProductsWithPagingJpa(Pageable pageable);

    /**
     * QueryDSL을 사용하여 페이징으로 상품 조회
     * @param pageable 페이징 정보
     * @return 페이징된 상품 응답 DTO 객체
     */
    Page<ProductResponseDto> findProductsWithPagingQueryDsl(Pageable pageable);

    /**
     * MyBatis를 사용하여 페이징으로 상품 조회
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @return 페이징된 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsWithPagingMyBatis(int offset, int limit);

    /**
     * JPA를 사용하여 정렬로 상품 조회
     * @param sort 정렬 정보
     * @return 정렬된 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsWithSortingJpa(Sort sort);

    /**
     * QueryDSL을 사용하여 정렬로 상품 조회
     * @param sort 정렬 정보
     * @return 정렬된 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsWithSortingQueryDsl(Sort sort);

    /**
     * MyBatis를 사용하여 정렬로 상품 조회
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 정렬된 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsWithSortingMyBatis(String sortColumn, String sortDirection);

    /**
     * JPA를 사용하여 페이징 및 정렬로 상품 조회
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징 및 정렬된 상품 응답 DTO 객체
     */
    Page<ProductResponseDto> findProductsWithPagingAndSortingJpa(Pageable pageable);

    /**
     * MyBatis를 사용하여 페이징 및 정렬로 상품 조회
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 페이징 및 정렬된 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> findProductsWithPagingAndSortingMyBatis(int offset, int limit, String sortColumn, String sortDirection);

    /**
     * JPQL을 사용하여 브랜드 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductWithBrandJpql(Long productId);

    /**
     * QueryDSL을 사용하여 브랜드 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductWithBrandQueryDsl(Long productId);

    /**
     * JPQL을 사용하여 카테고리 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductWithCategoryJpql(Long productId);

    /**
     * QueryDSL을 사용하여 카테고리 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductWithCategoryQueryDsl(Long productId);

    /**
     * JPQL을 사용하여 이미지 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductWithImagesJpql(Long productId);

    /**
     * QueryDSL을 사용하여 이미지 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductWithImagesQueryDsl(Long productId);

    /**
     * QueryDSL을 사용하여 모든 상세 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 응답 DTO Optional 객체
     */
    Optional<ProductResponseDto> findProductWithAllDetailsQueryDsl(Long productId);

    /**
     * JPQL을 사용하여 키워드로 상품 검색
     * @param keyword 검색 키워드
     * @return 상품 응답 DTO 리스트
     */
    List<ProductResponseDto> searchProductsByKeywordJpql(String keyword);

    /**
     * JPQL을 사용한 상품 검색
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 페이징된 상품 응답 DTO 객체
     */
    Page<ProductResponseDto> searchProductsJpql(ProductSearchDto searchDto, Pageable pageable);

    /**
     * QueryDSL을 사용한 상품 검색
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 페이징된 상품 응답 DTO 객체
     */
    Page<ProductResponseDto> searchProductsQueryDsl(ProductSearchDto searchDto, Pageable pageable);

    /**
     * MyBatis를 사용한 상품 검색
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 페이징 및 정렬된 상품 응답 DTO 리스트와 총 개수
     */
    Page<ProductResponseDto> searchProductsMyBatis(ProductSearchDto searchDto, int offset, int limit,
                                                   String sortColumn, String sortDirection);

    /**
     * JPA를 사용하여 상품 정보 업데이트
     * @param id 상품 ID
     * @param productDto 업데이트할 상품 DTO
     * @return 업데이트된 상품 응답 DTO
     */
    ProductResponseDto updateProductJpa(Long id, ProductRequestDto productDto);

    /**
     * MyBatis를 사용하여 상품 정보 업데이트
     * @param id 상품 ID
     * @param productDto 업데이트할 상품 DTO
     * @return 결과 메시지
     */
    String updateProductMyBatis(Long id, ProductRequestDto productDto);

    /**
     * JPA를 사용하여 상품 삭제
     * @param id 삭제할 상품 ID
     * @return 결과 메시지
     */
    String deleteProductJpa(Long id);

    /**
     * MyBatis를 사용하여 상품 삭제
     * @param id 삭제할 상품 ID
     * @return 결과 메시지
     */
    String deleteProductMyBatis(Long id);
}