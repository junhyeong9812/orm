package com.benchmark.orm.domain.order.dto;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.entity.OrderItem;
import com.benchmark.orm.domain.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private OrderStatus status;
    private UserResponseDto user;

    @Builder.Default
    private List<OrderItemResponseDto> orderItems = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 주문 총액
     */
    private Integer totalAmount;

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
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalAmount(order.calculateTotalPrice())
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
     * 엔티티로부터 DTO 생성 (주문 상품 정보 포함)
     * @param order 주문 엔티티
     * @return 주문 응답 DTO (주문 상품 정보 포함)
     */
    public static OrderResponseDto fromEntityWithOrderItems(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = fromEntity(order);

        // 주문 상품 정보 설정
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            dto.orderItems = order.getOrderItems().stream()
                    .map(OrderItemResponseDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return dto;
    }

    /**
     * 엔티티로부터 DTO 생성 (사용자 및 주문 상품 정보 포함)
     * @param order 주문 엔티티
     * @return 주문 응답 DTO (사용자 및 주문 상품 정보 포함)
     */
    public static OrderResponseDto fromEntityWithUserAndOrderItems(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = fromEntity(order);

        // 사용자 정보 설정
        if (order.getUser() != null) {
            dto.user = UserResponseDto.fromEntity(order.getUser());
        }

        // 주문 상품 정보 설정
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            dto.orderItems = order.getOrderItems().stream()
                    .map(OrderItemResponseDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return dto;
    }

    /**
     * 주문 상품 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponseDto {
        private Long id;
        private int quantity;
        private int orderPrice;
        private Long productId;
        private String productName;
        private int productPrice;
        private int itemTotalPrice;

        /**
         * 엔티티로부터 DTO 생성
         * @param orderItem 주문 상품 엔티티
         * @return 주문 상품 응답 DTO
         */
        public static OrderItemResponseDto fromEntity(OrderItem orderItem) {
            if (orderItem == null) {
                return null;
            }

            OrderItemResponseDtoBuilder builder = OrderItemResponseDto.builder()
                    .id(orderItem.getId())
                    .quantity(orderItem.getQuantity())
                    .orderPrice(orderItem.getOrderPrice())
                    .itemTotalPrice(orderItem.calculateTotalPrice());

            if (orderItem.getProduct() != null) {
                builder.productId(orderItem.getProduct().getId())
                        .productName(orderItem.getProduct().getName())
                        .productPrice(orderItem.getProduct().getPrice());
            }

            return builder.build();
        }
    }
}