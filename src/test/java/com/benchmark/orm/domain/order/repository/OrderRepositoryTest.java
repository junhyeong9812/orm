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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OrderRepository 테스트
 * <p>
 * JPA Repository를 사용한 주문 관련 데이터 접근 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    @Test
    @DisplayName("주문 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given
        User user = createTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        // when
        Order savedOrder = orderRepository.save(order);

        // then
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("주문 정보 수정 테스트")
    public void updateTest() {
        // given
        User user = createTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();
        Order savedOrder = orderRepository.save(order);

        // when
        savedOrder.changeStatus(OrderStatus.PROCESSING);
        Order updatedOrder = orderRepository.save(savedOrder);

        // then
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }

    @Test
    @DisplayName("주문 삭제 테스트")
    public void deleteTest() {
        // given
        User user = createTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();
        Order savedOrder = orderRepository.save(order);

        // when
        orderRepository.delete(savedOrder);

        // then
        Optional<Order> afterDelete = orderRepository.findById(savedOrder.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("사용자 ID로 주문 조회 테스트")
    public void findByUserIdTest() {
        // given
        User user = createTestUser();

        for (int i = 0; i < 3; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // when
        List<Order> orders = orderRepository.findByUserId(user.getId());

        // then
        assertThat(orders).hasSize(3);
        assertThat(orders).allMatch(o -> o.getUser().getId().equals(user.getId()));
    }

    @Test
    @DisplayName("주문 상태로 주문 조회 테스트")
    public void findByStatusTest() {
        // given
        User user = createTestUser();

        // PENDING 상태 주문 2개 생성
        for (int i = 0; i < 2; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // SHIPPED 상태 주문 1개 생성
        Order shippedOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.SHIPPED)
                .build();

        orderRepository.save(shippedOrder);

        // when
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        List<Order> shippedOrders = orderRepository.findByStatus(OrderStatus.SHIPPED);

        // then
        assertThat(pendingOrders).hasSize(2);
        assertThat(pendingOrders).allMatch(o -> o.getStatus() == OrderStatus.PENDING);

        assertThat(shippedOrders).hasSize(1);
        assertThat(shippedOrders).allMatch(o -> o.getStatus() == OrderStatus.SHIPPED);
    }

    @Test
    @DisplayName("사용자 ID와 주문 상태로 주문 조회 테스트")
    public void findByUserIdAndStatusTest() {
        // given
        User user1 = createTestUser();
        User user2 = User.builder()
                .username("테스트유저2")
                .email("test2@example.com")
                .build();

        // user1의 PENDING 상태 주문 2개 생성
        for (int i = 0; i < 2; i++) {
            Order order = Order.builder()
                    .user(user1)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // user1의 SHIPPED 상태 주문 1개 생성
        Order user1ShippedOrder = Order.builder()
                .user(user1)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.SHIPPED)
                .build();

        orderRepository.save(user1ShippedOrder);

        // user2의 PENDING 상태 주문 1개 생성
        Order user2Order = Order.builder()
                .user(user2)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(user2Order);

        // when
        List<Order> user1PendingOrders = orderRepository.findByUserIdAndStatus(user1.getId(), OrderStatus.PENDING);

        // then
        assertThat(user1PendingOrders).hasSize(2);
        assertThat(user1PendingOrders).allMatch(o ->
                o.getUser().getId().equals(user1.getId()) && o.getStatus() == OrderStatus.PENDING);
    }

    @Test
    @DisplayName("주문 날짜 범위로 주문 조회 테스트")
    public void findByOrderDateBetweenTest() {
        // given
        User user = createTestUser();

        // 어제 주문
        Order yesterdayOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().minusDays(1))
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(yesterdayOrder);

        // 오늘 주문
        Order todayOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(todayOrder);

        // 내일 주문 (미래 데이터)
        Order tomorrowOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().plusDays(1))
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(tomorrowOrder);

        // when
        LocalDateTime startDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

        // then
        assertThat(orders).hasSize(2);
        assertThat(orders).allMatch(o ->
                !o.getOrderDate().isBefore(startDate) && !o.getOrderDate().isAfter(endDate));
    }

    @Test
    @DisplayName("JPQL을 사용한 사용자 ID로 주문 조회 테스트")
    public void findByUserIdJpqlTest() {
        // given
        User user = createTestUser();

        for (int i = 0; i < 3; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // when - JPQL 사용
        List<Order> orders = orderRepository.findByUserIdJpql(user.getId());

        // then
        assertThat(orders).hasSize(3);
        assertThat(orders).allMatch(o -> o.getUser().getId().equals(user.getId()));
    }

    @Test
    @DisplayName("JPQL을 사용한 주문 날짜 범위로 주문 조회 테스트")
    public void findByOrderDateBetweenJpqlTest() {
        // given
        User user = createTestUser();

        // 어제 주문
        Order yesterdayOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().minusDays(1))
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(yesterdayOrder);

        // 오늘 주문
        Order todayOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(todayOrder);

        // when - JPQL 사용
        LocalDateTime startDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Order> orders = orderRepository.findByOrderDateBetweenJpql(startDate, endDate);

        // then
        assertThat(orders).hasSize(2);
        assertThat(orders).allMatch(o ->
                !o.getOrderDate().isBefore(startDate) && !o.getOrderDate().isAfter(endDate));
    }

    @Test
    @DisplayName("JPQL을 사용한 사용자 정보와 함께 주문 조회 테스트")
    public void findOrderWithUserJpqlTest() {
        // given
        User user = createTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // when - JPQL 사용
        Optional<Order> result = orderRepository.findOrderWithUserJpql(savedOrder.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUser()).isNotNull();
        assertThat(result.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("JPQL을 사용한 주문 상품 정보와 함께 주문 조회 테스트")
    public void findOrderWithOrderItemsJpqlTest() {
        // given
        User user = createTestUser();
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 주문 생성
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // 주문 상품 생성
        OrderItem orderItem1 = OrderItem.builder()
                .order(savedOrder)
                .product(product1)
                .quantity(2)
                .orderPrice(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(savedOrder)
                .product(product2)
                .quantity(1)
                .orderPrice(product2.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2));

        // when - JPQL 사용
        Optional<Order> result = orderRepository.findOrderWithOrderItemsJpql(savedOrder.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderItems()).hasSize(2);
    }

    @Test
    @DisplayName("JPQL을 사용한 사용자 및 주문 상품 정보와 함께 주문 조회 테스트")
    public void findOrderWithUserAndOrderItemsJpqlTest() {
        // given
        User user = createTestUser();
        Product product = createTestProduct("테스트상품", 10000);

        // 주문 생성
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.builder()
                .order(savedOrder)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        orderItemRepository.save(orderItem);

        // when - JPQL 사용
        Optional<Order> result = orderRepository.findOrderWithUserAndOrderItemsJpql(savedOrder.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUser()).isNotNull();
        assertThat(result.get().getOrderItems()).hasSize(1);
    }

    @Test
    @DisplayName("JPQL을 사용한 주문 상태별 주문 수 카운트 테스트")
    public void countByStatusJpqlTest() {
        // given
        User user = createTestUser();

        // PENDING 상태 주문 3개 생성
        for (int i = 0; i < 3; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // SHIPPED 상태 주문 2개 생성
        for (int i = 0; i < 2; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.SHIPPED)
                    .build();

            orderRepository.save(order);
        }

        // when - JPQL 사용
        long pendingCount = orderRepository.countByStatusJpql(OrderStatus.PENDING);
        long shippedCount = orderRepository.countByStatusJpql(OrderStatus.SHIPPED);

        // then
        assertThat(pendingCount).isEqualTo(3);
        assertThat(shippedCount).isEqualTo(2);
    }

    @Test
    @DisplayName("JPQL을 사용한 사용자별 총 주문 금액 계산 테스트")
    public void calculateTotalOrderAmountByUserIdTest() {
        // given
        User user = createTestUser();
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 주문 생성
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // 주문 상품 생성
        OrderItem orderItem1 = OrderItem.builder()
                .order(savedOrder)
                .product(product1)
                .quantity(2)  // 10000 * 2 = 20000
                .orderPrice(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(savedOrder)
                .product(product2)
                .quantity(1)  // 20000 * 1 = 20000
                .orderPrice(product2.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2));

        // when - JPQL 사용
        Integer totalAmount = orderRepository.calculateTotalOrderAmountByUserId(user.getId());

        // then
        assertThat(totalAmount).isEqualTo(40000);  // 20000 + 20000 = 40000
    }

    @Test
    @DisplayName("JPQL을 사용한 복합 조건으로 주문 검색 테스트")
    public void searchOrdersJpqlTest() {
        // given
        User user = createTestUser();

        // 다양한 상태와 날짜의 주문 생성
        Order pendingOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().minusDays(1))
                .status(OrderStatus.PENDING)
                .build();

        Order processingOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().minusDays(2))
                .status(OrderStatus.PROCESSING)
                .build();

        Order shippedOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().minusDays(3))
                .status(OrderStatus.SHIPPED)
                .build();

        orderRepository.saveAll(List.of(pendingOrder, processingOrder, shippedOrder));

        // when - JPQL 사용 (PENDING 상태 주문)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("orderDate").descending());
        Page<Order> result = orderRepository.searchOrdersJpql(
                user.getId(), OrderStatus.PENDING, null, null, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }
}