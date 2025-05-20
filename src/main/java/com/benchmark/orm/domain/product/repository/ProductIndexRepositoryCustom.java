package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import com.benchmark.orm.domain.product.entity.ProductIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * 인덱스가 적용된 상품 리포지토리의 커스텀 인터페이스
 * JPA 기본 인터페이스에서 제공하지 않는 기능을 확장하기 위한 인터페이스
 */
public interface ProductIndexRepositoryCustom {

    /**
     * 상품명으로 상품 조회
     * @param name 상품명
     * @return 상품 Optional 객체
     */
    Optional<ProductIndex> findByName(String name);

    /**
     * 가격 범위로 상품 조회
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 상품 리스트
     */
    List<ProductIndex> findByPriceBetween(int minPrice, int maxPrice);

    /**
     * 브랜드 ID로 상품 조회
     * @param brandId 브랜드 ID
     * @return 상품 리스트
     */
    List<ProductIndex> findByBrandId(Long brandId);

    /**
     * 카테고리 ID로 상품 조회
     * @param categoryId 카테고리 ID
     * @return 상품 리스트
     */
    List<ProductIndex> findByCategoryId(Long categoryId);

    /**
     * 페이징 및 정렬 기능을 사용하여 모든 상품 조회
     * @param pageable 페이징 정보
     * @return 페이징된 상품 리스트
     */
    Page<ProductIndex> findAllWithPaging(Pageable pageable);

    /**
     * 특정 정렬 방식으로 모든 상품 조회
     * @param sort 정렬 정보
     * @return 정렬된 상품 리스트
     */
    List<ProductIndex> findAllWithSorting(Sort sort);

    /**
     * 브랜드 정보와 함께 상품 조회
     * @param productIndexId 상품 ID
     * @return 상품 Optional 객체
     */
    Optional<ProductIndex> findProductIndexWithBrand(Long productIndexId);

    /**
     * 카테고리 정보와 함께 상품 조회
     * @param productIndexId 상품 ID
     * @return 상품 Optional 객체
     */
    Optional<ProductIndex> findProductIndexWithCategory(Long productIndexId);

    /**
     * 이미지 정보와 함께 상품 조회
     * @param productIndexId 상품 ID
     * @return 상품 Optional 객체
     */
    Optional<ProductIndex> findProductIndexWithImages(Long productIndexId);

    /**
     * 브랜드, 카테고리, 이미지 정보와 함께 상품 조회
     * @param productIndexId 상품 ID
     * @return 상품 Optional 객체
     */
    Optional<ProductIndex> findProductIndexWithAllDetails(Long productIndexId);

    /**
     * 검색 조건을 이용한 상품 검색 (QueryDSL 사용)
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 페이징된 상품 정보
     */
    Page<ProductIndex> searchProductIndexs(ProductSearchDto searchDto, Pageable pageable);
}