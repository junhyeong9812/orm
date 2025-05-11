package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.QAddress;
import com.benchmark.orm.domain.user.entity.QUser;
import com.benchmark.orm.domain.user.entity.QUserProfile;
import com.benchmark.orm.domain.user.entity.User;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UserRepositoryCustom 인터페이스의 QueryDSL 구현체
 */
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        QUser user = QUser.user;
        User result = queryFactory
                .selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        QUser user = QUser.user;
        User result = queryFactory
                .selectFrom(user)
                .where(user.username.eq(username))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Page<User> findAllWithPaging(Pageable pageable) {
        QUser user = QUser.user;

        List<User> users = queryFactory
                .selectFrom(user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(user)
                .fetchCount();

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public List<User> findAllWithSorting(Sort sort) {
        QUser user = QUser.user;

        // Sort 객체에서 정렬 정보 추출
        List<Sort.Order> orders = new ArrayList<>();
        sort.forEach(orders::add);

        com.querydsl.core.types.OrderSpecifier<?>[] orderSpecifiers =
                new com.querydsl.core.types.OrderSpecifier[orders.size()];

        for (int i = 0; i < orders.size(); i++) {
            Sort.Order order = orders.get(i);
            com.querydsl.core.types.OrderSpecifier<?> orderSpecifier;

            if (order.getProperty().equals("username")) {
                orderSpecifier = order.isAscending()
                        ? user.username.asc() : user.username.desc();
            } else if (order.getProperty().equals("email")) {
                orderSpecifier = order.isAscending()
                        ? user.email.asc() : user.email.desc();
            } else if (order.getProperty().equals("id")) {
                orderSpecifier = order.isAscending()
                        ? user.id.asc() : user.id.desc();
            } else {
                // 기본값은 ID 기준 정렬
                orderSpecifier = user.id.asc();
            }
            orderSpecifiers[i] = orderSpecifier;
        }

        return queryFactory
                .selectFrom(user)
                .orderBy(orderSpecifiers)
                .fetch();
    }

    @Override
    public Optional<User> findUserWithProfile(Long userId) {
        QUser user = QUser.user;
        QUserProfile profile = QUserProfile.userProfile;

        User result = queryFactory
                .selectFrom(user)
                .leftJoin(user.profile, profile).fetchJoin()
                .where(user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findUserWithAddresses(Long userId) {
        QUser user = QUser.user;
        QAddress address = QAddress.address;

        User result = queryFactory
                .selectFrom(user)
                .leftJoin(user.addresses, address).fetchJoin()
                .where(user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findUserWithProfileAndAddresses(Long userId) {
        QUser user = QUser.user;
        QUserProfile profile = QUserProfile.userProfile;
        QAddress address = QAddress.address;

        // 프로필 먼저 조회
        User result = queryFactory
                .selectFrom(user)
                .leftJoin(user.profile, profile).fetchJoin()
                .where(user.id.eq(userId))
                .fetchOne();

        if (result != null) {
            // 주소 정보 조회 (N+1 문제를 방지하기 위해 별도 쿼리 실행)
            User userWithAddresses = queryFactory
                    .selectFrom(user)
                    .leftJoin(user.addresses, address).fetchJoin()
                    .where(user.id.eq(userId))
                    .fetchOne();

            if (userWithAddresses != null) {
                // 이미 영속성 컨텍스트에 존재하므로 주소 정보만 가져오면 됨
                result.getAddresses().addAll(userWithAddresses.getAddresses());
            }
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Page<User> searchUsers(UserSearchDto searchDto, Pageable pageable) {
        QUser user = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 (사용자명 또는 이메일)
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().isEmpty()) {
            builder.or(user.username.containsIgnoreCase(searchDto.getKeyword()));
            builder.or(user.email.containsIgnoreCase(searchDto.getKeyword()));
        }

        // 사용자명 검색
        if (searchDto.getUsername() != null && !searchDto.getUsername().isEmpty()) {
            builder.and(user.username.containsIgnoreCase(searchDto.getUsername()));
        }

        // 이메일 검색
        if (searchDto.getEmail() != null && !searchDto.getEmail().isEmpty()) {
            builder.and(user.email.containsIgnoreCase(searchDto.getEmail()));
        }

        // 정렬 설정
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // DTO의 정렬 정보가 있으면 사용
        if (searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");

            if ("asc".equalsIgnoreCase(searchDto.getSortDirection())) {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).asc());
            } else {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).desc());
            }
        }

        // Pageable의 정렬 정보 사용
        if (pageable.getSort() != null && pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");

                if (order.isAscending()) {
                    orderSpecifiers.add(entityPath.getString(order.getProperty()).asc());
                } else {
                    orderSpecifiers.add(entityPath.getString(order.getProperty()).desc());
                }
            });
        }

        // 기본 정렬이 없는 경우 ID 기준으로 정렬
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(user.id.asc());
        }

        // 검색 결과 조회
        List<User> users = queryFactory
                .selectFrom(user)
                .where(builder)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(user)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(users, pageable, total);
    }
}