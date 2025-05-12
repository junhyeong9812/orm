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
 * ProductRepositoryCustom 인터페이스의 QueryDSL 구현체
 */
@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public ProductRepositoryCustomImpl(EntityManager entityManager, JPAQueryFactory queryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Product> findByName(String name) {
        QProduct product = QProduct.product;
        Product result = queryFactory
                .selectFrom(product)
                .where(product.name.eq(name))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<Product> findByPriceBetween(int minPrice, int maxPrice) {
        QProduct product = QProduct.product;
        return queryFactory
                .selectFrom(product)
                .where(product.price.between(minPrice, maxPrice))
                .fetch();
    }

    @Override
    public List<Product> findByBrandId(Long brandId) {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;

        return queryFactory
                .selectFrom(product)
                .join(product.brand, brand)
                .where(brand.id.eq(brandId))
                .fetch();
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        return queryFactory
                .selectFrom(product)
                .join(product.category, category)
                .where(category.id.eq(categoryId))
                .fetch();
    }

    @Override
    public Page<Product> findAllWithPaging(Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> products = queryFactory
                .selectFrom(product)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(product)
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public List<Product> findAllWithSorting(Sort sort) {
        QProduct product = QProduct.product;

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
                        ? product.name.asc() : product.name.desc();
            } else if (order.getProperty().equals("price")) {
                orderSpecifier = order.isAscending()
                        ? product.price.asc() : product.price.desc();
            } else if (order.getProperty().equals("id")) {
                orderSpecifier = order.isAscending()
                        ? product.id.asc() : product.id.desc();
            } else {
                // 기본값은 ID 기준 정렬
                orderSpecifier = product.id.asc();
            }
            orderSpecifiers[i] = orderSpecifier;
        }

        return queryFactory
                .selectFrom(product)
                .orderBy(orderSpecifiers)
                .fetch();
    }

    @Override
    public Optional<Product> findProductWithBrand(Long productId) {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;

        Product result = queryFactory
                .selectFrom(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Product> findProductWithCategory(Long productId) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        Product result = queryFactory
                .selectFrom(product)
                .leftJoin(product.category, category).fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Product> findProductWithImages(Long productId) {
        QProduct product = QProduct.product;
        QProductImage image = QProductImage.productImage;

        Product result = queryFactory
                .selectFrom(product)
                .leftJoin(product.images, image).fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Product> findProductWithAllDetails(Long productId) {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;
        QProductImage image = QProductImage.productImage;

        // 브랜드, 카테고리와 함께 상품 조회
        Product result = queryFactory
                .selectFrom(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .where(product.id.eq(productId))
                .fetchOne();

        if (result != null) {
            // 이미지 직접 조회 - 상품 엔티티를 통하지 않고 이미지 엔티티만 조회
            List<ProductImage> images = queryFactory
                    .selectFrom(image)
                    .where(image.product.id.eq(productId))
                    .fetch();

            // 기존 컬렉션 비우고 새로 조회한 이미지들로 채움
            result.getImages().clear();
            result.getImages().addAll(images);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Page<Product> searchProducts(ProductSearchDto searchDto, Pageable pageable) {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;

        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 (상품명)
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().isEmpty()) {
            builder.and(product.name.containsIgnoreCase(searchDto.getKeyword()));
        }

        // 가격 범위 검색
        if (searchDto.getMinPrice() != null && searchDto.getMaxPrice() != null) {
            builder.and(product.price.between(searchDto.getMinPrice(), searchDto.getMaxPrice()));
        } else if (searchDto.getMinPrice() != null) {
            builder.and(product.price.goe(searchDto.getMinPrice()));
        } else if (searchDto.getMaxPrice() != null) {
            builder.and(product.price.loe(searchDto.getMaxPrice()));
        }

        // 브랜드 ID 검색
        if (searchDto.getBrandId() != null) {
            builder.and(product.brand.id.eq(searchDto.getBrandId()));
        }

        // 카테고리 ID 검색
        if (searchDto.getCategoryId() != null) {
            builder.and(product.category.id.eq(searchDto.getCategoryId()));
        }

        // 정렬 설정
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // DTO의 정렬 정보가 있으면 사용
        if (searchDto.getSortBy() != null && !searchDto.getSortBy().isEmpty()) {
            PathBuilder<Product> entityPath = new PathBuilder<>(Product.class, "product");

            if ("asc".equalsIgnoreCase(searchDto.getSortDirection())) {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).asc());
            } else {
                orderSpecifiers.add(entityPath.getString(searchDto.getSortBy()).desc());
            }
        }

        // Pageable의 정렬 정보 사용
        if (pageable.getSort() != null && pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                PathBuilder<Product> entityPath = new PathBuilder<>(Product.class, "product");

                if (order.isAscending()) {
                    orderSpecifiers.add(entityPath.getString(order.getProperty()).asc());
                } else {
                    orderSpecifiers.add(entityPath.getString(order.getProperty()).desc());
                }
            });
        }

        // 기본 정렬이 없는 경우 ID 기준으로 정렬
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(product.id.asc());
        }

        // 검색 결과 조회
        List<Product> products = queryFactory
                .selectFrom(product)
                .leftJoin(product.brand, brand)
                .leftJoin(product.category, category)
                .where(builder)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(product)
                .leftJoin(product.brand, brand)
                .leftJoin(product.category, category)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }
}