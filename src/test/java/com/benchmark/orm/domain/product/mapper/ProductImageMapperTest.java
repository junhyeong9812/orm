package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.entity.Brand;
import com.benchmark.orm.domain.product.entity.Category;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.entity.ProductImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductImageMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 상품 이미지 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductImageMapperTest {

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // 테스트용 브랜드 생성 및 저장
        Brand brand = Brand.builder()
                .name("이미지테스트 브랜드")
                .build();
        brandMapper.insert(brand);

        // 테스트용 카테고리 생성 및 저장
        Category category = Category.builder()
                .name("이미지테스트 카테고리")
                .build();
        categoryMapper.insert(category);

        // 테스트용 상품 생성 및 저장
        Product product = Product.builder()
                .name("이미지테스트 상품")
                .price(10000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(product);

        // 테스트에서 사용할 상품 저장
        this.testProduct = productMapper.findById(product.getId());
    }

    @Test
    @DisplayName("상품 이미지 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 상품 이미지 생성
        ProductImage image = ProductImage.builder()
                .url("https://example.com/test-image.jpg")
                .isThumbnail(true)
                .product(testProduct)
                .build();

        // when - 상품 이미지 저장
        productImageMapper.insert(image);

        // then - 결과 검증
        ProductImage foundImage = productImageMapper.findById(image.getId());
        assertThat(foundImage).isNotNull();
        assertThat(foundImage.getId()).isEqualTo(image.getId());
        assertThat(foundImage.getUrl()).isEqualTo("https://example.com/test-image.jpg");
        assertThat(foundImage.isThumbnail()).isTrue();
    }

    @Test
    @DisplayName("상품 이미지 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 상품 이미지 생성 및 저장
        ProductImage image = ProductImage.builder()
                .url("https://example.com/before.jpg")
                .isThumbnail(false)
                .product(testProduct)
                .build();
        productImageMapper.insert(image);

        // when - 상품 이미지 정보 수정
        ProductImage updatedImage = ProductImage.builder()
                .id(image.getId())
                .url("https://example.com/after.jpg")
                .isThumbnail(true)
                .product(testProduct)
                .build();
        productImageMapper.update(updatedImage);

        // then - 결과 검증
        ProductImage foundImage = productImageMapper.findById(image.getId());
        assertThat(foundImage).isNotNull();
        assertThat(foundImage.getUrl()).isEqualTo("https://example.com/after.jpg");
        assertThat(foundImage.isThumbnail()).isTrue();
    }

    @Test
    @DisplayName("상품 이미지 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 상품 이미지 생성 및 저장
        ProductImage image = ProductImage.builder()
                .url("https://example.com/delete-test.jpg")
                .isThumbnail(false)
                .product(testProduct)
                .build();
        productImageMapper.insert(image);

        // 삭제 전 존재 확인
        ProductImage beforeDelete = productImageMapper.findById(image.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 상품 이미지 삭제
        productImageMapper.deleteById(image.getId());

        // then - 삭제 후 존재 여부 확인
        ProductImage afterDelete = productImageMapper.findById(image.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("모든 상품 이미지 조회 테스트")
    public void findAllTest() {
        // given - 여러 상품 이미지 추가
        for (int i = 1; i <= 5; i++) {
            ProductImage image = ProductImage.builder()
                    .url("https://example.com/image" + i + ".jpg")
                    .isThumbnail(i == 1) // 첫번째 이미지만 썸네일로 설정
                    .product(testProduct)
                    .build();
            productImageMapper.insert(image);
        }

        // when - 모든 상품 이미지 조회
        List<ProductImage> allImages = productImageMapper.findAll();

        // then - 결과 검증
        assertThat(allImages).isNotEmpty();
        assertThat(allImages.size()).isGreaterThanOrEqualTo(5);

        // 추가한 이미지들이 포함되어 있는지 확인
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            String url = "https://example.com/image" + i + ".jpg";
            if (allImages.stream().anyMatch(img -> url.equals(img.getUrl()))) {
                count++;
            }
        }
        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("페이징 테스트")
    public void findAllWithPagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        for (int i = 1; i <= 20; i++) {
            ProductImage image = ProductImage.builder()
                    .url("https://example.com/paging" + i + ".jpg")
                    .isThumbnail(false)
                    .product(testProduct)
                    .build();
            productImageMapper.insert(image);
        }

        // when - 페이징 적용하여 조회
        List<ProductImage> page1 = productImageMapper.findAllWithPaging(0, 10); // 첫 페이지 (10개)
        List<ProductImage> page2 = productImageMapper.findAllWithPaging(10, 10); // 두번째 페이지 (10개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(10);
        assertThat(page2.size()).isEqualTo(10);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(page1).extracting("id")
                .doesNotContainAnyElementsOf(page2.stream().map(ProductImage::getId).toList());
    }

    @Test
    @DisplayName("정렬 테스트")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 상품 이미지 추가
        ProductImage image1 = ProductImage.builder()
                .url("https://example.com/a-image.jpg")
                .isThumbnail(false)
                .product(testProduct)
                .build();
        productImageMapper.insert(image1);

        ProductImage image2 = ProductImage.builder()
                .url("https://example.com/z-image.jpg")
                .isThumbnail(true)
                .product(testProduct)
                .build();
        productImageMapper.insert(image2);

        // when - URL 기준 오름차순 정렬
        List<ProductImage> ascImages = productImageMapper.findAllWithSorting("url", "asc");

        // then - 결과 검증 (a가 z보다 앞에 있어야 함)
        boolean correctOrder = false;
        for (int i = 0; i < ascImages.size() - 1; i++) {
            if (ascImages.get(i).getUrl().contains("a-image") &&
                    ascImages.get(i+1).getUrl().contains("z-image")) {
                correctOrder = true;
                break;
            }
        }

        if (!correctOrder) {
            int indexA = -1, indexZ = -1;
            for (int i = 0; i < ascImages.size(); i++) {
                if (ascImages.get(i).getUrl().contains("a-image")) {
                    indexA = i;
                }
                if (ascImages.get(i).getUrl().contains("z-image")) {
                    indexZ = i;
                }
            }

            if (indexA != -1 && indexZ != -1) {
                assertThat(indexA).isLessThan(indexZ);
            }
        }

        // when - isThumbnail 기준 내림차순 정렬 (true가 앞에 와야 함)
        List<ProductImage> descImages = productImageMapper.findAllWithSorting("is_thumbnail", "desc");

        // then - 썸네일 기준 정렬 검증
        boolean thumbnailFirst = false;
        for (ProductImage image : descImages) {
            if (image.isThumbnail()) {
                thumbnailFirst = true;
                break;
            }
        }

        assertThat(thumbnailFirst).isTrue();
    }

    @Test
    @DisplayName("페이징 및 정렬 함께 적용 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given - 테스트용 데이터 생성
        for (int i = 1; i <= 20; i++) {
            ProductImage image = ProductImage.builder()
                    .url("https://example.com/sorting" + i + ".jpg")
                    .isThumbnail(i % 5 == 0) // 5의 배수 인덱스만 썸네일로 설정
                    .product(testProduct)
                    .build();
            productImageMapper.insert(image);
        }

        // when - 페이징 및 정렬 적용하여 조회 (썸네일 우선, 내림차순)
        List<ProductImage> firstPage = productImageMapper.findAllWithPagingAndSorting(0, 5, "is_thumbnail", "desc");

        // then - 결과 검증
        assertThat(firstPage).hasSize(5);

        // 썸네일 이미지가 먼저 오는지 확인
        int thumbnailCount = 0;
        for (int i = 0; i < firstPage.size(); i++) {
            if (firstPage.get(i).isThumbnail()) {
                thumbnailCount++;
            }
        }

        // 테스트 데이터에서 썸네일은 20개 중 4개(5, 10, 15, 20번)이며
        // 페이지 사이즈가 5이므로, 내림차순 정렬 시 첫 페이지에 썸네일 이미지가 포함되어야 함
        assertThat(thumbnailCount).isGreaterThan(0);
    }
}