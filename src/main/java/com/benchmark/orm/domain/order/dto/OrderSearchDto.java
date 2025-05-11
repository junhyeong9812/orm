package com.benchmark.orm.domain.order.dto;

import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주문 검색 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchDto {
    /**
     * 사용자 ID
     */
    private Long userId;

    /**
     * 주문 상태
     */
    private OrderStatus status;

    /**
     * 주문 시작일
     */
    private LocalDateTime startDate;

    /**
     * 주문 종료일
     */
    private LocalDateTime endDate;

    /**
     * 정렬 기준 컬럼
     */
    private String sortBy;

    /**
     * 정렬 방향 (asc/desc)
     */
    private String sortDirection;
}