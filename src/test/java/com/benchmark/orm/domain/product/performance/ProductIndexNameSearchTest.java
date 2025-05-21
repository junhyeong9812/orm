package com.benchmark.orm.domain.product.performance;

import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.entity.ProductIndex;
import com.benchmark.orm.domain.product.mapper.ProductIndexMapper;
import com.benchmark.orm.domain.product.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 상품명 검색에 대한 인덱스 성능 비교 테스트
 */
@Slf4j
public class ProductIndexNameSearchTest extends ProductIndexBaseTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductIndexMapper productIndexMapper;

    @Test
    @DisplayName("테스트 데이터 생성")
    public void testCreateData() {
        createTestData();
    }

    @Test
    @DisplayName("상품명 조회 성능 비교")
    public void compareNameSearchPerformance() {
        // 테스트 데이터 생성
        createTestData();

        String testName = testNames.get(random.nextInt(testNames.size()));
        log.info("상품명 조회 성능 테스트 시작 - 검색어: {}", testName);

        // JPA Repository 성능 테스트 (인덱스 없는 경우)
        long startNormal = System.nanoTime();
        Product productJpa = productRepository.findByNameJpql(testName).orElse(null);
        long endNormal = System.nanoTime();
        long elapsedNormal = TimeUnit.NANOSECONDS.toMillis(endNormal - startNormal);
        logPerformanceResult("상품명 조회", "JPA Repository (인덱스 없음)", elapsedNormal);

        // JPA Repository 성능 테스트 (인덱스 있는 경우)
        long startIndexed = System.nanoTime();
        ProductIndex productIndexJpa = productIndexRepository.findByNameJpql(testName).orElse(null);
        long endIndexed = System.nanoTime();
        long elapsedIndexed = TimeUnit.NANOSECONDS.toMillis(endIndexed - startIndexed);
        logPerformanceResult("상품명 조회", "JPA Repository (인덱스 있음)", elapsedIndexed);

        // QueryDSL 성능 테스트 (인덱스 없는 경우)
        long startQueryDsl = System.nanoTime();
        Product productQueryDsl = productRepository.findByName(testName).orElse(null);
        long endQueryDsl = System.nanoTime();
        long elapsedQueryDsl = TimeUnit.NANOSECONDS.toMillis(endQueryDsl - startQueryDsl);
        logPerformanceResult("상품명 조회", "QueryDSL (인덱스 없음)", elapsedQueryDsl);

        // QueryDSL 성능 테스트 (인덱스 있는 경우)
        long startIndexedQueryDsl = System.nanoTime();
        ProductIndex productIndexQueryDsl = productIndexRepository.findByName(testName).orElse(null);
        long endIndexedQueryDsl = System.nanoTime();
        long elapsedIndexedQueryDsl = TimeUnit.NANOSECONDS.toMillis(endIndexedQueryDsl - startIndexedQueryDsl);
        logPerformanceResult("상품명 조회", "QueryDSL (인덱스 있음)", elapsedIndexedQueryDsl);

        // MyBatis 성능 테스트 (인덱스 없는 경우)
        long startMyBatis = System.nanoTime();
        Product productMyBatis = productMapper.findByName(testName);
        long endMyBatis = System.nanoTime();
        long elapsedMyBatis = TimeUnit.NANOSECONDS.toMillis(endMyBatis - startMyBatis);
        logPerformanceResult("상품명 조회", "MyBatis (인덱스 없음)", elapsedMyBatis);

        // MyBatis 성능 테스트 (인덱스 있는 경우)
        long startIndexedMyBatis = System.nanoTime();
        ProductIndex productIndexMyBatis = productIndexMapper.findByName(testName);
        long endIndexedMyBatis = System.nanoTime();
        long elapsedIndexedMyBatis = TimeUnit.NANOSECONDS.toMillis(endIndexedMyBatis - startIndexedMyBatis);
        logPerformanceResult("상품명 조회", "MyBatis (인덱스 있음)", elapsedIndexedMyBatis);

        // 결과 요약
        log.info("=== 상품명 조회 성능 테스트 결과 요약 ===");
        log.info("JPA: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedNormal, elapsedIndexed, calculateImprovement(elapsedNormal, elapsedIndexed));
        log.info("QueryDSL: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedQueryDsl, elapsedIndexedQueryDsl, calculateImprovement(elapsedQueryDsl, elapsedIndexedQueryDsl));
        log.info("MyBatis: 인덱스 없음({}ms) vs 인덱스 있음({}ms) - 성능 향상: {}%",
                elapsedMyBatis, elapsedIndexedMyBatis, calculateImprovement(elapsedMyBatis, elapsedIndexedMyBatis));
    }
}