package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import com.benchmark.orm.domain.product.entity.Brand;
import com.benchmark.orm.domain.product.entity.Category;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.entity.ProductImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 상품 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Test
    @DisplayName("상품 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 브랜드 생성 및 저장
        Brand brand = Brand.builder()
                .name("테스트 브랜드")
                .build();
        brandMapper.insert(brand);

        // 테스트용 카테고리 생성 및 저장
        Category category = Category.builder()
                .name("테스트 카테고리")
                .build();
        categoryMapper.insert(category);

        // 테스트용 상품 생성
        Product product = Product.builder()
                .name("테스트 상품")
                .price(10000)
                .brand(brand)
                .category(category)
                .build();

        // when - 상품 저장
        productMapper.insert(product);

        // then - 결과 검증
        Product foundProduct = productMapper.findById(product.getId());
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getId()).isEqualTo(product.getId());
        assertThat(foundProduct.getName()).isEqualTo("테스트 상품");
        assertThat(foundProduct.getPrice()).isEqualTo(10000);

        // 연관 엔티티 확인
        assertThat(foundProduct.getBrand()).isNotNull();
        assertThat(foundProduct.getBrand().getName()).isEqualTo("테스트 브랜드");
        assertThat(foundProduct.getCategory()).isNotNull();
        assertThat(foundProduct.getCategory().getName()).isEqualTo("테스트 카테고리");
    }

    @Test
    @DisplayName("상품 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 브랜드 및 카테고리 생성
        Brand brand1 = Brand.builder().name("원래 브랜드").build();
        brandMapper.insert(brand1);

        Brand brand2 = Brand.builder().name("새 브랜드").build();
        brandMapper.insert(brand2);

        Category category1 = Category.builder().name("원래 카테고리").build();
        categoryMapper.insert(category1);

        Category category2 = Category.builder().name("새 카테고리").build();
        categoryMapper.insert(category2);

        // 테스트용 상품 생성 및 저장
        Product product = Product.builder()
                .name("수정전")
                .price(10000)
                .brand(brand1)
                .category(category1)
                .build();
        productMapper.insert(product);

        // when - 상품 정보 수정
        Product updatedProduct = Product.builder()
                .id(product.getId())
                .name("수정후")
                .price(20000)
                .brand(brand2)
                .category(category2)
                .build();
        productMapper.update(updatedProduct);

        // then - 결과 검증
        Product foundProduct = productMapper.findById(product.getId());
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo("수정후");
        assertThat(foundProduct.getPrice()).isEqualTo(20000);
        assertThat(foundProduct.getBrand().getName()).isEqualTo("새 브랜드");
        assertThat(foundProduct.getCategory().getName()).isEqualTo("새 카테고리");
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 상품 생성 및 저장
        Brand brand = Brand.builder().name("삭제테스트 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("삭제테스트 카테고리").build();
        categoryMapper.insert(category);

        Product product = Product.builder()
                .name("삭제테스트")
                .price(10000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(product);

        // 삭제 전 존재 확인
        Product beforeDelete = productMapper.findById(product.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 상품 삭제
        productMapper.deleteById(product.getId());

        // then - 삭제 후 존재 여부 확인
        Product afterDelete = productMapper.findById(product.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("상품명으로 상품 조회 테스트")
    public void findByNameTest() {
        // given - 테스트용 상품 생성 및 저장
        Brand brand = Brand.builder().name("이름검색 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("이름검색 카테고리").build();
        categoryMapper.insert(category);

        Product product = Product.builder()
                .name("고유한상품명")
                .price(10000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(product);

        // when - 상품명으로 검색
        Product foundProduct = productMapper.findByName("고유한상품명");

        // then - 결과 검증
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getId()).isEqualTo(product.getId());
        assertThat(foundProduct.getPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("가격 범위로 상품 조회 테스트")
    public void findByPriceBetweenTest() {
        // given - 테스트용 상품 생성 및 저장
        Brand brand = Brand.builder().name("가격검색 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("가격검색 카테고리").build();
        categoryMapper.insert(category);

        // 다양한 가격대의 상품 추가
        Product product1 = Product.builder()
                .name("저가 상품")
                .price(5000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(product1);

        Product product2 = Product.builder()
                .name("중가 상품")
                .price(15000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(product2);

        Product product3 = Product.builder()
                .name("고가 상품")
                .price(25000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(product3);

        // when - 가격 범위로 검색
        List<Product> results = productMapper.findByPriceBetween(10000, 20000);

        // then - 결과 검증
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("중가 상품");
        assertThat(results.get(0).getPrice()).isEqualTo(15000);
    }

    @Test
    @DisplayName("브랜드ID로 상품 조회 테스트")
    public void findByBrandIdTest() {
        // given - 테스트용 브랜드 생성
        Brand brand1 = Brand.builder().name("브랜드A").build();
        brandMapper.insert(brand1);

        Brand brand2 = Brand.builder().name("브랜드B").build();
        brandMapper.insert(brand2);

        Category category = Category.builder().name("브랜드검색 카테고리").build();
        categoryMapper.insert(category);

        // 다른 브랜드의 상품 생성
        Product product1 = Product.builder()
                .name("브랜드A 상품1")
                .price(10000)
                .brand(brand1)
                .category(category)
                .build();
        productMapper.insert(product1);

        Product product2 = Product.builder()
                .name("브랜드A 상품2")
                .price(20000)
                .brand(brand1)
                .category(category)
                .build();
        productMapper.insert(product2);

        Product product3 = Product.builder()
                .name("브랜드B 상품")
                .price(30000)
                .brand(brand2)
                .category(category)
                .build();
        productMapper.insert(product3);

        // when - 브랜드ID로 검색
        List<Product> results = productMapper.findByBrandId(brand1.getId());

        // then - 결과 검증
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name")
                .containsExactlyInAnyOrder("브랜드A 상품1", "브랜드A 상품2");
    }

    @Test
    @DisplayName("카테고리ID로 상품 조회 테스트")
    public void findByCategoryIdTest() {
        // given - 테스트용 브랜드 및 카테고리 생성
        Brand brand = Brand.builder().name("카테고리검색 브랜드").build();
        brandMapper.insert(brand);

        Category category1 = Category.builder().name("카테고리A").build();
        categoryMapper.insert(category1);

        Category category2 = Category.builder().name("카테고리B").build();
        categoryMapper.insert(category2);

        // 다른 카테고리의 상품 생성
        Product product1 = Product.builder()
                .name("카테고리A 상품1")
                .price(10000)
                .brand(brand)
                .category(category1)
                .build();
        productMapper.insert(product1);

        Product product2 = Product.builder()
                .name("카테고리A 상품2")
                .price(20000)
                .brand(brand)
                .category(category1)
                .build();
        productMapper.insert(product2);

        Product product3 = Product.builder()
                .name("카테고리B 상품")
                .price(30000)
                .brand(brand)
                .category(category2)
                .build();
        productMapper.insert(product3);

        // when - 카테고리ID로 검색
        List<Product> results = productMapper.findByCategoryId(category1.getId());

        // then - 결과 검증
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name")
                .containsExactlyInAnyOrder("카테고리A 상품1", "카테고리A 상품2");
    }

    @Test
    @DisplayName("모든 상품 조회 테스트")
    public void findAllTest() {
        // given - 테스트용 브랜드 및 카테고리 생성
        Brand brand = Brand.builder().name("모든상품 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("모든상품 카테고리").build();
        categoryMapper.insert(category);

        // 여러 상품 추가
        for (int i = 1; i <= 5; i++) {
            Product product = Product.builder()
                    .name("전체조회" + i)
                    .price(1000 * i)
                    .brand(brand)
                    .category(category)
                    .build();
            productMapper.insert(product);
        }

        // when - 모든 상품 조회
        List<Product> allProducts = productMapper.findAll();

        // then - 결과 검증
        assertThat(allProducts).isNotEmpty();
        assertThat(allProducts.size()).isGreaterThanOrEqualTo(5);

        // 추가한 상품들이 포함되어 있는지 확인
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            String name = "전체조회" + i;
            if (allProducts.stream().anyMatch(p -> name.equals(p.getName()))) {
                count++;
            }
        }
        assertThat(count).isEqualTo(5);

        // 브랜드와 카테고리 정보가 함께 조회되는지 확인
        Product firstProduct = allProducts.stream()
                .filter(p -> p.getName().startsWith("전체조회"))
                .findFirst()
                .orElse(null);

        assertThat(firstProduct).isNotNull();
        assertThat(firstProduct.getBrand()).isNotNull();
        assertThat(firstProduct.getBrand().getName()).isEqualTo("모든상품 브랜드");
        assertThat(firstProduct.getCategory()).isNotNull();
        assertThat(firstProduct.getCategory().getName()).isEqualTo("모든상품 카테고리");
    }

    @Test
    @DisplayName("페이징 테스트")
    public void findAllWithPagingTest() {
        // given - 테스트용 데이터 생성
        Brand brand = Brand.builder().name("페이징 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("페이징 카테고리").build();
        categoryMapper.insert(category);

        // 페이징 테스트를 위한 데이터 추가
        for (int i = 1; i <= 20; i++) {
            Product product = Product.builder()
                    .name("페이징" + i)
                    .price(1000 * i)
                    .brand(brand)
                    .category(category)
                    .build();
            productMapper.insert(product);
        }

        // when - 페이징 적용하여 조회
        List<Product> page1 = productMapper.findAllWithPaging(0, 10); // 첫 페이지 (10개)
        List<Product> page2 = productMapper.findAllWithPaging(10, 10); // 두번째 페이지 (10개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(10);
        assertThat(page2.size()).isEqualTo(10);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(page1).extracting("id")
                .doesNotContainAnyElementsOf(page2.stream().map(Product::getId).toList());
    }

    @Test
    @DisplayName("정렬 테스트")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 상품 추가
        Brand brand = Brand.builder().name("정렬 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("정렬 카테고리").build();
        categoryMapper.insert(category);

        Product productA = Product.builder()
                .name("A정렬상품")
                .price(1000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(productA);

        Product productB = Product.builder()
                .name("B정렬상품")
                .price(2000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(productB);

        // when - 이름 오름차순 정렬
        List<Product> nameAscProducts = productMapper.findAllWithSorting("name", "asc");

        // then - 이름 정렬 결과 검증 (A가 B보다 앞에 있어야 함)
        boolean nameCorrectOrder = false;
        for (int i = 0; i < nameAscProducts.size() - 1; i++) {
            if ("A정렬상품".equals(nameAscProducts.get(i).getName()) &&
                    "B정렬상품".equals(nameAscProducts.get(i+1).getName())) {
                nameCorrectOrder = true;
                break;
            }
        }

        if (!nameCorrectOrder) {
            int indexA = -1, indexB = -1;
            for (int i = 0; i < nameAscProducts.size(); i++) {
                if ("A정렬상품".equals(nameAscProducts.get(i).getName())) {
                    indexA = i;
                }
                if ("B정렬상품".equals(nameAscProducts.get(i).getName())) {
                    indexB = i;
                }
            }

            if (indexA != -1 && indexB != -1) {
                assertThat(indexA).isLessThan(indexB);
            }
        }

        // when - 가격 내림차순 정렬
        List<Product> priceDescProducts = productMapper.findAllWithSorting("price", "desc");

        // then - 가격 정렬 결과 검증 (2000원 상품이 1000원 상품보다 앞에 있어야 함)
        boolean priceCorrectOrder = false;
        for (int i = 0; i < priceDescProducts.size() - 1; i++) {
            if (2000 == priceDescProducts.get(i).getPrice() &&
                    1000 == priceDescProducts.get(i+1).getPrice()) {
                priceCorrectOrder = true;
                break;
            }
        }

        if (!priceCorrectOrder && priceDescProducts.size() >= 2) {
            int index2000 = -1, index1000 = -1;
            for (int i = 0; i < priceDescProducts.size(); i++) {
                if (2000 == priceDescProducts.get(i).getPrice()) {
                    index2000 = i;
                }
                if (1000 == priceDescProducts.get(i).getPrice()) {
                    index1000 = i;
                }
            }

            if (index2000 != -1 && index1000 != -1) {
                assertThat(index2000).isLessThan(index1000);
            }
        }
    }

    @Test
    @DisplayName("페이징 및 정렬 함께 적용 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given - 테스트용 데이터 생성
        Brand brand = Brand.builder().name("페이징정렬 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("페이징정렬 카테고리").build();
        categoryMapper.insert(category);

        // 테스트 데이터 추가
        for (int i = 1; i <= 20; i++) {
            Product product = Product.builder()
                    .name("페이징정렬" + i)
                    .price(1000 * i)
                    .brand(brand)
                    .category(category)
                    .build();
            productMapper.insert(product);
        }

        // when - 페이징 및 정렬 적용하여 조회 (가격 내림차순)
        List<Product> firstPage = productMapper.findAllWithPagingAndSorting(0, 5, "price", "desc");

        // then - 결과 검증
        assertThat(firstPage).hasSize(5);

        // 가격 내림차순 확인
        for (int i = 0; i < firstPage.size() - 1; i++) {
            assertThat(firstPage.get(i).getPrice()).isGreaterThanOrEqualTo(firstPage.get(i + 1).getPrice());
        }

        // 페이징이 잘 적용되었는지 확인 (가장 비싼 5개 상품이 있어야 함)
        assertThat(firstPage.get(0).getPrice()).isGreaterThanOrEqualTo(16000);
    }

    @Test
    @DisplayName("상품과 이미지 함께 조회 테스트")
    public void findProductWithImagesTest() {
        // given - 테스트용 상품 생성
        Brand brand = Brand.builder().name("이미지테스트 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("이미지테스트 카테고리").build();
        categoryMapper.insert(category);

        Product product = Product.builder()
                .name("이미지테스트 상품")
                .price(10000)
                .brand(brand)
                .category(category)
                .build();
        productMapper.insert(product);

        // 상품 이미지 추가
        Product savedProduct = productMapper.findById(product.getId());

        ProductImage image1 = ProductImage.builder()
                .url("https://example.com/image1.jpg")
                .isThumbnail(true)
                .product(savedProduct)
                .build();
        productImageMapper.insert(image1);

        ProductImage image2 = ProductImage.builder()
                .url("https://example.com/image2.jpg")
                .isThumbnail(false)
                .product(savedProduct)
                .build();
        productImageMapper.insert(image2);

        // when - 상품과 이미지 함께 조회
        Product productWithImages = productMapper.findProductWithImages(product.getId());

        // then - 결과 검증
        assertThat(productWithImages).isNotNull();
        assertThat(productWithImages.getName()).isEqualTo("이미지테스트 상품");
        assertThat(productWithImages.getImages()).isNotNull();
        assertThat(productWithImages.getImages().size()).isEqualTo(2);

        // 이미지 정보 검증
        boolean hasThumbnail = false;
        boolean hasNonThumbnail = false;

        for (ProductImage image : productWithImages.getImages()) {
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
    @DisplayName("검색 테스트")
    public void searchProductsTest() {
        // given - 검색용 상품 추가
        Brand brand1 = Brand.builder().name("브랜드A").build();
        brandMapper.insert(brand1);

        Brand brand2 = Brand.builder().name("브랜드B").build();
        brandMapper.insert(brand2);

        Category category = Category.builder().name("검색 카테고리").build();
        categoryMapper.insert(category);

        Product product1 = Product.builder()
                .name("검색테스트A")
                .price(1000)
                .brand(brand1)
                .category(category)
                .build();
        productMapper.insert(product1);

        Product product2 = Product.builder()
                .name("일반상품")
                .price(5000)
                .brand(brand1)
                .category(category)
                .build();
        productMapper.insert(product2);

        Product product3 = Product.builder()
                .name("검색테스트B")
                .price(10000)
                .brand(brand2)
                .category(category)
                .build();
        productMapper.insert(product3);

        // when - 상품명으로 검색
        ProductSearchDto nameDto = new ProductSearchDto();
        nameDto.setKeyword("검색테스트");

        List<Product> nameResults = productMapper.searchProducts(nameDto, 0, 10, "id", "asc");

        // then - 결과 검증
        assertThat(nameResults.size()).isEqualTo(2);
        assertThat(nameResults).extracting("name")
                .containsExactlyInAnyOrder("검색테스트A", "검색테스트B");

        // when - 가격 범위로 검색
        ProductSearchDto priceDto = new ProductSearchDto();
        priceDto.setMinPrice(1000);
        priceDto.setMaxPrice(5000);

        List<Product> priceResults = productMapper.searchProducts(priceDto, 0, 10, "price", "asc");

        // then - 결과 검증
        assertThat(priceResults.size()).isEqualTo(2);
        assertThat(priceResults).extracting("price")
                .containsExactlyInAnyOrder(1000, 5000);

        // when - 브랜드로 검색
        ProductSearchDto brandDto = new ProductSearchDto();
        brandDto.setBrandId(brand1.getId());

        List<Product> brandResults = productMapper.searchProducts(brandDto, 0, 10, "name", "asc");

        // then - 결과 검증
        assertThat(brandResults.size()).isEqualTo(2);
        assertThat(brandResults).extracting("name")
                .containsExactlyInAnyOrder("검색테스트A", "일반상품");

        // when - 복합 조건 검색 (키워드 + 브랜드)
        ProductSearchDto complexDto = new ProductSearchDto();
        complexDto.setKeyword("검색테스트");
        complexDto.setBrandId(brand2.getId());

        List<Product> complexResults = productMapper.searchProducts(complexDto, 0, 10, "name", "asc");

        // then - 결과 검증
        assertThat(complexResults.size()).isEqualTo(1);
        assertThat(complexResults.get(0).getName()).isEqualTo("검색테스트B");
        assertThat(complexResults.get(0).getBrand().getId()).isEqualTo(brand2.getId());
    }

    @Test
    @DisplayName("검색 카운트 테스트")
    public void countBySearchDtoTest() {
        // given - 검색용 상품 추가
        Brand brand = Brand.builder().name("카운트 브랜드").build();
        brandMapper.insert(brand);

        Category category = Category.builder().name("카운트 카테고리").build();
        categoryMapper.insert(category);

        for (int i = 1; i <= 5; i++) {
            Product product = Product.builder()
                    .name("카운트테스트" + i)
                    .price(1000 * i)
                    .brand(brand)
                    .category(category)
                    .build();
            productMapper.insert(product);
        }

        // when - 키워드로 검색 카운트
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setKeyword("카운트테스트");

        int count = productMapper.countBySearchDto(searchDto);

        // then - 결과 검증
        assertThat(count).isEqualTo(5);

        // when - 가격 범위로 검색 카운트
        ProductSearchDto priceDto = new ProductSearchDto();
        priceDto.setMinPrice(3000);
        priceDto.setMaxPrice(4000);

        int priceCount = productMapper.countBySearchDto(priceDto);

        // then - 결과 검증
        assertThat(priceCount).isEqualTo(2); // 3000, 4000원 상품만 포함
    }
}