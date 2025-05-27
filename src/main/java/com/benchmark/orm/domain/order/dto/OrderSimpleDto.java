package com.benchmark.orm.domain.order.dto;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 간단한 주문 DTO (성능 비교용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSimpleDto {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Long userId;
    private String username;
    private Integer totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity to DTO
    public static OrderSimpleDto from(Order order) {
        return OrderSimpleDto.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .username(order.getUser() != null ? order.getUser().getUsername() : null)
                .totalAmount(order.calculateTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}