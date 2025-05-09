package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 주문 리포지토리의 커스텀 인터페이스
 * JPA 기본 인터페이스에서 제공하지 않는 기능을 확장하기 위한 인터페이스
 */
public interface OrderRepositoryCustom {

    /**
     * 사용자 ID로 주문 조회
     * @param userId 사용자 ID
     * @return 주문 리스트
     */
    List<Order> findByUserId(Long userId);

    /**
     * 상품 ID로 주문 조회
     * @param productId 상품 ID
     * @return 주문 리스트
     */
    List<Order> findByProductId(Long productId);

    /**
     * 주문 날짜 범위로 주문 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 리스트
     */
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 페이징 및 정렬 기능을 사용하여 모든 주문 조회
     * @param pageable 페이징 정보
     * @return 페이징된 주문 리스트
     */
    Page<Order> findAllWithPaging(Pageable pageable);

    /**
     * 특정 정렬 방식으로 모든 주문 조회
     * @param sort 정렬 정보
     * @return 정렬된 주문 리스트
     */
    List<Order> findAllWithSorting(Sort sort);

    /**
     * 사용자 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    Optional<Order> findOrderWithUser(Long orderId);

    /**
     * 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    Optional<Order> findOrderWithProduct(Long orderId);

    /**
     * 사용자 및 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 Optional 객체
     */
    Optional<Order> findOrderWithUserAndProduct(Long orderId);

    /**
     * 사용자 ID로 주문을 페이징하여 조회
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 페이징된 주문 리스트
     */
    Page<Order> findByUserIdWithPaging(Long userId, Pageable pageable);
}