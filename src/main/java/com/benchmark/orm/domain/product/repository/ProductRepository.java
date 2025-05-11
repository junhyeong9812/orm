package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 상품 리포지토리 인터페이스
 * ProductRepositoryCustom 확장
 */
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    /**
     * JPQL을 사용한 상품명으로 상품 조회
     * @param name 상품명
     * @return 상품 Optional 객체
     */
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findByNameJpql(@Param("name") String name);

    /**
     * JPQL을 사용한 가격 범위로 상품 조회
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 상품 리스트
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceBetweenJpql(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

    /**
     * JPQL을 사용한 브랜드 ID로 상품 조회
     * @param brandId 브랜드 ID
     * @return 상품 리스트
     */
    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandId")
    List<Product> findByBrandIdJpql(@Param("brandId") Long brandId);

    /**
     * JPQL을 사용한 카테고리 ID로 상품 조회
     * @param categoryId 카테고리 ID
     * @return 상품 리스트
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryIdJpql(@Param("categoryId") Long categoryId);

    /**
     * JPQL을 사용한 브랜드 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 Optional 객체
     */
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand WHERE p.id = :productId")
    Optional<Product> findProductWithBrandJpql(@Param("productId") Long productId);

    /**
     * JPQL을 사용한 카테고리 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 Optional 객체
     */
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :productId")
    Optional<Product> findProductWithCategoryJpql(@Param("productId") Long productId);

    /**
     * JPQL을 사용한 이미지 정보와 함께 상품 조회
     * @param productId 상품 ID
     * @return 상품 Optional 객체
     */
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :productId")
    Optional<Product> findProductWithImagesJpql(@Param("productId") Long productId);

    /**
     * JPQL을 사용한 키워드로 상품 검색
     * @param keyword 검색 키워드
     * @return 상품 리스트
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    List<Product> searchProductsByKeywordJpql(@Param("keyword") String keyword);

    /**
     * JPQL을 사용한 복합 조건으로 상품 검색 (페이징)
     * @param keyword 검색 키워드
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param brandId 브랜드 ID
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 페이징된 상품 정보
     */
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword%) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:brandId IS NULL OR p.brand.id = :brandId) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> searchProductsJpql(@Param("keyword") String keyword,
                                     @Param("minPrice") Integer minPrice,
                                     @Param("maxPrice") Integer maxPrice,
                                     @Param("brandId") Long brandId,
                                     @Param("categoryId") Long categoryId,
                                     Pageable pageable);
}