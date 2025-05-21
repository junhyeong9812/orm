package com.benchmark.orm.domain.product.performance;

import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.entity.ProductIndex;
import com.benchmark.orm.domain.product.mapper.ProductIndexMapper;
import com.benchmark.orm.domain.product.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 가격 범위 검색에 대한 인덱스 성능 비교 테스트
 */
@Slf4j
public class ProductIndexPriceSearchTest extends ProductIndexBaseTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductIndexMapper productIndexMapper;

    @Test
    @DisplayName("가격 범위 조회 성능 비교")
    public void comparePriceRangeSearchPerformance() {
        // 테스트 데이터 생성
        createTestData();

        int minPrice = 20000;
        int maxPrice = 50000;
        log.info("가격 범위 조회 성능 테스트 시작 - 범위: {} ~ {}", minPrice, maxPrice);

        // JPA Repository 성능 테스트 (인덱스 없는 경우)
        long startNormal = System.nanoTime();
        List<Product> productsJpa = productRepository.findByPriceBetweenJpql(minPrice, maxPrice);
        long endNormal = System.nanoTime();
        long elapsedNormal = TimeUnit.NANOSECONDS.toMillis(endNormal - startNormal);
        logPerformanceResult("가격 범위 조회", "JPA Repository (인덱스 없음)", elapsedNormal);
        log.info("조회된 상품 수 (JPA 인덱스 없음): {}", productsJpa.size());

        // JPA Repository 성능 테스트 (인덱스 있는 경우)
        long startIndexed = System.nanoTime();
        List<ProductIndex> productIndexesJpa = productIndexRepository.findByPriceBetweenJpql(minPrice, maxPrice);
        long endIndexed = System.nanoTime();
        long elapsedIndexed = TimeUnit.NANOSECONDS.toMillis(endIndexed - startIndexed);
        logPerformanceResult("가격 범위 조회", "JPA Repository (인덱스 있음)", elapsedIndexed);
        log.info("조회된 상품 수 (JPA 인덱스 있음): {}", productIndexesJpa.size());

        // QueryDSL 성능 테스트 (인덱스 없는 경우)
        long startQueryDsl = System.nanoTime();
        List<Product> productsQueryDsl = productRepository.findByPriceBetween(minPrice, maxPrice);
        long endQueryDsl = System.nanoTime();
        long elapsedQueryDsl = TimeUnit.NANOSECONDS.toMillis(endQueryDsl - startQueryDsl);
        logPerformanceResult("가격 범위 조회", "QueryDSL (인덱스 없음)", elapsedQueryDsl);
        log.info("조회된 상품 수 (QueryDSL 인덱스 없음): {}", productsQueryDsl.size());

        // QueryDSL 성능 테스트 (인덱스 있는 경우)
        long startIndexedQueryDsl = System.nanoTime();
        List<ProductIndex> productIndexesQueryDsl = productIndexRepository.findByPriceBetween(minPrice, maxPrice);
        long endIndexedQueryDsl = System.nanoTime();
        long elapsedIndexedQueryDsl = TimeUnit.NANOSECONDS.toMillis(endIndexedQueryDsl - startIndexedQueryDsl);
        logPerformanceResult("가격 범위 조회", "QueryDSL (인덱스 있음)", elapsedIndexedQueryDsl);
        log.info("조회된 상품 수 (QueryDSL 인덱스 있음): {}", productIndexesQueryDsl.size());

        // MyBatis 성능 테스트 (인덱스 없는 경우)
        long startMyBatis = System.nanoTime();
        List<Product> productsMyBatis = productMapper.findByPriceBetween(minPrice, maxPrice);
        long endMyBatis = System.nanoTime();
        long elapsedMyBatis = TimeUnit.NANOSECONDS.toMillis(endMyBatis - startMyBatis);
        logPerformanceResult("가격 범위 조회", "MyBatis (인덱스 없음)", elapsedMyBatis);
        log.info("조회된 상품 수 (MyBatis 인덱스 없음): {}", productsMyBatis.size());

        // MyBatis 성능 테스트 (인덱스 있는 경우)
        long startIndexedMyBatis = System.nanoTime();
        List<ProductIndex> productIndexesMyBatis = productIndexMapper.findByPriceBetween(minPrice, maxPrice);
        long endIndexedMyBatis = System.nanoTime();
        long elapsedIndexedMyBatis = TimeUnit.NANOSECONDS.toMillis(endIndexedMyBatis - startIndexedMyBatis);
        logPerformanceResult("가격 범위 조회", "MyBatis (인덱스 있음)", elapsedIndexedMyBatis);
        log.info("조회된 상품 수 (MyBatis 인덱스 있음): {}", productIndexesMyBatis.size());

        // 결과 요약
        log.info("=== 가격 범위 조회 성능 테스트 결과 요약 ===");
        log.info("JPA: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedNormal, elapsedIndexed, calculateImprovement(elapsedNormal, elapsedIndexed));
        log.info("QueryDSL: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedQueryDsl, elapsedIndexedQueryDsl, calculateImprovement(elapsedQueryDsl, elapsedIndexedQueryDsl));
        log.info("MyBatis: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedMyBatis, elapsedIndexedMyBatis, calculateImprovement(elapsedMyBatis, elapsedIndexedMyBatis));
    }
}