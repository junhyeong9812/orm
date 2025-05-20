package com.benchmark.orm.domain.product.entity;

import com.benchmark.orm.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 인덱스가 적용된 상품 엔티티
 * 기존 Product 엔티티와 동일한 구조이지만, 성능 비교를 위해 다양한 인덱스가 적용되어 있음
 */
@Entity
@Table(name = "product_index",
        indexes = {
                @Index(name = "idx_product_index_name", columnList = "name"),         // 상품명에 대한 인덱스
                @Index(name = "idx_product_index_price", columnList = "price"),       // 가격에 대한 인덱스
                @Index(name = "idx_product_index_brand", columnList = "brand_id"),    // 브랜드 ID에 대한 인덱스
                @Index(name = "idx_product_index_category", columnList = "category_id") // 카테고리 ID에 대한 인덱스
        })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductIndex extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;    // 상품명
    private int price;      // 가격

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;    // 브랜드 정보

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;  // 카테고리 정보

    @Builder.Default
    @OneToMany(mappedBy = "productIndex", cascade = CascadeType.ALL)
    private List<ProductIndexImage> images = new ArrayList<>();  // 상품 이미지 목록

    /**
     * 상품 정보 업데이트
     *
     * @param name 변경할 상품명
     * @param price 변경할 가격
     * @return 업데이트된 상품 엔티티
     */
    public ProductIndex updateInfo(String name, int price) {
        this.name = name;
        this.price = price;
        return this;
    }

    /**
     * 브랜드 변경
     *
     * @param brand 변경할 브랜드
     * @return 현재 상품 엔티티
     */
    public ProductIndex changeBrand(Brand brand) {
        this.brand = brand;
        return this;
    }

    /**
     * 카테고리 변경
     *
     * @param category 변경할 카테고리
     * @return 현재 상품 엔티티
     */
    public ProductIndex changeCategory(Category category) {
        this.category = category;
        return this;
    }

    /**
     * 상품 이미지 추가
     *
     * @param image 추가할 이미지
     * @return 현재 상품 엔티티
     */
    public ProductIndex addImage(ProductIndexImage image) {
        this.images.add(image);
        image.assignProductIndex(this); // 양방향 관계 설정
        return this;
    }

    /**
     * 상품 이미지 제거
     *
     * @param image 제거할 이미지
     * @return 현재 상품 엔티티
     */
    public ProductIndex removeImage(ProductIndexImage image) {
        this.images.remove(image);
        image.assignProductIndex(null); // 양방향 관계 해제
        return this;
    }

    /**
     * 썸네일 이미지 찾기
     *
     * @return 썸네일로 설정된 이미지, 없으면 null
     */
    public ProductIndexImage findThumbnailImage() {
        return this.images.stream()
                .filter(ProductIndexImage::isThumbnail)
                .findFirst()
                .orElse(null);
    }
}