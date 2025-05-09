package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 주문 리포지토리 인터페이스
 * OrderRepositoryCustom 확장
 */
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    /**
     * JPQL을 사용한 사용자 ID로 주문 조회
     * @param userId 사용자 ID
     * @return 주문 리스트
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserIdJpql(@Param("userId") Long userId);

    /**
     * JPQL을 사용한 상품 ID로 주문 조회
     * @param productId 상품 ID
     * @return 주문 리스트
     */
    @Query("SELECT o FROM Order o WHERE o.product.id = :productId")
    List<Order> findByProductIdJpql(@Param("productId") Long productId);

    /**
     * JPQL을 사용한 주문 날짜 범위로 주문 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 리스트
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetweenJpql(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * JPQL을 사용한 사용자 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.id = :orderId")
    Optional<Order> findOrderWithUserJpql(@Param("orderId") Long orderId);

    /**
     * JPQL을 사용한 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.product WHERE o.id = :orderId")
    Optional<Order> findOrderWithProductJpql(@Param("orderId") Long orderId);

    /**
     * JPQL을 사용한 사용자 및 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.product WHERE o.id = :orderId")
    Optional<Order> findOrderWithUserAndProductJpql(@Param("orderId") Long orderId);
}