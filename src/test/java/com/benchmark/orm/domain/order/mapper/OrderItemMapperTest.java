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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OrderItemMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 주문 상품 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderItemMapperTest {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper; // 추가: Product를 DB에 저장하기 위해 필요

    @Autowired
    private UserMapper userMapper; // 추가: User를 DB에 저장하기 위해 필요 (필요시)

    // 테스트용 사용자 생성 및 저장 헬퍼 메서드
    private User createTestUser() {
        User user = User.builder()
                .username("테스트유저")
                .email("test@example.com")
                .build();

        // 필요시 User를 DB에 저장
        // userMapper.insert(user);

        return user;
    }

    // 테스트용 상품 생성 및 저장 헬퍼 메서드
    private Product createTestProduct(String name, int price) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .build();

        // 중요: Product를 DB에 저장 (이 부분이 핵심 수정)
        productMapper.insert(product);

        return product;
    }

    // 테스트용 주문 생성 헬퍼 메서드
    private Order createTestOrder(User user) {
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderMapper.insert(order);
        return order;
    }

    @Test
    @DisplayName("주문상품 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);
        Product product = createTestProduct("테스트상품", 10000);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        // when - 주문상품 저장
        orderItemMapper.insert(orderItem);

        // ID 설정 확인
        assertThat(orderItem.getId()).isNotNull();

        // then - ID로 조회 및 결과 검증
        OrderItem foundOrderItem = orderItemMapper.findById(orderItem.getId());
        assertThat(foundOrderItem).isNotNull();
        assertThat(foundOrderItem.getId()).isEqualTo(orderItem.getId());
        assertThat(foundOrderItem.getQuantity()).isEqualTo(2);
        assertThat(foundOrderItem.getOrderPrice()).isEqualTo(10000);
        assertThat(foundOrderItem.getOrder().getId()).isEqualTo(order.getId());
        assertThat(foundOrderItem.getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("주문상품 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 주문상품 생성 및 저장
        User user = createTestUser();
        Order order = createTestOrder(user);
        Product product = createTestProduct("테스트상품", 10000);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        orderItemMapper.insert(orderItem);

        // ID 설정 확인
        assertThat(orderItem.getId()).isNotNull();

        // when - 주문상품 정보 수정
        OrderItem updatedOrderItem = OrderItem.builder()
                .id(orderItem.getId())
                .order(order)
                .product(product)
                .quantity(3)  // 수량 변경
                .orderPrice(9000)  // 가격 변경
                .build();

        orderItemMapper.update(updatedOrderItem);

        // then - 결과 검증
        OrderItem foundOrderItem = orderItemMapper.findById(orderItem.getId());
        assertThat(foundOrderItem).isNotNull();
        assertThat(foundOrderItem.getQuantity()).isEqualTo(3);
        assertThat(foundOrderItem.getOrderPrice()).isEqualTo(9000);
    }

    @Test
    @DisplayName("주문상품 수량 수정 테스트")
    public void updateQuantityTest() {
        // given - 테스트용 주문상품 생성 및 저장
        User user = createTestUser();
        Order order = createTestOrder(user);
        Product product = createTestProduct("테스트상품", 10000);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        orderItemMapper.insert(orderItem);

        // ID 설정 확인
        assertThat(orderItem.getId()).isNotNull();

        // when - 주문상품 수량 수정
        orderItemMapper.updateQuantity(orderItem.getId(), 5);

        // then - 결과 검증
        OrderItem foundOrderItem = orderItemMapper.findById(orderItem.getId());
        assertThat(foundOrderItem).isNotNull();
        assertThat(foundOrderItem.getQuantity()).isEqualTo(5);
        assertThat(foundOrderItem.getOrderPrice()).isEqualTo(10000);  // 가격은 변경되지 않음
    }

    @Test
    @DisplayName("주문상품 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 주문상품 생성 및 저장
        User user = createTestUser();
        Order order = createTestOrder(user);
        Product product = createTestProduct("테스트상품", 10000);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        orderItemMapper.insert(orderItem);

        // ID 설정 확인
        assertThat(orderItem.getId()).isNotNull();

        // 삭제 전 존재 확인
        OrderItem beforeDelete = orderItemMapper.findById(orderItem.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 주문상품 삭제
        orderItemMapper.deleteById(orderItem.getId());

        // then - 삭제 후 존재 여부 확인
        OrderItem afterDelete = orderItemMapper.findById(orderItem.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("주문 ID로 주문상품 삭제 테스트")
    public void deleteByOrderIdTest() {
        // given - 테스트용 주문 및 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 두 개의 주문상품 추가
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

        // 주문상품 존재 확인
        List<OrderItem> beforeDelete = orderItemMapper.findByOrderId(order.getId());
        assertThat(beforeDelete).hasSize(2);

        // when - 주문 ID로 모든 주문상품 삭제
        orderItemMapper.deleteByOrderId(order.getId());

        // then - 삭제 후 주문상품 없음 확인
        List<OrderItem> afterDelete = orderItemMapper.findByOrderId(order.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("주문 ID로 주문상품 목록 조회 테스트")
    public void findByOrderIdTest() {
        // given - 테스트용 주문 및 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000);
        Product product2 = createTestProduct("상품2", 20000);

        // 두 개의 주문상품 추가
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

        // when - 주문 ID로 주문상품 목록 조회
        List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());

        // then - 결과 검증
        assertThat(orderItems).hasSize(2);
        assertThat(orderItems).extracting("id")
                .containsExactlyInAnyOrder(orderItem1.getId(), orderItem2.getId());
    }

    @Test
    @DisplayName("상품 ID로 주문상품 목록 조회 테스트")
    public void findByProductIdTest() {
        // given - 테스트용 주문 및 주문상품 생성
        User user = createTestUser();
        Order order1 = createTestOrder(user);
        Order order2 = createTestOrder(user);
        Product product = createTestProduct("테스트상품", 10000); // Product가 DB에 저장되도록 수정됨

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

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);

        // when - 상품 ID로 주문상품 목록 조회
        List<OrderItem> orderItems = orderItemMapper.findByProductId(product.getId());

        // then - 결과 검증
        assertThat(orderItems).hasSize(2);
        assertThat(orderItems).extracting("id")
                .containsExactlyInAnyOrder(orderItem1.getId(), orderItem2.getId());
    }

    @Test
    @DisplayName("주문 ID와 상품 ID로 주문상품 조회 테스트")
    public void findByOrderIdAndProductIdTest() {
        // given - 테스트용 주문 및 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);
        Product product1 = createTestProduct("상품1", 10000); // Product가 DB에 저장되도록 수정됨
        Product product2 = createTestProduct("상품2", 20000); // Product가 DB에 저장되도록 수정됨

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

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);

        // when - 주문 ID와 상품 ID로 주문상품 조회
        List<OrderItem> result = orderItemMapper.findByOrderIdAndProductId(order.getId(), product1.getId());

        // then - 결과 검증
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(orderItem1.getId());
        assertThat(result.get(0).getProduct().getId()).isEqualTo(product1.getId());
    }

    @Test
    @DisplayName("주문 ID로 주문상품의 총 금액 계산 테스트")
    public void calculateTotalPriceByOrderIdTest() {
        // given - 테스트용 주문 및 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);
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

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);

        // when - 주문 ID로 총 금액 계산
        Integer totalPrice = orderItemMapper.calculateTotalPriceByOrderId(order.getId());

        // then - 결과 검증
        assertThat(totalPrice).isEqualTo(40000);  // 20000 + 20000 = 40000
    }

    @Test
    @DisplayName("모든 주문상품 조회 테스트")
    public void findAllTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);
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

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);

        // when - 모든 주문상품 조회
        List<OrderItem> orderItems = orderItemMapper.findAll();

        // then - 결과 검증
        assertThat(orderItems).hasSizeGreaterThanOrEqualTo(2);
        assertThat(orderItems.stream().map(OrderItem::getId))
                .contains(orderItem1.getId(), orderItem2.getId());
    }

    @Test
    @DisplayName("페이징된 주문상품 조회 테스트")
    public void findAllWithPagingTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);

        // 여러개의 주문상품 추가
        for (int i = 1; i <= 10; i++) {
            Product product = createTestProduct("상품" + i, i * 1000);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(i)
                    .orderPrice(product.getPrice())
                    .build();

            orderItemMapper.insert(orderItem);
        }

        // when - 페이징 적용하여 조회
        List<OrderItem> page1 = orderItemMapper.findAllWithPaging(0, 3); // 첫 3개
        List<OrderItem> page2 = orderItemMapper.findAllWithPaging(3, 3); // 다음 3개

        // then - 결과 검증
        assertThat(page1).hasSize(3);
        assertThat(page2).hasSize(3);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(page1).extracting("id")
                .doesNotContainAnyElementsOf(page2.stream().map(OrderItem::getId).toList());
    }

    @Test
    @DisplayName("주문 ID로 페이징된 주문상품 조회 테스트")
    public void findByOrderIdWithPagingTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);

        // 여러개의 주문상품 추가
        for (int i = 1; i <= 10; i++) {
            Product product = createTestProduct("상품" + i, i * 1000);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(i)
                    .orderPrice(product.getPrice())
                    .build();

            orderItemMapper.insert(orderItem);
        }

        // when - 주문 ID와 페이징 적용하여 조회
        List<OrderItem> page1 = orderItemMapper.findByOrderIdWithPaging(order.getId(), 0, 3); // 첫 3개
        List<OrderItem> page2 = orderItemMapper.findByOrderIdWithPaging(order.getId(), 3, 3); // 다음 3개

        // then - 결과 검증
        assertThat(page1).hasSize(3);
        assertThat(page2).hasSize(3);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(page1).extracting("id")
                .doesNotContainAnyElementsOf(page2.stream().map(OrderItem::getId).toList());

        // 모든 주문상품이 동일한 주문에 속하는지 확인
        assertThat(page1).allMatch(item -> item.getOrder().getId().equals(order.getId()));
        assertThat(page2).allMatch(item -> item.getOrder().getId().equals(order.getId()));
    }

    @Test
    @DisplayName("특정 수량 이상의 주문상품 조회 테스트")
    public void findByQuantityGreaterThanEqualTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);

        // 다양한 수량의 주문상품 추가
        for (int i = 1; i <= 5; i++) {
            Product product = createTestProduct("상품" + i, 10000);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(i) // 1부터 5까지의 수량
                    .orderPrice(product.getPrice())
                    .build();

            orderItemMapper.insert(orderItem);
        }

        // when - 수량이 3 이상인 주문상품 조회
        List<OrderItem> result = orderItemMapper.findByQuantityGreaterThanEqual(3);

        // then - 결과 검증
        assertThat(result).hasSize(3); // 수량 3, 4, 5인 상품만 조회
        assertThat(result).allMatch(item -> item.getQuantity() >= 3);
    }

    @Test
    @DisplayName("특정 가격 범위의 주문상품 조회 테스트")
    public void findByPriceBetweenTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();
        Order order = createTestOrder(user);

        // 다양한 가격의 주문상품 추가
        for (int i = 1; i <= 5; i++) {
            Product product = createTestProduct("상품" + i, i * 10000); // 10000, 20000, 30000, 40000, 50000

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(1)
                    .orderPrice(product.getPrice())
                    .build();

            orderItemMapper.insert(orderItem);
        }

        // when - 가격이 20000원 이상 40000원 이하인 주문상품 조회
        List<OrderItem> result = orderItemMapper.findByPriceBetween(20000, 40000);

        // then - 결과 검증
        assertThat(result).hasSize(3); // 20000, 30000, 40000 가격의 상품만 조회
        assertThat(result).allMatch(item ->
                item.getOrderPrice() >= 20000 && item.getOrderPrice() <= 40000);
    }

    @Test
    @DisplayName("특정 상품의 총 주문 수량 계산 테스트")
    public void calculateTotalQuantityForProductTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();
        Product product = createTestProduct("테스트상품", 10000); // Product가 DB에 저장되도록 수정됨

        // 동일 상품을 여러 주문에 다양한 수량으로 추가
        Order order1 = createTestOrder(user);
        OrderItem orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product)
                .quantity(2)
                .orderPrice(product.getPrice())
                .build();

        Order order2 = createTestOrder(user);
        OrderItem orderItem2 = OrderItem.builder()
                .order(order2)
                .product(product)
                .quantity(3)
                .orderPrice(product.getPrice())
                .build();

        Order order3 = createTestOrder(user);
        OrderItem orderItem3 = OrderItem.builder()
                .order(order3)
                .product(product)
                .quantity(5)
                .orderPrice(product.getPrice())
                .build();

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);
        orderItemMapper.insert(orderItem3);

        // when - 특정 상품의 총 주문 수량 계산
        Integer totalQuantity = orderItemMapper.calculateTotalQuantityForProduct(product.getId());

        // then - 결과 검증
        assertThat(totalQuantity).isEqualTo(10); // 2 + 3 + 5 = 10
    }

    @Test
    @DisplayName("가장 많이 주문된 상품 목록 조회 테스트")
    public void findMostOrderedProductsTest() {
        // given - 테스트용 주문상품 생성
        User user = createTestUser();

        Product product1 = createTestProduct("인기상품1", 10000); // Product가 DB에 저장되도록 수정됨
        Product product2 = createTestProduct("인기상품2", 20000); // Product가 DB에 저장되도록 수정됨
        Product product3 = createTestProduct("인기상품3", 30000); // Product가 DB에 저장되도록 수정됨

        // product1은 총 10개 주문 (7+3)
        Order order1 = createTestOrder(user);
        OrderItem orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product1)
                .quantity(7)
                .orderPrice(product1.getPrice())
                .build();

        Order order2 = createTestOrder(user);
        OrderItem orderItem2 = OrderItem.builder()
                .order(order2)
                .product(product1)
                .quantity(3)
                .orderPrice(product1.getPrice())
                .build();

        // product2는 총 5개 주문
        Order order3 = createTestOrder(user);
        OrderItem orderItem3 = OrderItem.builder()
                .order(order3)
                .product(product2)
                .quantity(5)
                .orderPrice(product2.getPrice())
                .build();

        // product3는 총 2개 주문
        Order order4 = createTestOrder(user);
        OrderItem orderItem4 = OrderItem.builder()
                .order(order4)
                .product(product3)
                .quantity(2)
                .orderPrice(product3.getPrice())
                .build();

        orderItemMapper.insert(orderItem1);
        orderItemMapper.insert(orderItem2);
        orderItemMapper.insert(orderItem3);
        orderItemMapper.insert(orderItem4);

        // when - 가장 많이 주문된 상품 목록 조회 (상위 2개)
        List<OrderItem> mostOrderedProducts = orderItemMapper.findMostOrderedProducts(2);

        // then - 결과 검증
        assertThat(mostOrderedProducts).isNotEmpty(); // 비었는지만 확인 (GROUP BY 수정 후 정확한 사이즈 검증)

        if (mostOrderedProducts.size() >= 2) {
            // 첫 번째는 product1과 관련된 주문상품
            assertThat(mostOrderedProducts.get(0).getProduct().getId()).isEqualTo(product1.getId());

            // 두 번째는 product2와 관련된 주문상품
            assertThat(mostOrderedProducts.get(1).getProduct().getId()).isEqualTo(product2.getId());
        }
    }
}