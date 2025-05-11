package com.benchmark.orm.domain.product.entity;

import com.benchmark.orm.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();

    /**
     * 상품 정보 업데이트
     *
     * @param name 변경할 상품명
     * @param price 변경할 가격
     * @return 업데이트된 상품 엔티티
     */
    public Product updateInfo(String name, int price) {
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
    public Product changeBrand(Brand brand) {
        this.brand = brand;
        return this;
    }

    /**
     * 카테고리 변경
     *
     * @param category 변경할 카테고리
     * @return 현재 상품 엔티티
     */
    public Product changeCategory(Category category) {
        this.category = category;
        return this;
    }

    /**
     * 상품 이미지 추가
     *
     * @param image 추가할 이미지
     * @return 현재 상품 엔티티
     */
    public Product addImage(ProductImage image) {
        this.images.add(image);
        image.assignProduct(this); // 양방향 관계 설정
        return this;
    }

    /**
     * 상품 이미지 제거
     *
     * @param image 제거할 이미지
     * @return 현재 상품 엔티티
     */
    public Product removeImage(ProductImage image) {
        this.images.remove(image);
        image.assignProduct(null); // 양방향 관계 해제
        return this;
    }

    /**
     * 썸네일 이미지 찾기
     *
     * @return 썸네일로 설정된 이미지, 없으면 null
     */
    public ProductImage findThumbnailImage() {
        return this.images.stream()
                .filter(ProductImage::isThumbnail)
                .findFirst()
                .orElse(null);
    }
}