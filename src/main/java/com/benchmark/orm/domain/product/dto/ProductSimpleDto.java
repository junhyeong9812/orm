package com.benchmark.orm.domain.product.dto;

import com.benchmark.orm.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 간단한 상품 DTO (성능 비교용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSimpleDto {
    private Long id;
    private String name;
    private int price;
    private String brandName;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity to DTO
    public static ProductSimpleDto from(Product product) {
        return ProductSimpleDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}