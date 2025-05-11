package com.benchmark.orm.domain.order.repository;

import com.benchmark.orm.domain.order.dto.OrderSearchDto;
import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.entity.QOrder;
import com.benchmark.orm.domain.order.entity.QOrderItem;
import com.benchmark.orm.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            } else if (orderItem.getProperty().equals("status")) {
                orderSpecifier = orderItem.isAscending()
                        ? order.status.asc() : order.status.desc();
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
    public Optional<Order> findOrderWithOrderItems(Long orderId) {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        Order result = queryFactory
                .selectFrom(order)
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Order> findOrderWithUserAndOrderItems(Long orderId) {
        QOrder order = QOrder.order;
        QUser user = QUser.user;
        QOrderItem orderItem = QOrderItem.orderItem;

        Order result = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
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

    @Override
    public Page<Order> findByStatusWithPaging(OrderStatus status, Pageable pageable) {
        QOrder order = QOrder.order;

        List<Order> orders = queryFactory
                .selectFrom(order)
                .where(order.status.eq(status))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(order)
                .where(order.status.eq(status))
                .fetchCount();

        return new PageImpl<>(orders, pageable, total);
    }

    @Override
    public Page<Order> findByUserIdAndStatusWithPaging(Long userId, OrderStatus status, Pageable pageable) {
        QOrder order = QOrder.order;

        List<Order> orders = queryFactory
                .selectFrom(order)
                .where(order.user.id.eq(userId).and(order.status.eq(status)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(order)
                .where(order.user.id.eq(userId).and(order.status.eq(status)))
                .fetchCount();

        return new PageImpl<>(orders, pageable, total);
    }

    @Override
    public Page<Order> searchOrders(OrderSearchDto searchDto, Pageable pageable) {
        QOrder order = QOrder.order;
        QUser user = QUser.user;
        QOrderItem orderItem = QOrderItem.orderItem;

        BooleanBuilder builder = new BooleanBuilder();

        // 사용자 ID 검색
        if (searchDto.getUserId() != null) {
            builder.and(order.user.id.eq(searchDto.getUserId()));
        }

        // 주문 날짜 범위 검색
        if (searchDto.getStartDate() != null && searchDto.getEndDate() != null) {
            builder.and(order.orderDate.between(searchDto.getStartDate(), searchDto.getEndDate()));
        } else if (searchDto.getStartDate() != null) {
            builder.and(order.orderDate.goe(searchDto.getStartDate()));
        } else if (searchDto.getEndDate() != null) {
            builder.and(order.orderDate.loe(searchDto.getEndDate()));
        }

        // 정렬 설정
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // DTO의 정렬 정보가 있으면 사용
        if (searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            PathBuilder<Order> entityPath = new PathBuilder<>(Order.class, "order");

            if ("asc".equalsIgnoreCase(searchDto.getSortDirection())) {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).asc());
            } else {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).desc());
            }
        }

        // Pageable의 정렬 정보 사용
        if (pageable.getSort() != null && pageable.getSort().isSorted()) {
            pageable.getSort().forEach(sort -> {
                PathBuilder<Order> entityPath = new PathBuilder<>(Order.class, "order");

                if (sort.isAscending()) {
                    orderSpecifiers.add(entityPath.getString(sort.getProperty()).asc());
                } else {
                    orderSpecifiers.add(entityPath.getString(sort.getProperty()).desc());
                }
            });
        }

        // 기본 정렬이 없는 경우 주문일자 기준으로 내림차순 정렬
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(order.orderDate.desc());
        }

        // 검색 결과 조회
        List<Order> orders = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user)
                .leftJoin(order.orderItems, orderItem)
                .where(builder)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user)
                .where(builder)
                .distinct()
                .fetchCount();

        return new PageImpl<>(orders, pageable, total);
    }

    @Override
    public List<Order> findOrdersContainingProduct(Long productId) {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        return queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem)
                .where(orderItem.product.id.eq(productId))
                .distinct()
                .fetch();
    }

    @Override
    public List<Order> findRecentOrders(int limit) {
        QOrder order = QOrder.order;

        return queryFactory
                .selectFrom(order)
                .orderBy(order.orderDate.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        QOrder order = QOrder.order;

        // 주문 상태 업데이트
        long updated = queryFactory
                .update(order)
                .set(order.status, status)
                .where(order.id.eq(orderId))
                .execute();

        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();

        // 업데이트된 주문 조회
        if (updated > 0) {
            return queryFactory
                    .selectFrom(order)
                    .where(order.id.eq(orderId))
                    .fetchOne();
        }

        return null;
    }
}