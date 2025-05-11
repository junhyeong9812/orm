package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 주문 상품 리포지토리의 커스텀 인터페이스
 * JPA 기본 인터페이스에서 제공하지 않는 기능을 확장하기 위한 인터페이스
 */
public interface OrderItemRepositoryCustom {

    /**
     * 주문 상품 조회 (주문 및 상품 정보 포함)
     *
     * @param orderItemId 주문 상품 ID
     * @return 주문 상품 Optional 객체
     */
    Optional<OrderItem> findOrderItemWithDetails(Long orderItemId);

    /**
     * 주문 ID로 주문 상품 목록 조회 (상품 정보 포함)
     *
     * @param orderId 주문 ID
     * @return 주문 상품 목록
     */
    List<OrderItem> findByOrderIdWithProduct(Long orderId);

    /**
     * 상품 ID 목록으로 주문 상품 조회
     *
     * @param productIds 상품 ID 목록
     * @return 주문 상품 목록
     */
    List<OrderItem> findByProductIds(List<Long> productIds);

    /**
     * 특정 가격 이상의 상품을 포함한 주문 상품 조회
     *
     * @param price 기준 가격
     * @param pageable 페이징 정보
     * @return 페이징된 주문 상품 정보
     */
    Page<OrderItem> findByPriceGreaterThan(int price, Pageable pageable);

    /**
     * 특정 상품의 총 주문 수량 계산
     *
     * @param productId 상품 ID
     * @return 총 주문 수량
     */
    int calculateTotalQuantityForProduct(Long productId);

    /**
     * 가장 많이 주문된 상품 목록 조회
     *
     * @param limit 최대 개수
     * @return 주문 상품 목록
     */
    List<OrderItem> findMostOrderedProducts(int limit);
}