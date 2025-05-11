package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 사용자 ID로 주문 조회
     *
     * @param userId 사용자 ID
     * @return 주문 목록
     */
    List<Order> findByUserId(Long userId);

    /**
     * 주문 상태로 주문 조회
     *
     * @param status 주문 상태
     * @return 주문 목록
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * 사용자 ID와 주문 상태로 주문 조회
     *
     * @param userId 사용자 ID
     * @param status 주문 상태
     * @return 주문 목록
     */
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    /**
     * 주문 날짜 범위로 주문 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 목록
     */
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * JPQL을 사용한 사용자 ID로 주문 조회
     *
     * @param userId 사용자 ID
     * @return 주문 리스트
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserIdJpql(@Param("userId") Long userId);

    /**
     * JPQL을 사용한 주문 날짜 범위로 주문 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 리스트
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetweenJpql(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * JPQL을 사용한 사용자 정보와 함께 주문 조회
     *
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.id = :orderId")
    Optional<Order> findOrderWithUserJpql(@Param("orderId") Long orderId);

    /**
     * JPQL을 사용한 주문 상품 정보와 함께 주문 조회
     *
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findOrderWithOrderItemsJpql(@Param("orderId") Long orderId);

    /**
     * JPQL을 사용한 사용자 및 주문 상품 정보와 함께 주문 조회
     *
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findOrderWithUserAndOrderItemsJpql(@Param("orderId") Long orderId);

    /**
     * JPQL을 사용한 주문 상태별 주문 수 카운트
     *
     * @param status 주문 상태
     * @return 주문 수
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatusJpql(@Param("status") OrderStatus status);

    /**
     * JPQL을 사용한 사용자별 총 주문 금액 계산
     *
     * @param userId 사용자 ID
     * @return 총 주문 금액
     */
    @Query("SELECT SUM(oi.orderPrice * oi.quantity) FROM Order o JOIN o.orderItems oi WHERE o.user.id = :userId")
    Integer calculateTotalOrderAmountByUserId(@Param("userId") Long userId);

    /**
     * JPQL을 사용한 복합 조건으로 주문 검색 (페이징)
     *
     * @param userId 사용자 ID
     * @param status 주문 상태
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param pageable 페이징 정보
     * @return 페이징된 주문 정보
     */
    @Query("SELECT o FROM Order o WHERE " +
            "(:userId IS NULL OR o.user.id = :userId) AND " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:startDate IS NULL OR o.orderDate >= :startDate) AND " +
            "(:endDate IS NULL OR o.orderDate <= :endDate)")
    Page<Order> searchOrdersJpql(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}