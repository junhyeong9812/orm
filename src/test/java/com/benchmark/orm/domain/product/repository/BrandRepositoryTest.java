package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.Brand;
import com.benchmark.orm.domain.user.repository.UserRepositoryTestConfig;
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
 * BrandRepository 테스트
 * <p>
 * JPA Repository를 사용한 브랜드 관련 데이터 접근 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @DisplayName("브랜드 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given
        Brand brand = Brand.builder()
                .name("테스트 브랜드")
                .build();

        // when
        Brand savedBrand = brandRepository.save(brand);

        // then
        Optional<Brand> foundBrand = brandRepository.findById(savedBrand.getId());
        assertThat(foundBrand).isPresent();
        assertThat(foundBrand.get().getName()).isEqualTo("테스트 브랜드");
    }

    @Test
    @DisplayName("브랜드 정보 수정 테스트")
    public void updateTest() {
        // given
        Brand brand = Brand.builder()
                .name("수정 전")
                .build();
        Brand savedBrand = brandRepository.save(brand);

        // when
        savedBrand.updateInfo("수정 후");
        Brand updatedBrand = brandRepository.save(savedBrand);

        // then
        assertThat(updatedBrand.getName()).isEqualTo("수정 후");
    }

    @Test
    @DisplayName("브랜드 삭제 테스트")
    public void deleteTest() {
        // given
        Brand brand = Brand.builder()
                .name("삭제 테스트")
                .build();
        Brand savedBrand = brandRepository.save(brand);

        // when
        brandRepository.delete(savedBrand);

        // then
        Optional<Brand> afterDelete = brandRepository.findById(savedBrand.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("모든 브랜드 조회 테스트")
    public void findAllTest() {
        // given
        Brand brand1 = Brand.builder().name("브랜드A").build();
        Brand brand2 = Brand.builder().name("브랜드B").build();
        Brand brand3 = Brand.builder().name("브랜드C").build();

        brandRepository.saveAll(List.of(brand1, brand2, brand3));

        // when
        List<Brand> brands = brandRepository.findAll();

        // then
        assertThat(brands).hasSize(3);
        assertThat(brands).extracting("name")
                .containsExactlyInAnyOrder("브랜드A", "브랜드B", "브랜드C");
    }

    @Test
    @DisplayName("페이징 및 정렬 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given
        for (int i = 1; i <= 20; i++) {
            Brand brand = Brand.builder()
                    .name("JPA브랜드" + i)
                    .build();
            brandRepository.save(brand);
        }

        // when - 이름 오름차순 정렬, 첫 페이지 (5개)
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());
        Page<Brand> firstPage = brandRepository.findAll(pageable);

        // then
        assertThat(firstPage.getContent()).hasSize(5);
        assertThat(firstPage.getTotalElements()).isGreaterThanOrEqualTo(20);
        assertThat(firstPage.getTotalPages()).isGreaterThanOrEqualTo(4);

        // 이름 정렬 확인
        boolean isAscending = true;
        for (int i = 0; i < firstPage.getContent().size() - 1; i++) {
            if (firstPage.getContent().get(i).getName().compareTo(
                    firstPage.getContent().get(i + 1).getName()) > 0) {
                isAscending = false;
                break;
            }
        }

        assertThat(isAscending).isTrue();
    }
}