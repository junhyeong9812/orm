package com.benchmark.orm.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 검색 조건 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDto {
    private String keyword;
    private Integer minPrice;
    private Integer maxPrice;
    private Long brandId;
    private Long categoryId;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}