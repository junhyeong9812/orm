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
 * ProductRepository 테스트
 * <p>
 * JpaRepository 및 JPQL 메서드를 통한 상품 데이터 접근 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    private Brand testBrand1;
    private Brand testBrand2;
    private Category testCategory1;
    private Category testCategory2;
    private Product testProduct1;
    private Product testProduct2;
    private Product testProduct3;

    @BeforeEach
    void setUp() {
        // 테스트용 브랜드 생성
        testBrand1 = Brand.builder().name("테스트 브랜드1").build();
        testBrand2 = Brand.builder().name("테스트 브랜드2").build();
        brandRepository.saveAll(List.of(testBrand1, testBrand2));

        // 테스트용 카테고리 생성
        testCategory1 = Category.builder().name("테스트 카테고리1").build();
        testCategory2 = Category.builder().name("테스트 카테고리2").build();
        categoryRepository.saveAll(List.of(testCategory1, testCategory2));

        // 테스트용 상품 생성
        testProduct1 = Product.builder()
                .name("JPQL 테스트 상품1")
                .price(10000)
                .brand(testBrand1)
                .category(testCategory1)
                .build();

        testProduct2 = Product.builder()
                .name("일반 상품")
                .price(20000)
                .brand(testBrand1)
                .category(testCategory2)
                .build();

        testProduct3 = Product.builder()
                .name("JPQL 테스트 상품2")
                .price(30000)
                .brand(testBrand2)
                .category(testCategory1)
                .build();

        productRepository.saveAll(List.of(testProduct1, testProduct2, testProduct3));

        // 상품 이미지 추가
        ProductImage image1 = ProductImage.builder()
                .url("https://example.com/image1.jpg")
                .isThumbnail(true)
                .build();
        image1.assignProduct(testProduct1);

        ProductImage image2 = ProductImage.builder()
                .url("https://example.com/image2.jpg")
                .isThumbnail(false)
                .build();
        image2.assignProduct(testProduct1);

        ProductImage image3 = ProductImage.builder()
                .url("https://example.com/image3.jpg")
                .isThumbnail(true)
                .build();
        image3.assignProduct(testProduct3);

        productImageRepository.saveAll(List.of(image1, image2, image3));
    }

    @Test
    @DisplayName("JpaRepository 기본 메서드 - findById 테스트")
    public void findByIdTest() {
        // when
        Optional<Product> foundProduct = productRepository.findById(testProduct1.getId());

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("JPQL 테스트 상품1");
        assertThat(foundProduct.get().getPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("JpaRepository 기본 메서드 - findAll 테스트")
    public void findAllTest() {
        // when
        List<Product> allProducts = productRepository.findAll();

        // then
        assertThat(allProducts).hasSize(3);
        assertThat(allProducts).extracting("name")
                .containsExactlyInAnyOrder("JPQL 테스트 상품1", "일반 상품", "JPQL 테스트 상품2");
    }

    @Test
    @DisplayName("JpaRepository 기본 메서드 - 정렬 테스트")
    public void findAllWithSortTest() {
        // when
        List<Product> sortedProducts = productRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));

        // then
        assertThat(sortedProducts).hasSize(3);
        assertThat(sortedProducts.get(0).getPrice()).isEqualTo(30000); // 가장 비싼 상품
        assertThat(sortedProducts.get(2).getPrice()).isEqualTo(10000); // 가장 저렴한 상품
    }

    @Test
    @DisplayName("JpaRepository 기본 메서드 - 페이징 테스트")
    public void findAllWithPageableTest() {
        // when
        Page<Product> productPage = productRepository.findAll(PageRequest.of(0, 2, Sort.by("name").ascending()));

        // then
        assertThat(productPage.getContent()).hasSize(2);
        assertThat(productPage.getTotalElements()).isEqualTo(3);
        assertThat(productPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("JpaRepository 기본 메서드 - save 테스트")
    public void saveTest() {
        // given
        Product newProduct = Product.builder()
                .name("새 상품")
                .price(50000)
                .brand(testBrand1)
                .category(testCategory1)
                .build();

        // when
        Product savedProduct = productRepository.save(newProduct);

        // then
        assertThat(savedProduct.getId()).isNotNull();

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("새 상품");
        assertThat(foundProduct.get().getPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("JpaRepository 기본 메서드 - delete 테스트")
    public void deleteTest() {
        // when
        productRepository.delete(testProduct1);

        // then
        Optional<Product> afterDelete = productRepository.findById(testProduct1.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("JPQL - findByNameJpql 테스트")
    public void findByNameJpqlTest() {
        // when
        Optional<Product> foundProduct = productRepository.findByNameJpql("JPQL 테스트 상품1");

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("JPQL 테스트 상품1");
        assertThat(foundProduct.get().getPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("JPQL - findByPriceBetweenJpql 테스트")
    public void findByPriceBetweenJpqlTest() {
        // when
        List<Product> products = productRepository.findByPriceBetweenJpql(15000, 25000);

        // then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("일반 상품");
        assertThat(products.get(0).getPrice()).isEqualTo(20000);
    }

    @Test
    @DisplayName("JPQL - findByBrandIdJpql 테스트")
    public void findByBrandIdJpqlTest() {
        // when
        List<Product> products = productRepository.findByBrandIdJpql(testBrand1.getId());

        // then
        assertThat(products).hasSize(2);
        assertThat(products).extracting("name")
                .containsExactlyInAnyOrder("JPQL 테스트 상품1", "일반 상품");
    }

    @Test
    @DisplayName("JPQL - findByCategoryIdJpql 테스트")
    public void findByCategoryIdJpqlTest() {
        // when
        List<Product> products = productRepository.findByCategoryIdJpql(testCategory1.getId());

        // then
        assertThat(products).hasSize(2);
        assertThat(products).extracting("name")
                .containsExactlyInAnyOrder("JPQL 테스트 상품1", "JPQL 테스트 상품2");
    }

    @Test
    @DisplayName("JPQL - findProductWithBrandJpql 테스트")
    public void findProductWithBrandJpqlTest() {
        // when
        Optional<Product> productWithBrand = productRepository.findProductWithBrandJpql(testProduct1.getId());

        // then
        assertThat(productWithBrand).isPresent();
        assertThat(productWithBrand.get().getBrand()).isNotNull();
        assertThat(productWithBrand.get().getBrand().getName()).isEqualTo("테스트 브랜드1");
    }

    @Test
    @DisplayName("JPQL - findProductWithCategoryJpql 테스트")
    public void findProductWithCategoryJpqlTest() {
        // when
        Optional<Product> productWithCategory = productRepository.findProductWithCategoryJpql(testProduct1.getId());

        // then
        assertThat(productWithCategory).isPresent();
        assertThat(productWithCategory.get().getCategory()).isNotNull();
        assertThat(productWithCategory.get().getCategory().getName()).isEqualTo("테스트 카테고리1");
    }

    @Test
    @DisplayName("JPQL - findProductWithImagesJpql 테스트")
    public void findProductWithImagesJpqlTest() {
        // when
        Optional<Product> productWithImages = productRepository.findProductWithImagesJpql(testProduct1.getId());

        // then
        assertThat(productWithImages).isPresent();
        assertThat(productWithImages.get().getImages()).isNotNull();
        assertThat(productWithImages.get().getImages()).hasSize(2);

        // 썸네일 이미지 확인
        boolean hasThumbnail = productWithImages.get().getImages().stream()
                .anyMatch(ProductImage::isThumbnail);
        assertThat(hasThumbnail).isTrue();
    }

    @Test
    @DisplayName("JPQL - searchProductsByKeywordJpql 테스트")
    public void searchProductsByKeywordJpqlTest() {
        // when
        List<Product> searchResults = productRepository.searchProductsByKeywordJpql("JPQL");

        // then
        assertThat(searchResults).hasSize(2);
        assertThat(searchResults).extracting("name")
                .containsExactlyInAnyOrder("JPQL 테스트 상품1", "JPQL 테스트 상품2");
    }

    @Test
    @DisplayName("JPQL - searchProductsJpql 키워드 검색 테스트")
    public void searchProductsJpqlWithKeywordTest() {
        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProductsJpql(
                "JPQL", null, null, null, null, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(2);
        assertThat(searchResults.getContent()).extracting("name")
                .containsExactlyInAnyOrder("JPQL 테스트 상품1", "JPQL 테스트 상품2");
    }

    @Test
    @DisplayName("JPQL - searchProductsJpql 가격 범위 검색 테스트")
    public void searchProductsJpqlWithPriceRangeTest() {
        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProductsJpql(
                null, 20000, 40000, null, null, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(2);
        assertThat(searchResults.getContent()).extracting("name")
                .containsExactlyInAnyOrder("일반 상품", "JPQL 테스트 상품2");
    }

    @Test
    @DisplayName("JPQL - searchProductsJpql 브랜드 검색 테스트")
    public void searchProductsJpqlWithBrandTest() {
        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProductsJpql(
                null, null, null, testBrand2.getId(), null, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(1);
        assertThat(searchResults.getContent().get(0).getName()).isEqualTo("JPQL 테스트 상품2");
    }

    @Test
    @DisplayName("JPQL - searchProductsJpql 카테고리 검색 테스트")
    public void searchProductsJpqlWithCategoryTest() {
        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProductsJpql(
                null, null, null, null, testCategory2.getId(), pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(1);
        assertThat(searchResults.getContent().get(0).getName()).isEqualTo("일반 상품");
    }

    @Test
    @DisplayName("JPQL - searchProductsJpql 복합 검색 테스트")
    public void searchProductsJpqlWithMultipleCriteriaTest() {
        // when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("price").descending());
        Page<Product> searchResults = productRepository.searchProductsJpql(
                "JPQL", 5000, 15000, testBrand1.getId(), testCategory1.getId(), pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(1);
        assertThat(searchResults.getContent().get(0).getName()).isEqualTo("JPQL 테스트 상품1");
        assertThat(searchResults.getContent().get(0).getPrice()).isEqualTo(10000);
    }
}