package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.QOrder;
import com.benchmark.orm.domain.product.entity.QProduct;
import com.benchmark.orm.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OrderRepositoryCustom 인터페이스의 QueryDSL 구현체
 */
@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        QOrder order = QOrder.order;
        QUser user = QUser.user;

        return queryFactory
                .selectFrom(order)
                .join(order.user, user)
                .where(user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Order> findByProductId(Long productId) {
        QOrder order = QOrder.order;
        QProduct product = QProduct.product;

        return queryFactory
                .selectFrom(order)
                .join(order.product, product)
                .where(product.id.eq(productId))
                .fetch();
    }

    @Override
    public List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        QOrder order = QOrder.order;

        return queryFactory
                .selectFrom(order)
                .where(order.orderDate.between(startDate, endDate))
                .fetch();
    }

    @Override
    public Page<Order> findAllWithPaging(Pageable pageable) {
        QOrder order = QOrder.order;

        List<Order> orders = queryFactory
                .selectFrom(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(order)
                .fetchCount();

        return new PageImpl<>(orders, pageable, total);
    }

    @Override
    public List<Order> findAllWithSorting(Sort sort) {
        QOrder order = QOrder.order;

        // Sort 객체에서 정렬 정보 추출
        List<Sort.Order> orders = new ArrayList<>();
        sort.forEach(orders::add);

        com.querydsl.core.types.OrderSpecifier<?>[] orderSpecifiers =
                new com.querydsl.core.types.OrderSpecifier[orders.size()];

        for (int i = 0; i < orders.size(); i++) {
            Sort.Order orderItem = orders.get(i);
            com.querydsl.core.types.OrderSpecifier<?> orderSpecifier;

            if (orderItem.getProperty().equals("orderDate")) {
                orderSpecifier = orderItem.isAscending()
                        ? order.orderDate.asc() : order.orderDate.desc();
            } else if (orderItem.getProperty().equals("id")) {
                orderSpecifier = orderItem.isAscending()
                        ? order.id.asc() : order.id.desc();
            } else {
                // 기본값은 ID 기준 정렬
                orderSpecifier = order.id.asc();
            }
            orderSpecifiers[i] = orderSpecifier;
        }

        return queryFactory
                .selectFrom(order)
                .orderBy(orderSpecifiers)
                .fetch();
    }

    @Override
    public Optional<Order> findOrderWithUser(Long orderId) {
        QOrder order = QOrder.order;
        QUser user = QUser.user;

        Order result = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Order> findOrderWithProduct(Long orderId) {
        QOrder order = QOrder.order;
        QProduct product = QProduct.product;

        Order result = queryFactory
                .selectFrom(order)
                .leftJoin(order.product, product).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Order> findOrderWithUserAndProduct(Long orderId) {
        QOrder order = QOrder.order;
        QUser user = QUser.user;
        QProduct product = QProduct.product;

        Order result = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .leftJoin(order.product, product).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<Order> findByUserIdWithPaging(Long userId, Pageable pageable) {
        QOrder order = QOrder.order;
        QUser user = QUser.user;

        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.user, user)
                .where(user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(order)
                .join(order.user, user)
                .where(user.id.eq(userId))
                .fetchCount();

        return new PageImpl<>(orders, pageable, total);
    }
}