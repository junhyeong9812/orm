package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.dto.ProductSearchDto;
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
 * ProductRepositoryCustom 구현체 테스트
 * <p>
 * ProductRepositoryCustom 인터페이스의 구현체(ProductRepositoryCustomImpl)를 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class ProductRepositoryCustomTest {

    @Autowired
    private ProductRepository productRepository; // ProductRepositoryCustom 인터페이스를 상속

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
                .name("QueryDSL 테스트 상품1")
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
                .name("QueryDSL 테스트 상품2")
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
    @DisplayName("findByName 메서드 테스트 - 상품명으로 상품 조회")
    public void findByNameTest() {
        // when - ProductRepositoryCustomImpl의 findByName 메서드 호출
        Optional<Product> foundProduct = productRepository.findByName("QueryDSL 테스트 상품1");

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("QueryDSL 테스트 상품1");
        assertThat(foundProduct.get().getPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("findByPriceBetween 메서드 테스트 - 가격 범위로 상품 조회")
    public void findByPriceBetweenTest() {
        // when - ProductRepositoryCustomImpl의 findByPriceBetween 메서드 호출
        List<Product> products = productRepository.findByPriceBetween(15000, 25000);

        // then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("일반 상품");
        assertThat(products.get(0).getPrice()).isEqualTo(20000);
    }

    @Test
    @DisplayName("findByBrandId 메서드 테스트 - 브랜드 ID로 상품 조회")
    public void findByBrandIdTest() {
        // when - ProductRepositoryCustomImpl의 findByBrandId 메서드 호출
        List<Product> products = productRepository.findByBrandId(testBrand1.getId());

        // then
        assertThat(products).hasSize(2);
        assertThat(products).extracting("name")
                .containsExactlyInAnyOrder("QueryDSL 테스트 상품1", "일반 상품");
    }

    @Test
    @DisplayName("findByCategoryId 메서드 테스트 - 카테고리 ID로 상품 조회")
    public void findByCategoryIdTest() {
        // when - ProductRepositoryCustomImpl의 findByCategoryId 메서드 호출
        List<Product> products = productRepository.findByCategoryId(testCategory1.getId());

        // then
        assertThat(products).hasSize(2);
        assertThat(products).extracting("name")
                .containsExactlyInAnyOrder("QueryDSL 테스트 상품1", "QueryDSL 테스트 상품2");
    }

    @Test
    @DisplayName("findAllWithPaging 메서드 테스트 - 페이징 적용 상품 조회")
    public void findAllWithPagingTest() {
        // when - ProductRepositoryCustomImpl의 findAllWithPaging 메서드 호출
        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());
        Page<Product> productPage = productRepository.findAllWithPaging(pageable);

        // then
        assertThat(productPage.getContent()).hasSize(2);
        assertThat(productPage.getTotalElements()).isEqualTo(3);
        assertThat(productPage.getTotalPages()).isEqualTo(2);

        // 이름 오름차순 정렬 확인
        assertThat(productPage.getContent().get(0).getName())
                .isLessThanOrEqualTo(productPage.getContent().get(1).getName());
    }

    @Test
    @DisplayName("findAllWithSorting 메서드 테스트 - 정렬 적용 상품 조회")
    public void findAllWithSortingTest() {
        // when - ProductRepositoryCustomImpl의 findAllWithSorting 메서드 호출
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        List<Product> products = productRepository.findAllWithSorting(sort);

        // then
        assertThat(products).hasSize(3);
        assertThat(products.get(0).getPrice()).isEqualTo(30000); // 가장 비싼 상품이 첫번째
        assertThat(products.get(2).getPrice()).isEqualTo(10000); // 가장 저렴한 상품이 마지막
    }

    @Test
    @DisplayName("findProductWithBrand 메서드 테스트 - 브랜드 정보와 함께 상품 조회")
    public void findProductWithBrandTest() {
        // when - ProductRepositoryCustomImpl의 findProductWithBrand 메서드 호출
        Optional<Product> productWithBrand = productRepository.findProductWithBrand(testProduct1.getId());

        // then
        assertThat(productWithBrand).isPresent();
        assertThat(productWithBrand.get().getBrand()).isNotNull();
        assertThat(productWithBrand.get().getBrand().getName()).isEqualTo("테스트 브랜드1");
    }

    @Test
    @DisplayName("findProductWithCategory 메서드 테스트 - 카테고리 정보와 함께 상품 조회")
    public void findProductWithCategoryTest() {
        // when - ProductRepositoryCustomImpl의 findProductWithCategory 메서드 호출
        Optional<Product> productWithCategory = productRepository.findProductWithCategory(testProduct1.getId());

        // then
        assertThat(productWithCategory).isPresent();
        assertThat(productWithCategory.get().getCategory()).isNotNull();
        assertThat(productWithCategory.get().getCategory().getName()).isEqualTo("테스트 카테고리1");
    }

    @Test
    @DisplayName("findProductWithImages 메서드 테스트 - 이미지 정보와 함께 상품 조회")
    public void findProductWithImagesTest() {
        // when - ProductRepositoryCustomImpl의 findProductWithImages 메서드 호출
        Optional<Product> productWithImages = productRepository.findProductWithImages(testProduct1.getId());

        // then
        assertThat(productWithImages).isPresent();
        assertThat(productWithImages.get().getImages()).isNotNull();
        assertThat(productWithImages.get().getImages()).hasSize(2);

        // 이미지 정보 확인
        boolean hasThumbnail = false;
        boolean hasNonThumbnail = false;

        for (ProductImage image : productWithImages.get().getImages()) {
            if (image.isThumbnail()) {
                hasThumbnail = true;
                assertThat(image.getUrl()).isEqualTo("https://example.com/image1.jpg");
            } else {
                hasNonThumbnail = true;
                assertThat(image.getUrl()).isEqualTo("https://example.com/image2.jpg");
            }
        }

        assertThat(hasThumbnail).isTrue();
        assertThat(hasNonThumbnail).isTrue();
    }

    @Test
    @DisplayName("findProductWithAllDetails 메서드 테스트 - 모든 상세 정보와 함께 상품 조회")
    public void findProductWithAllDetailsTest() {
        // when - ProductRepositoryCustomImpl의 findProductWithAllDetails 메서드 호출
        Optional<Product> productWithAllDetails = productRepository.findProductWithAllDetails(testProduct1.getId());

        // then
        assertThat(productWithAllDetails).isPresent();

        Product foundProduct = productWithAllDetails.get();
        assertThat(foundProduct.getName()).isEqualTo("QueryDSL 테스트 상품1");

        // 브랜드 정보 확인
        assertThat(foundProduct.getBrand()).isNotNull();
        assertThat(foundProduct.getBrand().getName()).isEqualTo("테스트 브랜드1");

        // 카테고리 정보 확인
        assertThat(foundProduct.getCategory()).isNotNull();
        assertThat(foundProduct.getCategory().getName()).isEqualTo("테스트 카테고리1");

        // 이미지 정보 확인
        assertThat(foundProduct.getImages()).isNotNull();
        assertThat(foundProduct.getImages()).hasSize(2);

        // 썸네일 이미지 확인
        ProductImage thumbnail = foundProduct.findThumbnailImage();
        assertThat(thumbnail).isNotNull();
        assertThat(thumbnail.getUrl()).isEqualTo("https://example.com/image1.jpg");
    }

    @Test
    @DisplayName("searchProducts 메서드 테스트 - 키워드 검색")
    public void searchProductsByKeywordTest() {
        // given - 키워드 검색 조건
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .keyword("QueryDSL")
                .build();

        // when - ProductRepositoryCustomImpl의 searchProducts 메서드 호출
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProducts(searchDto, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(2);
        assertThat(searchResults.getContent()).extracting("name")
                .containsExactlyInAnyOrder("QueryDSL 테스트 상품1", "QueryDSL 테스트 상품2");
    }

    @Test
    @DisplayName("searchProducts 메서드 테스트 - 가격 범위 검색")
    public void searchProductsByPriceRangeTest() {
        // given - 가격 범위 검색 조건
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .minPrice(20000)
                .maxPrice(40000)
                .build();

        // when - ProductRepositoryCustomImpl의 searchProducts 메서드 호출
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProducts(searchDto, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(2);
        assertThat(searchResults.getContent()).extracting("name")
                .containsExactlyInAnyOrder("일반 상품", "QueryDSL 테스트 상품2");
    }

    @Test
    @DisplayName("searchProducts 메서드 테스트 - 브랜드 검색")
    public void searchProductsByBrandTest() {
        // given - 브랜드 검색 조건
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .brandId(testBrand1.getId())
                .build();

        // when - ProductRepositoryCustomImpl의 searchProducts 메서드 호출
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProducts(searchDto, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(2);
        assertThat(searchResults.getContent()).extracting("name")
                .containsExactlyInAnyOrder("QueryDSL 테스트 상품1", "일반 상품");
    }

    @Test
    @DisplayName("searchProducts 메서드 테스트 - 카테고리 검색")
    public void searchProductsByCategoryTest() {
        // given - 카테고리 검색 조건
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .categoryId(testCategory2.getId())
                .build();

        // when - ProductRepositoryCustomImpl의 searchProducts 메서드 호출
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProducts(searchDto, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(1);
        assertThat(searchResults.getContent().get(0).getName()).isEqualTo("일반 상품");
    }

    @Test
    @DisplayName("searchProducts 메서드 테스트 - 복합 검색조건")
    public void searchProductsByMultipleCriteriaTest() {
        // given - 키워드 + 가격범위 + 브랜드 + 카테고리 검색 조건
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .keyword("QueryDSL")
                .minPrice(5000)
                .maxPrice(15000)
                .brandId(testBrand1.getId())
                .categoryId(testCategory1.getId())
                .build();

        // when - ProductRepositoryCustomImpl의 searchProducts 메서드 호출
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProducts(searchDto, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(1);
        assertThat(searchResults.getContent().get(0).getName()).isEqualTo("QueryDSL 테스트 상품1");
        assertThat(searchResults.getContent().get(0).getPrice()).isEqualTo(10000);
        assertThat(searchResults.getContent().get(0).getBrand().getId()).isEqualTo(testBrand1.getId());
        assertThat(searchResults.getContent().get(0).getCategory().getId()).isEqualTo(testCategory1.getId());
    }

    @Test
    @DisplayName("searchProducts 메서드 테스트 - 정렬기능")
    public void searchProductsWithSortingTest() {
        // given - 검색 조건 및 정렬 정보 (가격 내림차순)
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .keyword("QueryDSL")
                .sortBy("price")
                .sortDirection("desc")
                .build();

        // when - ProductRepositoryCustomImpl의 searchProducts 메서드 호출
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> searchResults = productRepository.searchProducts(searchDto, pageable);

        // then
        assertThat(searchResults.getContent()).hasSize(2);

        // 가격 내림차순 확인
        assertThat(searchResults.getContent().get(0).getPrice()).isEqualTo(30000); // 가장 비싼 상품이 첫번째
        assertThat(searchResults.getContent().get(1).getPrice()).isEqualTo(10000); // 가장 저렴한 상품이 두번째
    }
}