package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.dto.OrderSearchDto;
import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.entity.OrderItem;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.repository.ProductRepository;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.repository.UserRepository;
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
 * OrderRepositoryCustom 테스트
 * <p>
 * QueryDSL을 사용한 OrderRepositoryCustom 구현체 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class OrderRepositoryCustomTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // 테스트용 사용자 생성 헬퍼 메서드
    private User createTestUser() {
        User user = User.builder()
                .username("테스트유저")
                .email("test@example.com")
                .build();

        return userRepository.save(user);
    }

    // 테스트용 상품 생성 헬퍼 메서드
    private Product createTestProduct(String name, int price) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .build();

        return productRepository.save(product);
    }

    @Test
    @DisplayName("주문 날짜 범위로 주문 조회 테스트 (커스텀 메서드)")
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
    @DisplayName("페이징 적용하여 모든 주문 조회 테스트 (커스텀 메서드)")
    public void findAllWithPagingTest() {
        // given
        User user = createTestUser();

        for (int i = 1; i <= 20; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> result = orderRepository.findAllWithPaging(pageable);

        // then
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getTotalPages()).isEqualTo(4);
    }

    @Test
    @DisplayName("정렬 적용하여 모든 주문 조회 테스트 (커스텀 메서드)")
    public void findAllWithSortingTest() {
        // given
        User user = createTestUser();

        // 다양한 날짜의 주문 생성
        for (int i = 1; i <= 5; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusDays(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // when - 주문 날짜 기준 오름차순 정렬
        Sort sort = Sort.by(Sort.Direction.ASC, "orderDate");
        List<Order> result = orderRepository.findAllWithSorting(sort);

        // then
        assertThat(result).hasSize(5);

        // 오름차순 확인
        for (int i = 0; i < result.size() - 1; i++) {
            assertThat(result.get(i).getOrderDate())
                    .isBeforeOrEqualTo(result.get(i + 1).getOrderDate());
        }
    }

    @Test
    @DisplayName("사용자 정보와 함께 주문 조회 테스트 (커스텀 메서드)")
    public void findOrderWithUserTest() {
        // given
        User user = createTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // when
        Optional<Order> result = orderRepository.findOrderWithUser(savedOrder.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUser()).isNotNull();
        assertThat(result.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("주문 상품 정보와 함께 주문 조회 테스트 (커스텀 메서드)")
    public void findOrderWithOrderItemsTest() {
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

        // when
        Optional<Order> result = orderRepository.findOrderWithOrderItems(savedOrder.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderItems()).hasSize(2);
    }

    @Test
    @DisplayName("사용자 및 주문 상품 정보와 함께 주문 조회 테스트 (커스텀 메서드)")
    public void findOrderWithUserAndOrderItemsTest() {
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

        // when
        Optional<Order> result = orderRepository.findOrderWithUserAndOrderItems(savedOrder.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUser()).isNotNull();
        assertThat(result.get().getOrderItems()).hasSize(1);
    }

    @Test
    @DisplayName("사용자 ID로 주문을 페이징하여 조회 테스트 (커스텀 메서드)")
    public void findByUserIdWithPagingTest() {
        // given
        User user = createTestUser();

        for (int i = 1; i <= 10; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> result = orderRepository.findByUserIdWithPaging(user.getId(), pageable);

        // then
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent()).allMatch(o -> o.getUser().getId().equals(user.getId()));
    }

    @Test
    @DisplayName("주문 상태로 주문을 페이징하여 조회 테스트 (커스텀 메서드)")
    public void findByStatusWithPagingTest() {
        // given
        User user = createTestUser();

        // PENDING 상태 주문 10개 생성
        for (int i = 1; i <= 10; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // SHIPPED 상태 주문 5개 생성
        for (int i = 1; i <= 5; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.SHIPPED)
                    .build();

            orderRepository.save(order);
        }

        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> pendingOrders = orderRepository.findByStatusWithPaging(OrderStatus.PENDING, pageable);
        Page<Order> shippedOrders = orderRepository.findByStatusWithPaging(OrderStatus.SHIPPED, pageable);

        // then
        assertThat(pendingOrders.getContent()).hasSize(5);
        assertThat(pendingOrders.getTotalElements()).isEqualTo(10);
        assertThat(pendingOrders.getTotalPages()).isEqualTo(2);
        assertThat(pendingOrders.getContent()).allMatch(o -> o.getStatus() == OrderStatus.PENDING);

        assertThat(shippedOrders.getContent()).hasSize(5);
        assertThat(shippedOrders.getTotalElements()).isEqualTo(5);
        assertThat(shippedOrders.getTotalPages()).isEqualTo(1);
        assertThat(shippedOrders.getContent()).allMatch(o -> o.getStatus() == OrderStatus.SHIPPED);
    }

    @Test
    @DisplayName("사용자 ID와 주문 상태로 주문을 페이징하여 조회 테스트 (커스텀 메서드)")
    public void findByUserIdAndStatusWithPagingTest() {
        // given
        User user1 = createTestUser();
        User user2 = User.builder()
                .username("테스트유저2")
                .email("test2@example.com")
                .build();
        user2 = userRepository.save(user2);

        // user1의 PENDING 상태 주문 8개 생성
        for (int i = 1; i <= 8; i++) {
            Order order = Order.builder()
                    .user(user1)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // user1의 SHIPPED 상태 주문 4개 생성
        for (int i = 1; i <= 4; i++) {
            Order order = Order.builder()
                    .user(user1)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.SHIPPED)
                    .build();

            orderRepository.save(order);
        }

        // user2의 PENDING 상태 주문 3개 생성
        for (int i = 1; i <= 3; i++) {
            Order order = Order.builder()
                    .user(user2)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> user1PendingOrders = orderRepository.findByUserIdAndStatusWithPaging(
                user1.getId(), OrderStatus.PENDING, pageable);

        // then
        assertThat(user1PendingOrders.getContent()).hasSize(5);
        assertThat(user1PendingOrders.getTotalElements()).isEqualTo(8);
        assertThat(user1PendingOrders.getTotalPages()).isEqualTo(2);
        assertThat(user1PendingOrders.getContent()).allMatch(o ->
                o.getUser().getId().equals(user1.getId()) && o.getStatus() == OrderStatus.PENDING);
    }

    @Test
    @DisplayName("검색 조건을 이용한 주문 검색 테스트 (커스텀 메서드)")
    public void searchOrdersTest() {
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

        // when - 검색 조건 생성 (PENDING 상태 주문)
        OrderSearchDto searchDto = new OrderSearchDto();
        searchDto.setUserId(user.getId());
        searchDto.setStatus(OrderStatus.PENDING);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> result = orderRepository.searchOrders(searchDto, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("특정 상품을 포함하는 주문 목록 조회 테스트 (커스텀 메서드)")
    public void findOrdersContainingProductTest() {
        // given
        User user = createTestUser();
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 3개의 주문 생성
        Order order1 = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order order2 = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order order3 = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.saveAll(List.of(order1, order2, order3));

        // order1, order2에만 product1 포함
        OrderItem orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product1)
                .quantity(2)
                .orderPrice(product1.getPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .order(order2)
                .product(product1)
                .quantity(1)
                .orderPrice(product1.getPrice())
                .build();

        // order3에는 product2 포함
        OrderItem orderItem3 = OrderItem.builder()
                .order(order3)
                .product(product2)
                .quantity(3)
                .orderPrice(product2.getPrice())
                .build();

        orderItemRepository.saveAll(List.of(orderItem1, orderItem2, orderItem3));

        // when
        List<Order> ordersWithProduct1 = orderRepository.findOrdersContainingProduct(product1.getId());

        // then
        assertThat(ordersWithProduct1).hasSize(2);
        assertThat(ordersWithProduct1).extracting("id")
                .containsExactlyInAnyOrder(order1.getId(), order2.getId());
    }

    @Test
    @DisplayName("최근 주문 목록 조회 테스트 (커스텀 메서드)")
    public void findRecentOrdersTest() {
        // given
        User user = createTestUser();

        // 시간차를 두고 5개 주문 생성
        for (int i = 5; i >= 1; i--) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusDays(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);
        }

        // when - 최근 3개 주문 조회
        List<Order> recentOrders = orderRepository.findRecentOrders(3);

        // then
        assertThat(recentOrders).hasSize(3);

        // 최신 순서대로 정렬되었는지 확인
        for (int i = 0; i < recentOrders.size() - 1; i++) {
            assertThat(recentOrders.get(i).getOrderDate())
                    .isAfterOrEqualTo(recentOrders.get(i + 1).getOrderDate());
        }
    }

    @Test
    @DisplayName("주문 상태 변경 테스트 (커스텀 메서드)")
    public void updateOrderStatusTest() {
        // given
        User user = createTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // when
        Order updatedOrder = orderRepository.updateOrderStatus(savedOrder.getId(), OrderStatus.PROCESSING);

        // then
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);

        // 실제로 DB에 반영되었는지 확인
        Optional<Order> reloadedOrder = orderRepository.findById(savedOrder.getId());
        assertThat(reloadedOrder).isPresent();
        assertThat(reloadedOrder.get().getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }
}