package com.benchmark.orm.domain.product.entity;

import com.benchmark.orm.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인덱스가 적용된 상품 이미지 엔티티
 * 기존 ProductImage 엔티티와 유사하지만 ProductIndex와 연결됨
 */
@Entity
@Table(name = "product_index_image",
        indexes = {
                @Index(name = "idx_product_index_image_product", columnList = "product_index_id"),  // 상품 인덱스 ID에 대한 인덱스
                @Index(name = "idx_product_index_image_thumbnail", columnList = "is_thumbnail")     // 썸네일 여부에 대한 인덱스
        })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductIndexImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;            // 이미지 URL
    private boolean isThumbnail;   // 썸네일 여부

    @ManyToOne
    @JoinColumn(name = "product_index_id")
    private ProductIndex productIndex;   // 연결된 상품

    /**
     * 이미지 정보 업데이트
     *
     * @param url 변경할 URL
     * @return 업데이트된 상품 이미지 엔티티
     */
    public ProductIndexImage updateInfo(String url) {
        this.url = url;
        return this;
    }

    /**
     * 썸네일 설정
     *
     * @param isThumbnail 썸네일 여부
     * @return 현재 상품 이미지 엔티티
     */
    public ProductIndexImage markAsThumbnail(boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
        return this;
    }

    /**
     * 상품 할당
     *
     * @param productIndex 할당할 상품
     * @return 현재 상품 이미지 엔티티
     */
    public ProductIndexImage assignProductIndex(ProductIndex productIndex) {
        // 이전 상품에서 이미지 제거
        if (this.productIndex != null && this.productIndex != productIndex) {
            this.productIndex.getImages().remove(this);
        }

        // 상품 설정
        this.productIndex = productIndex;

        // 새 상품에 이미지 추가 (무한 루프 방지)
        if (productIndex != null && !productIndex.getImages().contains(this)) {
            productIndex.getImages().add(this);
        }

        return this;
    }
}