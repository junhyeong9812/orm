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
 * OrderItemRepository 테스트
 * <p>
 * JPA Repository를 사용한 주문 상품 관련 데이터 접근 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class OrderItemRepositoryTest {

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

    // 테스트용 주문 생성 헬퍼 메서드
    private Order createAndSaveTestOrder(User user) {
        Order order = Order.builder()
                .user(user)
                .orderDate(java.time.LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        return orderRepository.save(order);
    }

    @Test
    @DisplayName("주문상품 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
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

        // when
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        // then
        Optional<OrderItem> foundOrderItem = orderItemRepository.findById(savedOrderItem.getId());
        assertThat(foundOrderItem).isPresent();
        assertThat(foundOrderItem.get().getQuantity()).isEqualTo(2);
        assertThat(foundOrderItem.get().getOrderPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("주문상품 수정 테스트")
    public void updateTest() {
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
        savedOrderItem.changeQuantity(5);
        OrderItem updatedOrderItem = orderItemRepository.save(savedOrderItem);

        // then
        assertThat(updatedOrderItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("주문상품 삭제 테스트")
    public void deleteTest() {
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
        orderItemRepository.delete(savedOrderItem);

        // then
        Optional<OrderItem> afterDelete = orderItemRepository.findById(savedOrderItem.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("주문 ID로 주문상품 목록 조회 테스트")
    public void findByOrderIdTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 주문상품 추가
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
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        // then
        assertThat(orderItems).hasSize(2);
        assertThat(orderItems).extracting("id")
                .containsExactlyInAnyOrder(orderItem1.getId(), orderItem2.getId());
    }

    @Test
    @DisplayName("상품 ID로 주문상품 목록 조회 테스트")
    public void findByProductIdTest() {
        // given
        User user = createTestUser();
        Order order1 = createAndSaveTestOrder(user);
        Order order2 = createAndSaveTestOrder(user);
        Product product = createTestProduct("테스트상품", 10000);

        // 서로 다른 주문에 동일 상품 추가
        OrderItem orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(order2)
                .product(product)
                .quantity(3)
                .orderPrice(product.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2));

        // when
        List<OrderItem> orderItems = orderItemRepository.findByProductId(product.getId());

        // then
        assertThat(orderItems).hasSize(2);
        assertThat(orderItems).extracting("id")
                .containsExactlyInAnyOrder(orderItem1.getId(), orderItem2.getId());
    }

    @Test
    @DisplayName("주문 ID와 상품 ID로 주문상품 조회 테스트")
    public void findByOrderIdAndProductIdTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 동일 주문, 다른 상품으로 주문상품 추가
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
        List<OrderItem> result = orderItemRepository.findByOrderIdAndProductId(order.getId(), product1.getId());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(orderItem1.getId());
        assertThat(result.get(0).getProduct().getId()).isEqualTo(product1.getId());
    }

    @Test
    @DisplayName("특정 수량 이상의 주문상품 조회 테스트")
    public void findByQuantityGreaterThanEqualTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);

        // 다양한 수량의 주문상품 추가
        for (int i = 1; i <= 5; i++) {
            Product product = createTestProduct("상품" + i, 10000);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(i) // 1부터 5까지의 수량
                    .orderPrice(product.getPrice())
                    .build();

            orderItemRepository.save(orderItem);
        }

        // when
        List<OrderItem> result = orderItemRepository.findByQuantityGreaterThanEqual(3);

        // then
        assertThat(result).hasSize(3); // 수량 3, 4, 5인 상품만 조회됨
        assertThat(result).allMatch(item -> item.getQuantity() >= 3);
    }

    @Test
    @DisplayName("특정 가격 범위의 주문상품 조회 테스트")
    public void findByPriceBetweenTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);

        // 다양한 가격의 주문상품 추가
        for (int i = 1; i <= 5; i++) {
            Product product = createTestProduct("상품" + i, i * 10000); // 10000, 20000, 30000, 40000, 50000

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(1)
                    .orderPrice(product.getPrice())
                    .build();

            orderItemRepository.save(orderItem);
        }

        // when
        List<OrderItem> result = orderItemRepository.findByPriceBetween(20000, 40000);

        // then
        assertThat(result).hasSize(3); // 20000, 30000, 40000 가격의 상품만 조회됨
        assertThat(result).allMatch(item ->
                item.getOrderPrice() >= 20000 && item.getOrderPrice() <= 40000);
    }

    @Test
    @DisplayName("주문 ID로 주문상품의 총 금액 계산 테스트")
    public void calculateTotalPriceByOrderIdTest() {
        // given
        User user = createTestUser();
        Order order = createAndSaveTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 주문상품 추가
        OrderItem orderItem1 = OrderItem.builder()
                .order(order)
                .product(product1)
                .quantity(2)  // 10000 * 2 = 20000
                .orderPrice(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(order)
                .product(product2)
                .quantity(1)  // 20000 * 1 = 20000
                .orderPrice(product2.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2));

        // when
        int totalPrice = orderItemRepository.calculateTotalPriceByOrderId(order.getId());

        // then
        assertThat(totalPrice).isEqualTo(40000);  // 20000 + 20000 = 40000
    }

    @Test
    @DisplayName("특정 상품이 포함된 주문 ID 목록 조회 테스트")
    public void findOrderIdsByProductIdTest() {
        // given
        User user = createTestUser();
        Order order1 = createAndSaveTestOrder(user);
        Order order2 = createAndSaveTestOrder(user);
        Order order3 = createAndSaveTestOrder(user);

        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // order1, order2에는 product1 포함
        OrderItem orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product1)
                .quantity(2)
                .orderPrice(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(order2)
                .product(product1)
                .quantity(3)
                .orderPrice(product1.getPrice())
                .build();

        // order3에는 product2 포함
        OrderItem orderItem3 = OrderItem.builder()
                .order(order3)
                .product(product2)
                .quantity(1)
                .orderPrice(product2.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2, orderItem3));

        // when
        List<Long> orderIds = orderItemRepository.findOrderIdsByProductId(product1.getId());

        // then
        assertThat(orderIds).hasSize(2);
        assertThat(orderIds).containsExactlyInAnyOrder(order1.getId(), order2.getId());
    }
}