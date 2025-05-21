package com.benchmark.orm.domain.product.performance;

import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import com.benchmark.orm.domain.product.entity.Brand;
import com.benchmark.orm.domain.product.entity.Category;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.entity.ProductIndex;
import com.benchmark.orm.domain.product.mapper.ProductIndexMapper;
import com.benchmark.orm.domain.product.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 복합 검색 조건에 대한 인덱스 성능 비교 테스트
 */
@Slf4j
public class ProductIndexComplexSearchTest extends ProductIndexBaseTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductIndexMapper productIndexMapper;

    @Test
    @DisplayName("복합 검색 성능 비교")
    public void compareComplexSearchPerformance() {
        // 테스트 데이터 생성
        createTestData();

        // 복합 검색 조건 설정
        String keyword = "테스트";
        int minPrice = 10000;
        int maxPrice = 50000;
        Brand testBrand = brands.get(random.nextInt(brands.size()));
        Category testCategory = categories.get(random.nextInt(categories.size()));

        ProductSearchDto searchDto = ProductSearchDto.builder()
                .keyword(keyword)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .brandId(testBrand.getId())
                .categoryId(testCategory.getId())
                .sortBy("price")
                .sortDirection("desc")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "price"));

        log.info("복합 검색 성능 테스트 시작 - 키워드: {}, 가격: {} ~ {}, 브랜드: {}, 카테고리: {}, 정렬: price desc",
                keyword, minPrice, maxPrice, testBrand.getName(), testCategory.getName());

        // QueryDSL 성능 테스트 (인덱스 없는 경우)
        long startQueryDsl = System.nanoTime();
        Page<Product> productsQueryDsl = productRepository.searchProducts(searchDto, pageable);
        long endQueryDsl = System.nanoTime();
        long elapsedQueryDsl = TimeUnit.NANOSECONDS.toMillis(endQueryDsl - startQueryDsl);
        logPerformanceResult("복합 검색", "QueryDSL (인덱스 없음)", elapsedQueryDsl);
        log.info("조회된 상품 수 (QueryDSL 인덱스 없음): {}", productsQueryDsl.getContent().size());

        // QueryDSL 성능 테스트 (인덱스 있는 경우)
        long startIndexedQueryDsl = System.nanoTime();
        Page<ProductIndex> productIndexesQueryDsl = productIndexRepository.searchProductIndexs(searchDto, pageable);
        long endIndexedQueryDsl = System.nanoTime();
        long elapsedIndexedQueryDsl = TimeUnit.NANOSECONDS.toMillis(endIndexedQueryDsl - startIndexedQueryDsl);
        logPerformanceResult("복합 검색", "QueryDSL (인덱스 있음)", elapsedIndexedQueryDsl);
        log.info("조회된 상품 수 (QueryDSL 인덱스 있음): {}", productIndexesQueryDsl.getContent().size());

        // MyBatis 성능 테스트 (인덱스 없는 경우)
        long startMyBatis = System.nanoTime();
        List<Product> productsMyBatis = productMapper.searchProducts(searchDto, 0, 10, "price", "desc");
        long endMyBatis = System.nanoTime();
        long elapsedMyBatis = TimeUnit.NANOSECONDS.toMillis(endMyBatis - startMyBatis);
        logPerformanceResult("복합 검색", "MyBatis (인덱스 없음)", elapsedMyBatis);
        log.info("조회된 상품 수 (MyBatis 인덱스 없음): {}", productsMyBatis.size());

        // MyBatis 성능 테스트 (인덱스 있는 경우)
        long startIndexedMyBatis = System.nanoTime();
        List<ProductIndex> productIndexesMyBatis = productIndexMapper.searchProductIndexs(searchDto, 0, 10, "price", "desc");
        long endIndexedMyBatis = System.nanoTime();
        long elapsedIndexedMyBatis = TimeUnit.NANOSECONDS.toMillis(endIndexedMyBatis - startIndexedMyBatis);
        logPerformanceResult("복합 검색", "MyBatis (인덱스 있음)", elapsedIndexedMyBatis);
        log.info("조회된 상품 수 (MyBatis 인덱스 있음): {}", productIndexesMyBatis.size());

        // JPQL 복합 검색 테스트 (인덱스 없는 경우)
        long startJpql = System.nanoTime();
        Page<Product> productsJpql = productRepository.searchProductsJpql(
                searchDto.getKeyword(),
                searchDto.getMinPrice(),
                searchDto.getMaxPrice(),
                searchDto.getBrandId(),
                searchDto.getCategoryId(),
                pageable);
        long endJpql = System.nanoTime();
        long elapsedJpql = TimeUnit.NANOSECONDS.toMillis(endJpql - startJpql);
        logPerformanceResult("복합 검색", "JPQL (인덱스 없음)", elapsedJpql);
        log.info("조회된 상품 수 (JPQL 인덱스 없음): {}", productsJpql.getContent().size());

        // JPQL 복합 검색 테스트 (인덱스 있는 경우)
        long startIndexedJpql = System.nanoTime();
        Page<ProductIndex> productIndexesJpql = productIndexRepository.searchProductIndexsJpql(
                searchDto.getKeyword(),
                searchDto.getMinPrice(),
                searchDto.getMaxPrice(),
                searchDto.getBrandId(),
                searchDto.getCategoryId(),
                pageable);
        long endIndexedJpql = System.nanoTime();
        long elapsedIndexedJpql = TimeUnit.NANOSECONDS.toMillis(endIndexedJpql - startIndexedJpql);
        logPerformanceResult("복합 검색", "JPQL (인덱스 있음)", elapsedIndexedJpql);
        log.info("조회된 상품 수 (JPQL 인덱스 있음): {}", productIndexesJpql.getContent().size());

        // 결과 요약
        log.info("=== 복합 검색 성능 테스트 결과 요약 ===");
        log.info("QueryDSL: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedQueryDsl, elapsedIndexedQueryDsl, calculateImprovement(elapsedQueryDsl, elapsedIndexedQueryDsl));
        log.info("MyBatis: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedMyBatis, elapsedIndexedMyBatis, calculateImprovement(elapsedMyBatis, elapsedIndexedMyBatis));
        log.info("JPQL: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedJpql, elapsedIndexedJpql, calculateImprovement(elapsedJpql, elapsedIndexedJpql));
    }
}