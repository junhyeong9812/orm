package com.benchmark.orm.domain.product.performance;

import com.benchmark.orm.domain.product.entity.*;
import com.benchmark.orm.domain.product.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 인덱스 성능 비교 테스트의 기본 클래스
 * <p>
 * 공통 설정 및 데이터 생성 메서드 제공
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class ProductIndexBaseTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ProductIndexRepository productIndexRepository;

    @Autowired
    protected ProductImageRepository productImageRepository;

    @Autowired
    protected ProductIndexImageRepository productIndexImageRepository;

    @Autowired
    protected BrandRepository brandRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    protected static final int TEST_DATA_COUNT = 1000; // 테스트 데이터 수
    protected static final int BRAND_COUNT = 10;
    protected static final int CATEGORY_COUNT = 20;
    protected static final int IMAGE_PER_PRODUCT = 3;

    protected List<Brand> brands = new ArrayList<>();
    protected List<Category> categories = new ArrayList<>();
    protected List<String> testNames = new ArrayList<>();
    protected Random random = new Random();

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성 전 기존 데이터 삭제
        log.info("테스트 데이터 준비 시작...");

        // 기존 브랜드 및 카테고리 데이터 불러오기
        brands = brandRepository.findAll();
        categories = categoryRepository.findAll();

        // 브랜드 데이터 준비
        if (brands.isEmpty()) {
            for (int i = 1; i <= BRAND_COUNT; i++) {
                Brand brand = Brand.builder()
                        .name("테스트 브랜드 " + i)
                        .build();
                brands.add(brandRepository.save(brand));
            }
            log.info("브랜드 {} 개 생성 완료", BRAND_COUNT);
        }

        // 카테고리 데이터 준비
        if (categories.isEmpty()) {
            for (int i = 1; i <= CATEGORY_COUNT; i++) {
                Category category = Category.builder()
                        .name("테스트 카테고리 " + i)
                        .build();
                categories.add(categoryRepository.save(category));
            }
            log.info("카테고리 {} 개 생성 완료", CATEGORY_COUNT);
        }

        // 상품명 데이터 준비
        for (int i = 1; i <= TEST_DATA_COUNT; i++) {
            testNames.add("테스트 상품 " + i);
        }

        log.info("테스트 데이터 준비 완료");
    }

    /**
     * 테스트용 상품 데이터를 생성하는 메서드
     * 일반 Product 테이블과 인덱스가 있는 ProductIndex 테이블에 동일한 데이터 생성
     */
    protected void createTestData() {
        log.info("{}개의 테스트 상품 데이터 생성 시작...", TEST_DATA_COUNT);
        long start = System.nanoTime();

        // 일반 Product 데이터 생성
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            String name = testNames.get(i);
            int price = 1000 * (random.nextInt(100) + 1); // 1,000 ~ 100,000
            Brand brand = brands.get(random.nextInt(brands.size()));
            Category category = categories.get(random.nextInt(categories.size()));

            // 일반 Product 생성 및 저장
            Product product = Product.builder()
                    .name(name)
                    .price(price)
                    .brand(brand)
                    .category(category)
                    .build();

            Product savedProduct = productRepository.save(product);

            // 상품 이미지 추가
            for (int j = 0; j < IMAGE_PER_PRODUCT; j++) {
                ProductImage image = ProductImage.builder()
                        .url("https://example.com/image" + i + "_" + j + ".jpg")
                        .isThumbnail(j == 0) // 첫번째 이미지는 썸네일로 설정
                        .build();
                image.assignProduct(savedProduct);

                // ProductImage 저장
                productImageRepository.save(image);
            }

            // 동일한 ProductIndex 생성 및 저장
            ProductIndex productIndex = ProductIndex.builder()
                    .name(name)
                    .price(price)
                    .brand(brand)
                    .category(category)
                    .build();

            ProductIndex savedProductIndex = productIndexRepository.save(productIndex);

            // 상품 이미지 추가
            for (int j = 0; j < IMAGE_PER_PRODUCT; j++) {
                ProductIndexImage image = ProductIndexImage.builder()
                        .url("https://example.com/image" + i + "_" + j + ".jpg")
                        .isThumbnail(j == 0) // 첫번째 이미지는 썸네일로 설정
                        .build();
                image.assignProductIndex(savedProductIndex);

                // ProductIndexImage 저장
                productIndexImageRepository.save(image);
            }
        }

        long end = System.nanoTime();
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(end - start);
        log.info("데이터 생성 완료: {}개의 상품, 소요 시간: {}ms", TEST_DATA_COUNT, elapsedTime);
    }

    /**
     * 성능 향상률 계산 (퍼센트)
     *
     * @param original 인덱스 없는 경우의 실행 시간
     * @param improved 인덱스 있는 경우의 실행 시간
     * @return 성능 향상률 (%)
     */
    protected double calculateImprovement(long original, long improved) {
        if (original == 0) return 0;
        return Math.round((1 - (double) improved / original) * 10000) / 100.0;
    }

    /**
     * 성능 측정 결과를 로그로 출력하는 도우미 메서드
     */
    protected void logPerformanceResult(String methodName, String approach, long elapsedTime) {
        log.info("[성능 측정] {} - {}: {}ms", methodName, approach, elapsedTime);
    }
}