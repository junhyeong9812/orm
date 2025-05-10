package com.benchmark.orm.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchDto {
    private Long userId;
    private Long productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // 정렬 관련 필드
    private String sortBy; // 정렬 기준 필드
    private String sortDirection; // 정렬 방향 (asc/desc)
}