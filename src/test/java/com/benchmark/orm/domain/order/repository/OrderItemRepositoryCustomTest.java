package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.entity.OrderItem;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.repository.UserRepositoryTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OrderItemRepositoryCustom 테스트
 * <p>
 * QueryDSL을 사용한 OrderItemRepositoryCustom 구현체 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class OrderItemRepositoryCustomTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 테스트용 사용자 생성 헬퍼 메서드
    private User createTestUser() {
        return User.builder()
                .username("테스트유저")
                .email("test@example.com")
                .build();
    }

    // 테스트용 상품 생성 헬퍼 메서드
    private Product createTestProduct(String name, int price) {
        return Product.builder()
                .name(name)
                .price(price)
                .build();
    }

    // 테스트용 주문 생성 및 저장 헬퍼 메서드
    private Order createAndSaveTestOrder(User user) {
        Order order = Order.builder()
                .user(user)
                .orderDate(java.time.LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        return orderRepository.save(order);
    }

    @Test
    @DisplayName("주문상품 상세 정보와 함께 조회 테스트 (커스텀 메서드)")
    public void findOrderItemWithDetailsTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);
        Product product = createTestProduct("테스트상품", 10000);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        // when
        Optional<OrderItem> result = orderItemRepository.findOrderItemWithDetails(savedOrderItem.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getProduct()).isNotNull();
        assertThat(result.get().getProduct().getName()).isEqualTo("테스트상품");
        assertThat(result.get().getOrder()).isNotNull();
        assertThat(result.get().getOrder().getId()).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("주문 ID로 상품 정보를 포함한 주문상품 목록 조회 테스트 (커스텀 메서드)")
    public void findByOrderIdWithProductTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        OrderItem orderItem1 = OrderItem.builder()
                .order(order)
                .product(product1)
                .quantity(2)
                .orderPrice(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(order)
                .product(product2)
                .quantity(1)
                .orderPrice(product2.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2));

        // when
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithProduct(order.getId());

        // then
        assertThat(orderItems).hasSize(2);

        // 상품 정보가 제대로 로드되었는지 확인
        assertThat(orderItems).allMatch(item -> item.getProduct() != null);
        assertThat(orderItems).extracting("product.name")
                .containsExactlyInAnyOrder("상품1", "상품2");
    }

    @Test
    @DisplayName("상품 ID 목록으로 주문상품 조회 테스트 (커스텀 메서드)")
    public void findByProductIdsTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);
        Product product3 = createTestProduct("상품3", 30000);

        OrderItem orderItem1 = OrderItem.builder()
                .order(order)
                .product(product1)
                .quantity(2)
                .orderPrice(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(order)
                .product(product2)
                .quantity(1)
                .orderPrice(product2.getPrice())
                .build();

        OrderItem orderItem3 = OrderItem.builder()
                .order(order)
                .product(product3)
                .quantity(3)
                .orderPrice(product3.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2, orderItem3));

        // when - product1, product2 ID로 조회
        List<OrderItem> result = orderItemRepository.findByProductIds(
                List.of(product1.getId(), product2.getId()));

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("product.id")
                .containsExactlyInAnyOrder(product1.getId(), product2.getId());
    }

    @Test
    @DisplayName("특정 가격 이상의 상품을 포함한 주문상품 페이징 조회 테스트 (커스텀 메서드)")
    public void findByPriceGreaterThanTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);

        // 다양한 가격의 주문상품 추가
        for (int i = 1; i <= 10; i++) {
            Product product = createTestProduct("상품" + i, i * 5000); // 5000, 10000, 15000, ..., 50000

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(1)
                    .orderPrice(product.getPrice())
                    .build();

            orderItemRepository.save(orderItem);
        }

        // when - 20000원 이상 가격, 페이지당 2개씩
        Pageable pageable = PageRequest.of(0, 2);
        Page<OrderItem> result = orderItemRepository.findByPriceGreaterThan(20000, pageable);

        // then
        assertThat(result.getContent()).hasSize(2); // 첫 페이지에 2개
        assertThat(result.getTotalElements()).isEqualTo(6); // 20000원 이상인 항목은 총 6개 (25000~50000)
        assertThat(result.getTotalPages()).isEqualTo(3); // 페이지당 2개씩, 총 3페이지
        assertThat(result.getContent()).allMatch(item -> item.getOrderPrice() > 20000);
    }

    @Test
    @DisplayName("특정 상품의 총 주문 수량 계산 테스트 (커스텀 메서드)")
    public void calculateTotalQuantityForProductTest() {
        // given
        User user = createTestUser();
        Product product = createTestProduct("테스트상품", 10000);

        // 동일 상품을 여러 주문에 다양한 수량으로 추가
        Order order1 = createAndSaveTestOrder(user);
        OrderItem orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        Order order2 = createAndSaveTestOrder(user);
        OrderItem orderItem2 = OrderItem.builder()
                .order(order2)
                .product(product)
                .quantity(3)
                .orderPrice(product.getPrice())
                .build();

        Order order3 = createAndSaveTestOrder(user);
        OrderItem orderItem3 = OrderItem.builder()
                .order(order3)
                .product(product)
                .quantity(5)
                .orderPrice(product.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2, orderItem3));

        // when
        int totalQuantity = orderItemRepository.calculateTotalQuantityForProduct(product.getId());

        // then
        assertThat(totalQuantity).isEqualTo(10); // 2 + 3 + 5 = 10
    }

    @Test
    @DisplayName("가장 많이 주문된 상품 목록 조회 테스트 (커스텀 메서드)")
    public void findMostOrderedProductsTest() {
        // given
        User user = createTestUser();

        Product product1 = createTestProduct("인기상품1", 10000);
        Product product2 = createTestProduct("인기상품2", 20000);
        Product product3 = createTestProduct("인기상품3", 30000);

        // product1은 총 10개 주문 (7+3)
        Order order1 = createAndSaveTestOrder(user);
        OrderItem orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product1)
                .quantity(7)
                .orderPrice(product1.getPrice())
                .build();

        Order order2 = createAndSaveTestOrder(user);
        OrderItem orderItem2 = OrderItem.builder()
                .order(order2)
                .product(product1)
                .quantity(3)
                .orderPrice(product1.getPrice())
                .build();

        // product2는 총 5개 주문
        Order order3 = createAndSaveTestOrder(user);
        OrderItem orderItem3 = OrderItem.builder()
                .order(order3)
                .product(product2)
                .quantity(5)
                .orderPrice(product2.getPrice())
                .build();

        // product3는 총 2개 주문
        Order order4 = createAndSaveTestOrder(user);
        OrderItem orderItem4 = OrderItem.builder()
                .order(order4)
                .product(product3)
                .quantity(2)
                .orderPrice(product3.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2, orderItem3, orderItem4));

        // when - 가장 많이 주문된 상품 목록 조회 (상위 2개)
        List<OrderItem> mostOrderedProducts = orderItemRepository.findMostOrderedProducts(2);

        // then
        assertThat(mostOrderedProducts).hasSize(2);

        // 첫 번째는 product1과 관련된 주문상품 (총 10개 주문)
        assertThat(mostOrderedProducts.get(0).getProduct().getId()).isEqualTo(product1.getId());

        // 두 번째는 product2와 관련된 주문상품 (총 5개 주문)
        assertThat(mostOrderedProducts.get(1).getProduct().getId()).isEqualTo(product2.getId());
    }
}