package com.benchmark.orm.domain.product.entity;

import com.benchmark.orm.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private boolean isThumbnail;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 이미지 정보 업데이트
     *
     * @param url 변경할 URL
     * @return 업데이트된 상품 이미지 엔티티
     */
    public ProductImage updateInfo(String url) {
        this.url = url;
        return this;
    }

    /**
     * 썸네일 설정
     *
     * @param isThumbnail 썸네일 여부
     * @return 현재 상품 이미지 엔티티
     */
    public ProductImage markAsThumbnail(boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
        return this;
    }

    /**
     * 상품 할당
     *
     * @param product 할당할 상품
     * @return 현재 상품 이미지 엔티티
     */
    public ProductImage assignProduct(Product product) {
        // 이전 상품에서 이미지 제거
        if (this.product != null && this.product != product) {
            this.product.getImages().remove(this);
        }

        // 상품 설정
        this.product = product;

        // 새 상품에 이미지 추가 (무한 루프 방지)
        if (product != null && !product.getImages().contains(this)) {
            product.getImages().add(this);
        }

        return this;
    }
}