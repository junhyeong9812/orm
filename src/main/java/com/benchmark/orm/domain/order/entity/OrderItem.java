package com.benchmark.orm.domain.order.entity;

import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private int orderPrice; // 주문 당시 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 수량 변경
     *
     * @param quantity 변경할 수량
     * @return 현재 주문 상품 엔티티
     */
    public OrderItem changeQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }
        this.quantity = quantity;
        return this;
    }

    /**
     * 주문 가격 변경
     *
     * @param orderPrice 변경할 주문 가격
     * @return 현재 주문 상품 엔티티
     */
    public OrderItem changeOrderPrice(int orderPrice) {
        if (orderPrice < 0) {
            throw new IllegalArgumentException("주문 가격은 0원 이상이어야 합니다.");
        }
        this.orderPrice = orderPrice;
        return this;
    }

    /**
     * 주문 할당
     *
     * @param order 할당할 주문
     * @return 현재 주문 상품 엔티티
     */
    public OrderItem assignOrder(Order order) {
        this.order = order;
        return this;
    }

    /**
     * 상품 할당
     *
     * @param product 할당할 상품
     * @return 현재 주문 상품 엔티티
     */
    public OrderItem assignProduct(Product product) {
        this.product = product;
        if (product != null && this.orderPrice <= 0) {
            this.orderPrice = product.getPrice(); // 상품 가격으로 주문 가격 설정
        }
        return this;
    }

    /**
     * 총액 계산
     *
     * @return 주문 상품의 총액 (가격 * 수량)
     */
    public int calculateTotalPrice() {
        return this.orderPrice * this.quantity;
    }

    /**
     * 정적 팩토리 메서드 - 상품과 수량으로 주문 상품 생성
     *
     * @param product 상품
     * @param quantity 수량
     * @return 생성된 주문 상품 엔티티
     */
    public static OrderItem createOrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 필수입니다.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }

        return OrderItem.builder()
                .product(product)
                .orderPrice(product.getPrice())
                .quantity(quantity)
                .build();
    }
}