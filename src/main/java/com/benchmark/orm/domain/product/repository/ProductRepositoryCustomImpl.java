package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.*;
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
            // 이미지 정보 조회 (N+1 문제를 방지하기 위해 별도 쿼리 실행)
            Product productWithImages = queryFactory
                    .selectFrom(product)
                    .leftJoin(product.images, image).fetchJoin()
                    .where(product.id.eq(productId))
                    .fetchOne();

            if (productWithImages != null) {
                // 이미 영속성 컨텍스트에 존재하므로 이미지 정보만 가져오면 됨
                result.getImages().addAll(productWithImages.getImages());
            }
        }

        return Optional.ofNullable(result);
    }
}