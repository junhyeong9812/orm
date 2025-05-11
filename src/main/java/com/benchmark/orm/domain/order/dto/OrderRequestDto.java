package com.benchmark.orm.domain.order.dto;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.entity.OrderItem;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Long userId;

    /**
     * 주문 상품 요청 정보 목록
     */
    @Builder.Default
    private List<OrderItemRequestDto> orderItems = new ArrayList<>();

    /**
     * DTO를 엔티티로 변환
     * @param user 사용자 엔티티
     * @return Order 엔티티
     */
    public Order toEntity(User user) {
        return Order.builder()
                .id(id)
                .orderDate(orderDate != null ? orderDate : LocalDateTime.now())
                .status(status != null ? status : OrderStatus.PENDING)
                .user(user)
                .build();
    }

    /**
     * 주문 상품 요청 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequestDto {
        private Long id;
        private Long productId;
        private int quantity;
        private int orderPrice;

        /**
         * DTO를 엔티티로 변환
         * @param product 상품 엔티티
         * @return OrderItem 엔티티
         */
        public OrderItem toEntity(Product product) {
            return OrderItem.builder()
                    .id(id)
                    .product(product)
                    .quantity(quantity)
                    .orderPrice(orderPrice > 0 ? orderPrice : (product != null ? product.getPrice() : 0))
                    .build();
        }
    }
}