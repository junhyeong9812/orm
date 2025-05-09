package com.benchmark.orm.domain.product.dto;

import com.benchmark.orm.domain.product.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private BrandDto brand;
    private CategoryDto category;
    private List<ProductImageDto> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 엔티티로부터 DTO 생성
     * @param product 상품 엔티티
     * @return 상품 응답 DTO
     */
    public static ProductResponseDto fromEntity(Product product) {
        if (product == null) {
            return null;
        }

        List<ProductImageDto> imageDtos = new ArrayList<>();

        if (product.getImages() != null) {
            imageDtos = product.getImages().stream()
                    .map(ProductImageDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .brand(BrandDto.fromEntity(product.getBrand()))
                .category(CategoryDto.fromEntity(product.getCategory()))
                .images(imageDtos)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * 브랜드 DTO 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandDto {
        private Long id;
        private String name;

        /**
         * 엔티티로부터 DTO 생성
         * @param brand 브랜드 엔티티
         * @return 브랜드 DTO
         */
        public static BrandDto fromEntity(Brand brand) {
            if (brand == null) {
                return null;
            }

            return BrandDto.builder()
                    .id(brand.getId())
                    .name(brand.getName())
                    .build();
        }
    }

    /**
     * 카테고리 DTO 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDto {
        private Long id;
        private String name;
        private Long parentId;

        /**
         * 엔티티로부터 DTO 생성
         * @param category 카테고리 엔티티
         * @return 카테고리 DTO
         */
        public static CategoryDto fromEntity(Category category) {
            if (category == null) {
                return null;
            }

            Long parentId = null;
            if (category.getParent() != null) {
                parentId = category.getParent().getId();
            }

            return CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .parentId(parentId)
                    .build();
        }
    }

    /**
     * 상품 이미지 DTO 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageDto {
        private Long id;
        private String url;
        private boolean isThumbnail;

        /**
         * 엔티티로부터 DTO 생성
         * @param image 상품 이미지 엔티티
         * @return 상품 이미지 DTO
         */
        public static ProductImageDto fromEntity(ProductImage image) {
            if (image == null) {
                return null;
            }

            return ProductImageDto.builder()
                    .id(image.getId())
                    .url(image.getUrl())
                    .isThumbnail(image.isThumbnail())
                    .build();
        }
    }
}