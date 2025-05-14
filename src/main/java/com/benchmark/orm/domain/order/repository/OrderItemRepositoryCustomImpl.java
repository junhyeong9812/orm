package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.OrderItem;
import com.benchmark.orm.domain.order.entity.QOrderItem;
import com.benchmark.orm.domain.product.entity.QProduct;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OrderItemRepositoryCustom 인터페이스의 QueryDSL 구현체
 */
@Repository
public class OrderItemRepositoryCustomImpl implements OrderItemRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public OrderItemRepositoryCustomImpl(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<OrderItem> findOrderItemWithDetails(Long orderItemId) {
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;

        OrderItem result = queryFactory
                .selectFrom(orderItem)
                .leftJoin(orderItem.product, product).fetchJoin()
                .where(orderItem.id.eq(orderItemId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<OrderItem> findByOrderIdWithProduct(Long orderId) {
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;

        return queryFactory
                .selectFrom(orderItem)
                .leftJoin(orderItem.product, product).fetchJoin()
                .where(orderItem.order.id.eq(orderId))
                .fetch();
    }

    @Override
    public List<OrderItem> findByProductIds(List<Long> productIds) {
        QOrderItem orderItem = QOrderItem.orderItem;

        return queryFactory
                .selectFrom(orderItem)
                .where(orderItem.product.id.in(productIds))
                .fetch();
    }

    @Override
    public Page<OrderItem> findByPriceGreaterThan(int price, Pageable pageable) {
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;

        List<OrderItem> results = queryFactory
                .selectFrom(orderItem)
                .leftJoin(orderItem.product, product).fetchJoin()
                .where(orderItem.orderPrice.gt(price))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(orderItem)
                .where(orderItem.orderPrice.gt(price))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public int calculateTotalQuantityForProduct(Long productId) {
        QOrderItem orderItem = QOrderItem.orderItem;

        Integer result = queryFactory
                .select(orderItem.quantity.sum())
                .from(orderItem)
                .where(orderItem.product.id.eq(productId))
                .fetchOne();

        return result != null ? result : 0;
    }

    @Override
    public List<OrderItem> findMostOrderedProducts(int limit) {
        QOrderItem orderItem = QOrderItem.orderItem;

        // 1단계: 각 상품별 총 주문 수량을 계산하여 상위 N개 상품 ID 추출
        List<Long> productIds = queryFactory
                .select(orderItem.product.id)
                .from(orderItem)
                .groupBy(orderItem.product.id)
                .orderBy(orderItem.quantity.sum().desc())
                .limit(limit)
                .fetch();

        if (productIds.isEmpty()) {
            return List.of();
        }

        // 2단계: 각 상품 ID에 대해 가장 큰 주문 수량을 가진 주문상품 하나씩만 조회
        List<OrderItem> result = new ArrayList<>();

        for (Long productId : productIds) {
            OrderItem item = queryFactory
                    .selectFrom(orderItem)
                    .join(orderItem.product).fetchJoin()
                    .where(orderItem.product.id.eq(productId))
                    .orderBy(orderItem.quantity.desc())
                    .fetchFirst(); // 각 상품 ID에 대해 가장 큰 주문 수량을 가진 항목 하나만 가져옴

            if (item != null) {
                result.add(item);
            }
        }

        return result;
    }
}