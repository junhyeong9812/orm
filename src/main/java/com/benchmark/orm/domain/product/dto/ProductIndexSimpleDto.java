package com.benchmark.orm.domain.product.dto;

import com.benchmark.orm.domain.product.entity.ProductIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 간단한 상품 인덱스 DTO (성능 비교용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductIndexSimpleDto {
    private Long id;
    private String name;
    private int price;
    private String brandName;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity to DTO
    public static ProductIndexSimpleDto from(ProductIndex productIndex) {
        return ProductIndexSimpleDto.builder()
                .id(productIndex.getId())
                .name(productIndex.getName())
                .price(productIndex.getPrice())
                .brandName(productIndex.getBrand() != null ? productIndex.getBrand().getName() : null)
                .categoryName(productIndex.getCategory() != null ? productIndex.getCategory().getName() : null)
                .createdAt(productIndex.getCreatedAt())
                .updatedAt(productIndex.getUpdatedAt())
                .build();
    }
}