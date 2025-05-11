package com.benchmark.orm.domain.order.mapper;

import com.benchmark.orm.domain.order.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 주문 상품 매퍼 인터페이스
 */
@Mapper
public interface OrderItemMapper {
    /**
     * 주문 상품 저장
     *
     * @param orderItem 저장할 주문 상품 정보
     */
    void insert(OrderItem orderItem);

    /**
     * 주문 상품 수정
     *
     * @param orderItem 수정할 주문 상품 정보
     */
    void update(OrderItem orderItem);

    /**
     * 주문 상품 수량 수정
     *
     * @param id 주문 상품 ID
     * @param quantity 변경할 수량
     */
    void updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    /**
     * 주문 상품 삭제
     *
     * @param id 삭제할 주문 상품 ID
     */
    void deleteById(Long id);

    /**
     * 주문 ID로 주문 상품 삭제
     *
     * @param orderId 주문 ID
     */
    void deleteByOrderId(Long orderId);

    /**
     * 주문 상품 조회
     *
     * @param id 조회할 주문 상품 ID
     * @return 주문 상품 정보
     */
    OrderItem findById(Long id);

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
    List<OrderItem> findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

    /**
     * 주문 ID로 주문 상품의 총 금액 계산
     *
     * @param orderId 주문 ID
     * @return 총 금액
     */
    Integer calculateTotalPriceByOrderId(Long orderId);

    /**
     * 모든 주문 상품 조회
     *
     * @return 주문 상품 목록
     */
    List<OrderItem> findAll();

    /**
     * 페이징된 주문 상품 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 주문 상품 목록
     */
    List<OrderItem> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 주문 ID로 페이징된 주문 상품 조회
     *
     * @param orderId 주문 ID
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 주문 상품 목록
     */
    List<OrderItem> findByOrderIdWithPaging(
            @Param("orderId") Long orderId,
            @Param("offset") int offset,
            @Param("limit") int limit);

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
    List<OrderItem> findByPriceBetween(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);

    /**
     * 특정 상품의 총 주문 수량 계산
     *
     * @param productId 상품 ID
     * @return 총 주문 수량
     */
    Integer calculateTotalQuantityForProduct(Long productId);

    /**
     * 가장 많이 주문된 상품 목록 조회
     *
     * @param limit 최대 개수
     * @return 주문 상품 목록
     */
    List<OrderItem> findMostOrderedProducts(int limit);
}