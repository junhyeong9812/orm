package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import com.benchmark.orm.domain.product.entity.*;
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
 * ProductIndexRepositoryCustom 인터페이스의 QueryDSL 구현체
 */
@Repository
public class ProductIndexRepositoryCustomImpl implements ProductIndexRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public ProductIndexRepositoryCustomImpl(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<ProductIndex> findByName(String name) {
        QProductIndex productIndex = QProductIndex.productIndex;
        ProductIndex result = queryFactory
                .selectFrom(productIndex)
                .where(productIndex.name.eq(name))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<ProductIndex> findByPriceBetween(int minPrice, int maxPrice) {
        QProductIndex productIndex = QProductIndex.productIndex;
        return queryFactory
                .selectFrom(productIndex)
                .where(productIndex.price.between(minPrice, maxPrice))
                .fetch();
    }

    @Override
    public List<ProductIndex> findByBrandId(Long brandId) {
        QProductIndex productIndex = QProductIndex.productIndex;
        QBrand brand = QBrand.brand;

        return queryFactory
                .selectFrom(productIndex)
                .join(productIndex.brand, brand)
                .where(brand.id.eq(brandId))
                .fetch();
    }

    @Override
    public List<ProductIndex> findByCategoryId(Long categoryId) {
        QProductIndex productIndex = QProductIndex.productIndex;
        QCategory category = QCategory.category;

        return queryFactory
                .selectFrom(productIndex)
                .join(productIndex.category, category)
                .where(category.id.eq(categoryId))
                .fetch();
    }

    @Override
    public Page<ProductIndex> findAllWithPaging(Pageable pageable) {
        QProductIndex productIndex = QProductIndex.productIndex;

        List<ProductIndex> products = queryFactory
                .selectFrom(productIndex)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(productIndex)
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public List<ProductIndex> findAllWithSorting(Sort sort) {
        QProductIndex productIndex = QProductIndex.productIndex;

        // Sort 객체에서 정렬 정보 추출
        List<Sort.Order> orders = new ArrayList<>();
        sort.forEach(orders::add);

        com.querydsl.core.types.OrderSpecifier<?>[] orderSpecifiers =
                new com.querydsl.core.types.OrderSpecifier[orders.size()];

        for (int i = 0; i < orders.size(); i++) {
            Sort.Order order = orders.get(i);
            com.querydsl.core.types.OrderSpecifier<?> orderSpecifier;

            if (order.getProperty().equals("name")) {
                orderSpecifier = order.isAscending()
                        ? productIndex.name.asc() : productIndex.name.desc();
            } else if (order.getProperty().equals("price")) {
                orderSpecifier = order.isAscending()
                        ? productIndex.price.asc() : productIndex.price.desc();
            } else if (order.getProperty().equals("id")) {
                orderSpecifier = order.isAscending()
                        ? productIndex.id.asc() : productIndex.id.desc();
            } else {
                // 기본값은 ID 기준 정렬
                orderSpecifier = productIndex.id.asc();
            }
            orderSpecifiers[i] = orderSpecifier;
        }

        return queryFactory
                .selectFrom(productIndex)
                .orderBy(orderSpecifiers)
                .fetch();
    }

    @Override
    public Optional<ProductIndex> findProductIndexWithBrand(Long productIndexId) {
        QProductIndex productIndex = QProductIndex.productIndex;
        QBrand brand = QBrand.brand;

        ProductIndex result = queryFactory
                .selectFrom(productIndex)
                .leftJoin(productIndex.brand, brand).fetchJoin()
                .where(productIndex.id.eq(productIndexId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ProductIndex> findProductIndexWithCategory(Long productIndexId) {
        QProductIndex productIndex = QProductIndex.productIndex;
        QCategory category = QCategory.category;

        ProductIndex result = queryFactory
                .selectFrom(productIndex)
                .leftJoin(productIndex.category, category).fetchJoin()
                .where(productIndex.id.eq(productIndexId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ProductIndex> findProductIndexWithImages(Long productIndexId) {
        QProductIndex productIndex = QProductIndex.productIndex;
        QProductIndexImage image = QProductIndexImage.productIndexImage;

        ProductIndex result = queryFactory
                .selectFrom(productIndex)
                .leftJoin(productIndex.images, image).fetchJoin()
                .where(productIndex.id.eq(productIndexId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ProductIndex> findProductIndexWithAllDetails(Long productIndexId) {
        QProductIndex productIndex = QProductIndex.productIndex;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;
        QProductIndexImage image = QProductIndexImage.productIndexImage;

        // 브랜드, 카테고리와 함께 상품 조회
        ProductIndex result = queryFactory
                .selectFrom(productIndex)
                .leftJoin(productIndex.brand, brand).fetchJoin()
                .leftJoin(productIndex.category, category).fetchJoin()
                .where(productIndex.id.eq(productIndexId))
                .fetchOne();

        if (result != null) {
            // 이미지 직접 조회 - 상품 엔티티를 통하지 않고 이미지 엔티티만 조회
            List<ProductIndexImage> images = queryFactory
                    .selectFrom(image)
                    .where(image.productIndex.id.eq(productIndexId))
                    .fetch();

            // 기존 컬렉션 비우고 새로 조회한 이미지들로 채움
            result.getImages().clear();
            result.getImages().addAll(images);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Page<ProductIndex> searchProductIndexs(ProductSearchDto searchDto, Pageable pageable) {
        QProductIndex productIndex = QProductIndex.productIndex;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;

        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 (상품명)
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().isEmpty()) {
            builder.and(productIndex.name.containsIgnoreCase(searchDto.getKeyword()));
        }

        // 가격 범위 검색
        if (searchDto.getMinPrice() != null && searchDto.getMaxPrice() != null) {
            builder.and(productIndex.price.between(searchDto.getMinPrice(), searchDto.getMaxPrice()));
        } else if (searchDto.getMinPrice() != null) {
            builder.and(productIndex.price.goe(searchDto.getMinPrice()));
        } else if (searchDto.getMaxPrice() != null) {
            builder.and(productIndex.price.loe(searchDto.getMaxPrice()));
        }

        // 브랜드 ID 검색
        if (searchDto.getBrandId() != null) {
            builder.and(productIndex.brand.id.eq(searchDto.getBrandId()));
        }

        // 카테고리 ID 검색
        if (searchDto.getCategoryId() != null) {
            builder.and(productIndex.category.id.eq(searchDto.getCategoryId()));
        }

        // 정렬 설정
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // DTO의 정렬 정보가 있으면 사용
        if (searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            PathBuilder<ProductIndex> entityPath = new PathBuilder<>(ProductIndex.class, "productIndex");

            if ("asc".equalsIgnoreCase(searchDto.getSortDirection())) {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).asc());
            } else {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).desc());
            }
        }

        // Pageable의 정렬 정보 사용
        if (pageable.getSort() != null && pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                PathBuilder<ProductIndex> entityPath = new PathBuilder<>(ProductIndex.class, "productIndex");

                if (order.isAscending()) {
                    orderSpecifiers.add(entityPath.getString(order.getProperty()).asc());
                } else {
                    orderSpecifiers.add(entityPath.getString(order.getProperty()).desc());
                }
            });
        }

        // 기본 정렬이 없는 경우 ID 기준으로 정렬
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(productIndex.id.asc());
        }

        // 검색 결과 조회
        List<ProductIndex> products = queryFactory
                .selectFrom(productIndex)
                .leftJoin(productIndex.brand, brand)
                .leftJoin(productIndex.category, category)
                .where(builder)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(productIndex)
                .leftJoin(productIndex.brand, brand)
                .leftJoin(productIndex.category, category)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }
}