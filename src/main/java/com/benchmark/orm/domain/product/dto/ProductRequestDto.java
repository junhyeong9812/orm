package com.benchmark.orm.domain.product.dto;

import com.benchmark.orm.domain.product.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private Long id;
    private String name;
    private int price;
    private BrandDto brand;
    private CategoryDto category;
    private List<ProductImageDto> images;

    /**
     * DTO를 엔티티로 변환
     * @return Product 엔티티
     */
    public Product toEntity() {
        Product product = new Product();

        // Product 필드 설정
        try {
            if (id != null) {
                java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(product, id);
            }

            java.lang.reflect.Field nameField = Product.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(product, name);

            java.lang.reflect.Field priceField = Product.class.getDeclaredField("price");
            priceField.setAccessible(true);
            priceField.set(product, price);

            // 브랜드와 카테고리 설정
            if (brand != null) {
                java.lang.reflect.Field brandField = Product.class.getDeclaredField("brand");
                brandField.setAccessible(true);
                brandField.set(product, brand.toEntity());
            }

            if (category != null) {
                java.lang.reflect.Field categoryField = Product.class.getDeclaredField("category");
                categoryField.setAccessible(true);
                categoryField.set(product, category.toEntity());
            }
        } catch (Exception e) {
            // Reflection 실패 시 무시
        }

        return product;
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
         * DTO를 엔티티로 변환
         * @return Brand 엔티티
         */
        public Brand toEntity() {
            Brand brand = new Brand();

            try {
                if (id != null) {
                    java.lang.reflect.Field idField = Brand.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(brand, id);
                }

                java.lang.reflect.Field nameField = Brand.class.getDeclaredField("name");
                nameField.setAccessible(true);
                nameField.set(brand, name);
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }

            return brand;
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
         * DTO를 엔티티로 변환
         * @return Category 엔티티
         */
        public Category toEntity() {
            Category category = new Category();

            try {
                if (id != null) {
                    java.lang.reflect.Field idField = Category.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(category, id);
                }

                java.lang.reflect.Field nameField = Category.class.getDeclaredField("name");
                nameField.setAccessible(true);
                nameField.set(category, name);

                // 부모 카테고리는 별도 처리 필요
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }

            return category;
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
         * DTO를 엔티티로 변환
         * @param product 상품 엔티티 (연관관계 설정용)
         * @return ProductImage 엔티티
         */
        public ProductImage toEntity(Product product) {
            ProductImage image = new ProductImage();

            try {
                if (id != null) {
                    java.lang.reflect.Field idField = ProductImage.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(image, id);
                }

                java.lang.reflect.Field urlField = ProductImage.class.getDeclaredField("url");
                urlField.setAccessible(true);
                urlField.set(image, url);

                java.lang.reflect.Field isThumbnailField = ProductImage.class.getDeclaredField("isThumbnail");
                isThumbnailField.setAccessible(true);
                isThumbnailField.set(image, isThumbnail);

                java.lang.reflect.Field productField = ProductImage.class.getDeclaredField("product");
                productField.setAccessible(true);
                productField.set(image, product);
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }

            return image;
        }
    }
}