package com.benchmark.orm.domain.order.entity;

import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 주문 상태 enum
     */
    public enum OrderStatus {
        PENDING,       // 대기중
        PROCESSING,    // 처리중
        SHIPPED,       // 배송중
        DELIVERED,     // 배송완료
        CANCELLED      // 취소됨
    }

    /**
     * 주문 날짜 변경
     *
     * @param orderDate 변경할 주문 날짜
     * @return 현재 주문 엔티티
     */
    public Order changeOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    /**
     * 주문 상태 변경
     *
     * @param status 변경할 주문 상태
     * @return 현재 주문 엔티티
     */
    public Order changeStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    /**
     * 주문자 변경
     *
     * @param user 변경할 주문자
     * @return 현재 주문 엔티티
     */
    public Order changeUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * 주문 상품 추가
     *
     * @param orderItem 추가할 주문 상품
     * @return 현재 주문 엔티티
     */
    public Order addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this); // 양방향 관계 설정
        return this;
    }

    /**
     * 주문 상품 제거
     *
     * @param orderItem 제거할 주문 상품
     * @return 현재 주문 엔티티
     */
    public Order removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.assignOrder(null); // 양방향 관계 해제
        return this;
    }

    /**
     * 상품을 주문에 추가
     *
     * @param product 추가할 상품
     * @param quantity 수량
     * @return 현재 주문 엔티티
     */
    public Order addProduct(Product product, int quantity) {
        OrderItem orderItem = OrderItem.createOrderItem(product, quantity);
        return addOrderItem(orderItem);
    }

    /**
     * 주문 총액 계산
     *
     * @return 주문 내 모든 상품의 총액
     */
    public int calculateTotalPrice() {
        return this.orderItems.stream()
                .mapToInt(OrderItem::calculateTotalPrice)
                .sum();
    }

    /**
     * 정적 팩토리 메서드 - 주문 생성
     *
     * @param user 주문자
     * @param orderItems 주문 상품 목록
     * @return 생성된 주문 엔티티
     */
    public static Order createOrder(User user, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        // 주문 상품 추가
        orderItems.forEach(order::addOrderItem);

        return order;
    }

    /**
     * 정적 팩토리 메서드 - 빠른 주문 생성
     *
     * @param user 주문자
     * @param product 상품
     * @param quantity 수량
     * @return 생성된 주문 엔티티
     */
    public static Order createQuickOrder(User user, Product product, int quantity) {
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        // 주문 상품 추가
        order.addProduct(product, quantity);

        return order;
    }
}