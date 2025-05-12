package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.Brand;
import com.benchmark.orm.domain.product.entity.Category;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.entity.ProductImage;
import com.benchmark.orm.domain.user.repository.UserRepositoryTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductImageRepository 테스트
 * <p>
 * JPA Repository를 사용한 상품 이미지 관련 데이터 접근 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class ProductImageRepositoryTest {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // 테스트용 브랜드 생성 및 저장
        Brand brand = Brand.builder()
                .name("이미지테스트 브랜드")
                .build();
        brandRepository.save(brand);

        // 테스트용 카테고리 생성 및 저장
        Category category = Category.builder()
                .name("이미지테스트 카테고리")
                .build();
        categoryRepository.save(category);

        // 테스트용 상품 생성 및 저장
        Product product = Product.builder()
                .name("이미지테스트 상품")
                .price(10000)
                .brand(brand)
                .category(category)
                .build();
        testProduct = productRepository.save(product);
    }

    @Test
    @DisplayName("상품 이미지 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given
        ProductImage image = ProductImage.builder()
                .url("https://example.com/test-image.jpg")
                .isThumbnail(true)
                .build();
        image.assignProduct(testProduct);

        // when
        ProductImage savedImage = productImageRepository.save(image);

        // then
        Optional<ProductImage> foundImage = productImageRepository.findById(savedImage.getId());
        assertThat(foundImage).isPresent();
        assertThat(foundImage.get().getUrl()).isEqualTo("https://example.com/test-image.jpg");
        assertThat(foundImage.get().isThumbnail()).isTrue();
        assertThat(foundImage.get().getProduct().getId()).isEqualTo(testProduct.getId());
    }

    @Test
    @DisplayName("상품 이미지 정보 수정 테스트")
    public void updateTest() {
        // given
        ProductImage image = ProductImage.builder()
                .url("https://example.com/before.jpg")
                .isThumbnail(false)
                .build();
        image.assignProduct(testProduct);
        ProductImage savedImage = productImageRepository.save(image);

        // when
        savedImage.updateInfo("https://example.com/after.jpg");
        savedImage.markAsThumbnail(true);
        ProductImage updatedImage = productImageRepository.save(savedImage);

        // then
        assertThat(updatedImage.getUrl()).isEqualTo("https://example.com/after.jpg");
        assertThat(updatedImage.isThumbnail()).isTrue();
    }

    @Test
    @DisplayName("상품 이미지 삭제 테스트")
    public void deleteTest() {
        // given
        ProductImage image = ProductImage.builder()
                .url("https://example.com/delete-test.jpg")
                .isThumbnail(false)
                .build();
        image.assignProduct(testProduct);
        ProductImage savedImage = productImageRepository.save(image);

        // when
        productImageRepository.delete(savedImage);

        // then
        Optional<ProductImage> afterDelete = productImageRepository.findById(savedImage.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("여러 상품 이미지 저장 및 조회 테스트")
    public void saveMultipleImagesTest() {
        // given
        ProductImage image1 = ProductImage.builder()
                .url("https://example.com/image1.jpg")
                .isThumbnail(true)
                .build();
        image1.assignProduct(testProduct);

        ProductImage image2 = ProductImage.builder()
                .url("https://example.com/image2.jpg")
                .isThumbnail(false)
                .build();
        image2.assignProduct(testProduct);

        ProductImage image3 = ProductImage.builder()
                .url("https://example.com/image3.jpg")
                .isThumbnail(false)
                .build();
        image3.assignProduct(testProduct);

        // when
        List<ProductImage> savedImages = productImageRepository.saveAll(List.of(image1, image2, image3));

        // then
        assertThat(savedImages).hasSize(3);

        // 상품 조회를 통한 이미지 확인 - 페치 조인 사용
        Product productWithImages = productRepository.findProductWithImagesJpql(testProduct.getId()).orElseThrow();
        assertThat(productWithImages.getImages()).hasSize(3);

        // 썸네일 이미지 확인
        ProductImage thumbnail = productWithImages.findThumbnailImage();
        assertThat(thumbnail).isNotNull();
        assertThat(thumbnail.getUrl()).isEqualTo("https://example.com/image1.jpg");
    }

    @Test
    @DisplayName("모든 상품 이미지 조회 테스트")
    public void findAllTest() {
        // given
        for (int i = 1; i <= 5; i++) {
            ProductImage image = ProductImage.builder()
                    .url("https://example.com/image" + i + ".jpg")
                    .isThumbnail(i == 1) // 첫번째 이미지만 썸네일
                    .build();
            image.assignProduct(testProduct);
            productImageRepository.save(image);
        }

        // when
        List<ProductImage> allImages = productImageRepository.findAll();

        // then
        assertThat(allImages.size()).isGreaterThanOrEqualTo(5);

        // URL 패턴으로 이미지 확인
        int matchCount = 0;
        for (ProductImage image : allImages) {
            if (image.getUrl().matches("https://example.com/image[1-5].jpg")) {
                matchCount++;
            }
        }
        assertThat(matchCount).isGreaterThanOrEqualTo(5);
    }

    @Test
    @DisplayName("페이징 및 정렬 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given
        for (int i = 1; i <= 20; i++) {
            ProductImage image = ProductImage.builder()
                    .url("https://example.com/paging" + i + ".jpg")
                    .isThumbnail(i % 5 == 0) // 5의 배수 인덱스만 썸네일
                    .build();
            image.assignProduct(testProduct);
            productImageRepository.save(image);
        }

        // when - URL 기준 내림차순 정렬, 첫 페이지 (5개)
        Pageable pageable = PageRequest.of(0, 5, Sort.by("url").descending());
        Page<ProductImage> firstPage = productImageRepository.findAll(pageable);

        // then
        assertThat(firstPage.getContent()).hasSize(5);
        assertThat(firstPage.getTotalElements()).isGreaterThanOrEqualTo(20);
        assertThat(firstPage.getTotalPages()).isGreaterThanOrEqualTo(4);

        // URL 내림차순 정렬 확인
        for (int i = 0; i < firstPage.getContent().size() - 1; i++) {
            String current = firstPage.getContent().get(i).getUrl();
            String next = firstPage.getContent().get(i + 1).getUrl();
            assertThat(current.compareTo(next)).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    @DisplayName("상품별 이미지 조회 테스트")
    public void findImagesByProductTest() {
        // given
        // 첫 번째 상품 이미지 추가
        for (int i = 1; i <= 3; i++) {
            ProductImage image = ProductImage.builder()
                    .url("https://example.com/product1-image" + i + ".jpg")
                    .isThumbnail(i == 1)
                    .build();
            image.assignProduct(testProduct);
            productImageRepository.save(image);
        }

        // 두 번째 상품 생성 및 이미지 추가
        Product product2 = Product.builder()
                .name("두번째 상품")
                .price(20000)
                .brand(testProduct.getBrand())
                .category(testProduct.getCategory())
                .build();
        productRepository.save(product2);

        for (int i = 1; i <= 2; i++) {
            ProductImage image = ProductImage.builder()
                    .url("https://example.com/product2-image" + i + ".jpg")
                    .isThumbnail(i == 1)
                    .build();
            image.assignProduct(product2);
            productImageRepository.save(image);
        }

        // when
        // 페치 조인을 통한 상품별 이미지 조회
        Product foundProduct1 = productRepository.findProductWithImagesJpql(testProduct.getId()).orElseThrow();
        Product foundProduct2 = productRepository.findProductWithImagesJpql(product2.getId()).orElseThrow();

        // then
        assertThat(foundProduct1.getImages()).hasSize(3);
        assertThat(foundProduct2.getImages()).hasSize(2);

        // URL 패턴으로 이미지 확인
        for (ProductImage image : foundProduct1.getImages()) {
            assertThat(image.getUrl()).contains("product1-image");
        }

        for (ProductImage image : foundProduct2.getImages()) {
            assertThat(image.getUrl()).contains("product2-image");
        }
    }

    @Test
    @DisplayName("썸네일 이미지 조회 테스트")
    public void findThumbnailImageTest() {
        // given
        // 여러 이미지 중 하나만 썸네일로 설정
        ProductImage image1 = ProductImage.builder()
                .url("https://example.com/non-thumbnail1.jpg")
                .isThumbnail(false)
                .build();
        image1.assignProduct(testProduct);

        ProductImage image2 = ProductImage.builder()
                .url("https://example.com/thumbnail.jpg")
                .isThumbnail(true)
                .build();
        image2.assignProduct(testProduct);

        ProductImage image3 = ProductImage.builder()
                .url("https://example.com/non-thumbnail2.jpg")
                .isThumbnail(false)
                .build();
        image3.assignProduct(testProduct);

        productImageRepository.saveAll(List.of(image1, image2, image3));

        // when
        Product productWithImages = productRepository.findProductWithImagesJpql(testProduct.getId()).orElseThrow();
        ProductImage thumbnail = productWithImages.findThumbnailImage();

        // then
        assertThat(thumbnail).isNotNull();
        assertThat(thumbnail.isThumbnail()).isTrue();
        assertThat(thumbnail.getUrl()).isEqualTo("https://example.com/thumbnail.jpg");
    }

    @Test
    @DisplayName("썸네일 이미지 변경 테스트")
    public void changeThumbnailTest() {
        // given
        // 기존 썸네일 이미지
        ProductImage originalThumbnail = ProductImage.builder()
                .url("https://example.com/original-thumbnail.jpg")
                .isThumbnail(true)
                .build();
        originalThumbnail.assignProduct(testProduct);
        originalThumbnail = productImageRepository.save(originalThumbnail);
        Long originalThumbnailId = originalThumbnail.getId();

        // 일반 이미지
        ProductImage normalImage = ProductImage.builder()
                .url("https://example.com/normal-image.jpg")
                .isThumbnail(false)
                .build();
        normalImage.assignProduct(testProduct);
        normalImage = productImageRepository.save(normalImage);
        Long normalImageId = normalImage.getId();

        // when - 일반 이미지를 썸네일로 변경
        originalThumbnail.markAsThumbnail(false);
        normalImage.markAsThumbnail(true);

        productImageRepository.save(originalThumbnail);
        productImageRepository.save(normalImage);

        // then
        Product productWithImages = productRepository.findProductWithImagesJpql(testProduct.getId()).orElseThrow();
        ProductImage newThumbnail = productWithImages.findThumbnailImage();

        assertThat(newThumbnail).isNotNull();
        assertThat(newThumbnail.getId()).isEqualTo(normalImageId);
        assertThat(newThumbnail.getUrl()).isEqualTo("https://example.com/normal-image.jpg");

        // 원래 썸네일은 일반 이미지로 변경되었는지 확인
        Optional<ProductImage> oldThumbnail = productWithImages.getImages().stream()
                .filter(i -> i.getId().equals(originalThumbnailId))
                .findFirst();

        assertThat(oldThumbnail).isPresent();
        assertThat(oldThumbnail.get().isThumbnail()).isFalse();
    }
}