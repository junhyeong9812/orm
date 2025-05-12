package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import com.benchmark.orm.domain.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 상품 매퍼 인터페이스
 * <p>
 * MyBatis를 사용한 상품 데이터 접근을 위한 매퍼
 */
@Mapper
public interface ProductMapper {
    /**
     * 상품 등록
     * @param product 등록할 상품
     */
    void insert(Product product);

    /**
     * 상품 정보 수정
     * @param product 수정할 상품
     */
    void update(Product product);

    /**
     * ID로 상품 삭제
     * @param id 삭제할 상품 ID
     */
    void deleteById(Long id);

    /**
     * ID로 상품 조회 (브랜드, 카테고리 포함)
     * @param id 조회할 상품 ID
     * @return 조회된 상품
     */
    Product findById(Long id);

    /**
     * 모든 상품 조회 (브랜드, 카테고리 포함)
     * @return 모든 상품 리스트
     */
    List<Product> findAll();

    /**
     * 페이징된 상품 조회 (브랜드, 카테고리 포함)
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 페이징된 상품 리스트
     */
    List<Product> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 정렬된 상품 조회 (브랜드, 카테고리 포함)
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 정렬된 상품 리스트
     */
    List<Product> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);

    /**
     * 페이징 및 정렬된 상품 조회 (브랜드, 카테고리 포함)
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 페이징 및 정렬된 상품 리스트
     */
    List<Product> findAllWithPagingAndSorting(@Param("offset") int offset,
                                              @Param("limit") int limit,
                                              @Param("sortColumn") String sortColumn,
                                              @Param("sortDirection") String sortDirection);

    /**
     * 상품과 이미지 함께 조회
     * @param id 상품 ID
     * @return 이미지를 포함한 상품 정보
     */
    Product findProductWithImages(@Param("id") Long id);

    /**
     * 상품명으로 상품 검색
     * @param name 상품명
     * @return 검색된 상품
     */
    Product findByName(@Param("name") String name);

    /**
     * 가격 범위로 상품 검색
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 검색된 상품 리스트
     */
    List<Product> findByPriceBetween(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

    /**
     * 브랜드 ID로 상품 검색
     * @param brandId 브랜드 ID
     * @return 검색된 상품 리스트
     */
    List<Product> findByBrandId(@Param("brandId") Long brandId);

    /**
     * 카테고리 ID로 상품 검색
     * @param categoryId 카테고리 ID
     * @return 검색된 상품 리스트
     */
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 검색 조건을 이용한 상품 검색
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 검색된 상품 리스트
     */
    List<Product> searchProducts(@Param("searchDto") ProductSearchDto searchDto,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit,
                                 @Param("sortColumn") String sortColumn,
                                 @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 상품 총 개수 조회
     * @param searchDto 검색 조건 DTO
     * @return 검색된 상품 총 개수
     */
    int countBySearchDto(@Param("searchDto") ProductSearchDto searchDto);
}