package com.benchmark.orm.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDto {
    private String keyword; // 상품명 검색
    private Integer minPrice;
    private Integer maxPrice;
    private Long brandId;
    private Long categoryId;

    // 정렬 관련 필드
    private String sortBy; // 정렬 기준 필드
    private String sortDirection; // 정렬 방향 (asc/desc)
}