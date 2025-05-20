package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.ProductIndex;
import com.benchmark.orm.domain.product.entity.ProductIndexImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 인덱스가 적용된 상품 이미지 리포지토리 인터페이스
 */
public interface ProductIndexImageRepository extends JpaRepository<ProductIndexImage, Long> {

    /**
     * 상품 ID로 이미지 목록 조회
     * @param productIndexId 상품 ID
     * @return 이미지 목록
     */
    List<ProductIndexImage> findByProductIndexId(Long productIndexId);

    /**
     * 상품 ID로 썸네일 이미지 조회
     * @param productIndexId 상품 ID
     * @return 썸네일 이미지
     */
    Optional<ProductIndexImage> findByProductIndexIdAndIsThumbnailTrue(Long productIndexId);

    /**
     * 상품 ID로 모든 이미지 삭제
     * @param productIndexId 상품 ID
     */
    void deleteByProductIndexId(Long productIndexId);

    /**
     * 상품 ID와 URL로 이미지 조회
     * @param productIndexId 상품 ID
     * @param url 이미지 URL
     * @return 이미지
     */
    Optional<ProductIndexImage> findByProductIndexIdAndUrl(Long productIndexId, String url);

    /**
     * JPQL을 사용한 상품 ID로 이미지 목록 조회
     * @param productIndexId 상품 ID
     * @return 이미지 목록
     */
    @Query("SELECT i FROM ProductIndexImage i WHERE i.productIndex.id = :productIndexId")
    List<ProductIndexImage> findImagesByProductIndexIdJpql(@Param("productIndexId") Long productIndexId);

    /**
     * JPQL을 사용한 상품 ID로 썸네일 이미지 조회
     * @param productIndexId 상품 ID
     * @return 썸네일 이미지
     */
    @Query("SELECT i FROM ProductIndexImage i WHERE i.productIndex.id = :productIndexId AND i.isThumbnail = true")
    Optional<ProductIndexImage> findThumbnailByProductIndexIdJpql(@Param("productIndexId") Long productIndexId);
}