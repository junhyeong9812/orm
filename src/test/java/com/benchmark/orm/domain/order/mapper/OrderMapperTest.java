package com.benchmark.orm.domain.order.mapper;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.entity.OrderItem;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.mapper.ProductMapper;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OrderMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 주문 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    // 테스트용 사용자 생성 및 저장 헬퍼 메서드
    private User createAndSaveTestUser() {
        User user = User.builder()
                .username("테스트유저")
                .email("test@example.com")
                .build();

        userMapper.insert(user);
        return user;
    }

    // 테스트용 상품 생성 및 저장 헬퍼 메서드
    private Product createAndSaveTestProduct(String name, int price) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .build();

        productMapper.insert(product);
        return product;
    }

    @Test
    @DisplayName("주문 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 주문 생성
        User user = createAndSaveTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        // when - 주문 저장
        orderMapper.insert(order);

        // ID 설정 확인
        assertThat(order.getId()).isNotNull();

        // then - ID로 조회 및 결과 검증
        Order foundOrder = orderMapper.findById(order.getId());
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("주문 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 주문 생성 및 저장
        User user = createAndSaveTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(order);

        // ID 설정 확인
        assertThat(order.getId()).isNotNull();

        // when - 주문 정보 수정
        Order updatedOrder = Order.builder()
                .id(order.getId())
                .user(user)
                .orderDate(order.getOrderDate())
                .status(OrderStatus.PROCESSING)
                .build();

        orderMapper.update(updatedOrder);

        // then - 결과 검증
        Order foundOrder = orderMapper.findById(order.getId());
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }

    @Test
    @DisplayName("주문 상태 수정 테스트")
    public void updateStatusTest() {
        // given - 테스트용 주문 생성 및 저장
        User user = createAndSaveTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(order);

        // ID 설정 확인
        assertThat(order.getId()).isNotNull();

        // when - 주문 상태 수정
        orderMapper.updateStatus(order.getId(), OrderStatus.SHIPPED.name());

        // then - 결과 검증
        Order foundOrder = orderMapper.findById(order.getId());
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    @DisplayName("주문 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 주문 생성 및 저장
        User user = createAndSaveTestUser();
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(order);

        // ID 설정 확인
        assertThat(order.getId()).isNotNull();

        // 삭제 전 존재 확인
        Order beforeDelete = orderMapper.findById(order.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 주문 삭제
        orderMapper.deleteById(order.getId());

        // then - 삭제 후 존재 여부 확인
        Order afterDelete = orderMapper.findById(order.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("사용자ID로 주문 조회 테스트")
    public void findByUserIdTest() {
        // given - 테스트용 주문 생성 및 저장
        User user = createAndSaveTestUser();

        for (int i = 0; i < 3; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderMapper.insert(order);
        }

        // when - 사용자ID로 주문 조회
        List<Order> orders = orderMapper.findByUserId(user.getId());

        // then - 결과 검증
        assertThat(orders).isNotEmpty();
        assertThat(orders.size()).isEqualTo(3);
        assertThat(orders).allMatch(o -> o.getUser().getId().equals(user.getId()));
    }

    @Test
    @DisplayName("주문 상태로 주문 조회 테스트")
    public void findByStatusTest() {
        // given - 테스트용 주문 생성 및 저장
        User user = createAndSaveTestUser();

        // PENDING 상태 주문 2개 생성
        for (int i = 0; i < 2; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderMapper.insert(order);
        }

        // SHIPPED 상태 주문 1개 생성
        Order shippedOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.SHIPPED)
                .build();

        orderMapper.insert(shippedOrder);

        // when - 주문 상태로 주문 조회
        List<Order> pendingOrders = orderMapper.findByStatus(OrderStatus.PENDING.name());
        List<Order> shippedOrders = orderMapper.findByStatus(OrderStatus.SHIPPED.name());

        // then - 결과 검증
        assertThat(pendingOrders).hasSize(2);
        assertThat(pendingOrders).allMatch(o -> o.getStatus() == OrderStatus.PENDING);

        assertThat(shippedOrders).hasSize(1);
        assertThat(shippedOrders).allMatch(o -> o.getStatus() == OrderStatus.SHIPPED);
    }

    @Test
    @DisplayName("사용자ID와 주문상태로 주문 조회 테스트")
    public void findByUserIdAndStatusTest() {
        // given - 테스트용 주문 생성 및 저장
        User user1 = createAndSaveTestUser();
        User user2 = User.builder()
                .username("테스트유저2")
                .email("test2@example.com")
                .build();
        userMapper.insert(user2);

        // user1의 PENDING 상태 주문 2개 생성
        for (int i = 0; i < 2; i++) {
            Order order = Order.builder()
                    .user(user1)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING)
                    .build();

            orderMapper.insert(order);
        }

        // user1의 SHIPPED 상태 주문 1개 생성
        Order user1ShippedOrder = Order.builder()
                .user(user1)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.SHIPPED)
                .build();

        orderMapper.insert(user1ShippedOrder);

        // user2의 PENDING 상태 주문 1개 생성
        Order user2Order = Order.builder()
                .user(user2)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(user2Order);

        // when - 사용자ID와 주문상태로 주문 조회
        List<Order> user1PendingOrders = orderMapper.findByUserIdAndStatus(user1.getId(), OrderStatus.PENDING.name());

        // then - 결과 검증
        assertThat(user1PendingOrders).hasSize(2);
        assertThat(user1PendingOrders).allMatch(o ->
                o.getUser().getId().equals(user1.getId()) && o.getStatus() == OrderStatus.PENDING);
    }

    @Test
    @DisplayName("주문 날짜 범위로 주문 조회 테스트")
    public void findByOrderDateBetweenTest() {
        // given - 테스트용 주문 생성 및 저장
        User user = createAndSaveTestUser();

        // 어제 주문
        Order yesterdayOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().minusDays(1))
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(yesterdayOrder);

        // 오늘 주문
        Order todayOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(todayOrder);

        // 내일 주문 (미래 데이터)
        Order tomorrowOrder = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now().plusDays(1))
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(tomorrowOrder);

        // when - 어제부터 오늘까지의 주문 조회
        LocalDateTime startDate = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Order> orders = orderMapper.findByOrderDateBetween(startDate, endDate);

        // then - 결과 검증
        assertThat(orders).hasSizeGreaterThanOrEqualTo(2);
        assertThat(orders).allMatch(o ->
                !o.getOrderDate().isBefore(startDate) && !o.getOrderDate().isAfter(endDate));
    }

    @Test
    @DisplayName("페이징 테스트")
    public void findAllWithPagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        User user = createAndSaveTestUser();

        for (int i = 1; i <= 20; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderMapper.insert(order);
        }

        // when - 페이징 적용하여 조회
        List<Order> page1 = orderMapper.findAllWithPaging(0, 5); // 첫 페이지 (5개)
        List<Order> page2 = orderMapper.findAllWithPaging(5, 5); // 두번째 페이지 (5개)

        // then - 결과 검증
        assertThat(page1).hasSize(5);
        assertThat(page2).hasSize(5);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(page1).extracting("id")
                .doesNotContainAnyElementsOf(page2.stream().map(Order::getId).toList());
    }

    @Test
    @DisplayName("정렬 테스트")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 주문 추가
        User user = createAndSaveTestUser();

        // 다양한 날짜의 주문 생성
        for (int i = 1; i <= 5; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusDays(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderMapper.insert(order);
        }

        // when - 주문 날짜 기준 오름차순 정렬
        List<Order> ascOrders = orderMapper.findAllWithSorting("order_date", "asc");

        // 주문 날짜 기준 내림차순 정렬
        List<Order> descOrders = orderMapper.findAllWithSorting("order_date", "desc");

        // then - 결과 검증
        assertThat(ascOrders).isNotEmpty();
        assertThat(descOrders).isNotEmpty();

        // 오름차순 확인
        for (int i = 0; i < ascOrders.size() - 1; i++) {
            assertThat(ascOrders.get(i).getOrderDate())
                    .isBeforeOrEqualTo(ascOrders.get(i + 1).getOrderDate());
        }

        // 내림차순 확인
        for (int i = 0; i < descOrders.size() - 1; i++) {
            assertThat(descOrders.get(i).getOrderDate())
                    .isAfterOrEqualTo(descOrders.get(i + 1).getOrderDate());
        }
    }

    @Test
    @DisplayName("페이징 및 정렬 함께 적용 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given - 테스트용 데이터 생성
        User user = createAndSaveTestUser();

        for (int i = 1; i <= 20; i++) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusHours(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderMapper.insert(order);
        }

        // when - 페이징 및 정렬 적용하여 조회 (주문 날짜 내림차순)
        List<Order> firstPage = orderMapper.findAllWithPagingAndSorting(0, 5, "order_date", "desc");
        List<Order> secondPage = orderMapper.findAllWithPagingAndSorting(5, 5, "order_date", "desc");

        // then - 결과 검증
        assertThat(firstPage).hasSize(5);
        assertThat(secondPage).hasSize(5);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(firstPage).extracting("id")
                .doesNotContainAnyElementsOf(secondPage.stream().map(Order::getId).toList());

        // 내림차순 확인
        for (int i = 0; i < firstPage.size() - 1; i++) {
            assertThat(firstPage.get(i).getOrderDate())
                    .isAfterOrEqualTo(firstPage.get(i + 1).getOrderDate());
        }
    }

    @Test
    @DisplayName("주문과 주문 상품 정보 함께 조회 테스트")
    public void findOrderWithOrderItemsTest() {
        // given
        User user = createAndSaveTestUser();
        Product product1 = createAndSaveTestProduct("상품1", 10000);
        Product product2 = createAndSaveTestProduct("상품2", 20000);

        // 주문 생성
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(order);

        // 주문 상품 생성
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

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);

        // when
        Order foundOrder = orderMapper.findOrderWithOrderItems(order.getId());

        // then
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
        assertThat(foundOrder.getOrderItems()).hasSize(2);

        // 상품 정보가 제대로 조회되는지 확인
        boolean hasProduct1 = false;
        boolean hasProduct2 = false;

        for (OrderItem item : foundOrder.getOrderItems()) {
            if (item.getProduct() != null && item.getProduct().getId().equals(product1.getId())) {
                hasProduct1 = true;
                assertThat(item.getQuantity()).isEqualTo(2);
            } else if (item.getProduct() != null && item.getProduct().getId().equals(product2.getId())) {
                hasProduct2 = true;
                assertThat(item.getQuantity()).isEqualTo(1);
            }
        }

        assertThat(hasProduct1).isTrue();
        assertThat(hasProduct2).isTrue();
    }

    @Test
    @DisplayName("사용자별 주문 총 금액 계산 테스트")
    public void calculateTotalOrderAmountByUserIdTest() {
        // given
        User user = createAndSaveTestUser();
        Product product1 = createAndSaveTestProduct("상품1", 10000);
        Product product2 = createAndSaveTestProduct("상품2", 20000);

        // 주문 생성
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(order);

        // 주문 상품 생성
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

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);

        // when
        Integer totalAmount = orderMapper.calculateTotalOrderAmountByUserId(user.getId());

        // then
        assertThat(totalAmount).isEqualTo(40000);  // 20000 + 20000 = 40000
    }

    @Test
    @DisplayName("최근 주문 목록 조회 테스트")
    public void findRecentOrdersTest() {
        // given
        User user = createAndSaveTestUser();

        List<Order> createdOrders = new ArrayList<>();

        // 날짜가 다른 5개의 주문 생성 (최신순)
        for (int i = 5; i >= 1; i--) {
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now().minusDays(i))
                    .status(OrderStatus.PENDING)
                    .build();

            orderMapper.insert(order);
            createdOrders.add(order);
        }

        // when - 최근 3개 주문 조회
        List<Order> recentOrders = orderMapper.findRecentOrders(3);

        // then
        assertThat(recentOrders).hasSize(3);

        // 최신 순서대로 정렬되었는지 확인
        for (int i = 0; i < recentOrders.size() - 1; i++) {
            assertThat(recentOrders.get(i).getOrderDate())
                    .isAfterOrEqualTo(recentOrders.get(i + 1).getOrderDate());
        }
    }
}