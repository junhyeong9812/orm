package com.benchmark.orm.domain.order.dto;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.product.dto.ProductResponseDto;
import com.benchmark.orm.domain.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주문 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private LocalDateTime orderDate;
    private UserResponseDto user;
    private ProductResponseDto product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 엔티티로부터 DTO 생성 (기본)
     * @param order 주문 엔티티
     * @return 주문 응답 DTO (기본 정보만 포함)
     */
    public static OrderResponseDto fromEntity(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponseDto.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    /**
     * 엔티티로부터 DTO 생성 (사용자 정보 포함)
     * @param order 주문 엔티티
     * @return 주문 응답 DTO (사용자 정보 포함)
     */
    public static OrderResponseDto fromEntityWithUser(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = fromEntity(order);

        // 사용자 정보 설정
        if (order.getUser() != null) {
            dto.user = UserResponseDto.fromEntity(order.getUser());
        }

        return dto;
    }

    /**
     * 엔티티로부터 DTO 생성 (상품 정보 포함)
     * @param order 주문 엔티티
     * @return 주문 응답 DTO (상품 정보 포함)
     */
    public static OrderResponseDto fromEntityWithProduct(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = fromEntity(order);

        // 상품 정보 설정
        if (order.getProduct() != null) {
            dto.product = ProductResponseDto.fromEntity(order.getProduct());
        }

        return dto;
    }

    /**
     * 엔티티로부터 DTO 생성 (사용자 및 상품 정보 포함)
     * @param order 주문 엔티티
     * @return 주문 응답 DTO (사용자 및 상품 정보 포함)
     */
    public static OrderResponseDto fromEntityWithUserAndProduct(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = fromEntity(order);

        // 사용자 정보 설정
        if (order.getUser() != null) {
            dto.user = UserResponseDto.fromEntity(order.getUser());
        }

        // 상품 정보 설정
        if (order.getProduct() != null) {
            dto.product = ProductResponseDto.fromEntity(order.getProduct());
        }

        return dto;
    }
}