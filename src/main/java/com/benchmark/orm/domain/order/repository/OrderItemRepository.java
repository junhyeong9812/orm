package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 주문 상품 리포지토리 인터페이스
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemRepositoryCustom {

    /**
     * 주문 ID로 주문 상품 목록 조회
     *
     * @param orderId 주문 ID
     * @return 주문 상품 목록
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 상품 ID로 주문 상품 목록 조회
     *
     * @param productId 상품 ID
     * @return 주문 상품 목록
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * 주문 ID와 상품 ID로 주문 상품 조회
     *
     * @param orderId 주문 ID
     * @param productId 상품 ID
     * @return 주문 상품 목록
     */
    List<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);

    /**
     * 특정 수량 이상의 주문 상품 조회
     *
     * @param quantity 기준 수량
     * @return 주문 상품 목록
     */
    List<OrderItem> findByQuantityGreaterThanEqual(int quantity);

    /**
     * 특정 가격 범위의 주문 상품 조회
     *
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 주문 상품 목록
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderPrice BETWEEN :minPrice AND :maxPrice")
    List<OrderItem> findByPriceBetween(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

    /**
     * 주문 ID로 주문 상품의 총 금액 계산
     *
     * @param orderId 주문 ID
     * @return 총 금액
     */
    @Query("SELECT SUM(oi.orderPrice * oi.quantity) FROM OrderItem oi WHERE oi.order.id = :orderId")
    int calculateTotalPriceByOrderId(@Param("orderId") Long orderId);

    /**
     * 특정 상품이 포함된 주문 ID 목록 조회
     *
     * @param productId 상품 ID
     * @return 주문 ID 목록
     */
    @Query("SELECT DISTINCT oi.order.id FROM OrderItem oi WHERE oi.product.id = :productId")
    List<Long> findOrderIdsByProductId(@Param("productId") Long productId);
}