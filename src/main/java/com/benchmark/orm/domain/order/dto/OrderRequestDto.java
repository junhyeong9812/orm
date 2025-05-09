package com.benchmark.orm.domain.order.dto;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Long userId;
    private Long productId;

    /**
     * DTO를 엔티티로 변환
     * @param user 사용자 엔티티
     * @param product 상품 엔티티
     * @return Order 엔티티
     */
    public Order toEntity(User user, Product product) {
        Order order = new Order();

        try {
            if (id != null) {
                java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(order, id);
            }

            java.lang.reflect.Field orderDateField = Order.class.getDeclaredField("orderDate");
            orderDateField.setAccessible(true);
            orderDateField.set(order, orderDate != null ? orderDate : LocalDateTime.now());

            java.lang.reflect.Field userField = Order.class.getDeclaredField("user");
            userField.setAccessible(true);
            userField.set(order, user);

            java.lang.reflect.Field productField = Order.class.getDeclaredField("product");
            productField.setAccessible(true);
            productField.set(order, product);
        } catch (Exception e) {
            // Reflection 실패 시 무시
        }

        return order;
    }
}