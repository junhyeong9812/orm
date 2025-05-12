package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.entity.Brand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BrandMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 브랜드 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BrandMapperTest {

    @Autowired
    private BrandMapper brandMapper;

    @Test
    @DisplayName("브랜드 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 브랜드 생성
        Brand brand = Brand.builder()
                .name("테스트 브랜드")
                .build();

        // when - 브랜드 저장 (insert 실행 후 자동 생성된 ID가 brand 객체에 설정됨)
        brandMapper.insert(brand);

        // ID 설정 확인
        assertThat(brand.getId()).isNotNull();

        // then - ID로 조회 및 결과 검증
        Brand foundBrand = brandMapper.findById(brand.getId());
        assertThat(foundBrand).isNotNull();
        assertThat(foundBrand.getId()).isEqualTo(brand.getId());
        assertThat(foundBrand.getName()).isEqualTo("테스트 브랜드");
    }

    @Test
    @DisplayName("브랜드 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 브랜드 생성 및 저장
        Brand brand = Brand.builder()
                .name("수정전")
                .build();
        brandMapper.insert(brand);

        // ID 설정 확인
        assertThat(brand.getId()).isNotNull();

        // when - 브랜드 정보 수정
        Brand updatedBrand = Brand.builder()
                .id(brand.getId())  // 저장된 Brand의 ID 사용
                .name("수정후")
                .build();
        brandMapper.update(updatedBrand);

        // then - 결과 검증
        Brand foundBrand = brandMapper.findById(brand.getId());
        assertThat(foundBrand).isNotNull();
        assertThat(foundBrand.getName()).isEqualTo("수정후");
    }

    @Test
    @DisplayName("브랜드 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 브랜드 생성 및 저장
        Brand brand = Brand.builder()
                .name("삭제테스트")
                .build();
        brandMapper.insert(brand);

        // ID 설정 확인
        assertThat(brand.getId()).isNotNull();

        // 삭제 전 존재 확인
        Brand beforeDelete = brandMapper.findById(brand.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 브랜드 삭제
        brandMapper.deleteById(brand.getId());

        // then - 삭제 후 존재 여부 확인
        Brand afterDelete = brandMapper.findById(brand.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("모든 브랜드 조회 테스트")
    public void findAllTest() {
        // given - 여러 브랜드 추가
        for (int i = 1; i <= 5; i++) {
            Brand brand = Brand.builder()
                    .name("전체조회" + i)
                    .build();
            brandMapper.insert(brand);
        }

        // when - 모든 브랜드 조회
        List<Brand> allBrands = brandMapper.findAll();

        // then - 결과 검증
        assertThat(allBrands).isNotEmpty();
        assertThat(allBrands.size()).isGreaterThanOrEqualTo(5);

        // 추가한 브랜드들이 포함되어 있는지 확인
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            String name = "전체조회" + i;
            if (allBrands.stream().anyMatch(b -> name.equals(b.getName()))) {
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
            Brand brand = Brand.builder()
                    .name("페이징" + i)
                    .build();
            brandMapper.insert(brand);
        }

        // when - 페이징 적용하여 조회
        List<Brand> page1 = brandMapper.findAllWithPaging(0, 10); // 첫 페이지 (10개)
        List<Brand> page2 = brandMapper.findAllWithPaging(10, 10); // 두번째 페이지 (10개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(10);
        assertThat(page2.size()).isEqualTo(10);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(page1).extracting("id")
                .doesNotContainAnyElementsOf(page2.stream().map(Brand::getId).toList());
    }

    @Test
    @DisplayName("정렬 테스트")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 브랜드 추가
        Brand brandA = Brand.builder()
                .name("A브랜드")
                .build();
        brandMapper.insert(brandA);

        Brand brandZ = Brand.builder()
                .name("Z브랜드")
                .build();
        brandMapper.insert(brandZ);

        // when - 오름차순 정렬
        List<Brand> ascBrands = brandMapper.findAllWithSorting("name", "asc");

        // then - 결과 검증 (A가 Z보다 앞에 있어야 함)
        boolean correctOrder = false;
        for (int i = 0; i < ascBrands.size() - 1; i++) {
            if ("A브랜드".equals(ascBrands.get(i).getName()) &&
                    "Z브랜드".equals(ascBrands.get(i+1).getName())) {
                correctOrder = true;
                break;
            }
        }

        if (!correctOrder) {
            int indexA = -1, indexZ = -1;
            for (int i = 0; i < ascBrands.size(); i++) {
                if ("A브랜드".equals(ascBrands.get(i).getName())) {
                    indexA = i;
                }
                if ("Z브랜드".equals(ascBrands.get(i).getName())) {
                    indexZ = i;
                }
            }

            if (indexA != -1 && indexZ != -1) {
                assertThat(indexA).isLessThan(indexZ);
            }
        }

        // when - 내림차순 정렬
        List<Brand> descBrands = brandMapper.findAllWithSorting("name", "desc");

        // then - 결과 검증 (Z가 A보다 앞에 있어야 함)
        boolean descCorrectOrder = false;
        for (int i = 0; i < descBrands.size() - 1; i++) {
            if ("Z브랜드".equals(descBrands.get(i).getName()) &&
                    "A브랜드".equals(descBrands.get(i+1).getName())) {
                descCorrectOrder = true;
                break;
            }
        }

        if (!descCorrectOrder) {
            int indexA = -1, indexZ = -1;
            for (int i = 0; i < descBrands.size(); i++) {
                if ("A브랜드".equals(descBrands.get(i).getName())) {
                    indexA = i;
                }
                if ("Z브랜드".equals(descBrands.get(i).getName())) {
                    indexZ = i;
                }
            }

            if (indexA != -1 && indexZ != -1) {
                assertThat(indexZ).isLessThan(indexA);
            }
        }
    }

    @Test
    @DisplayName("페이징 및 정렬 함께 적용 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given - 테스트용 데이터 생성
        for (int i = 1; i <= 20; i++) {
            Brand brand = Brand.builder()
                    .name("브랜드" + i)
                    .build();
            brandMapper.insert(brand);
        }

        // when - 페이징 및 정렬 적용하여 조회 (이름 내림차순)
        List<Brand> firstPage = brandMapper.findAllWithPagingAndSorting(0, 5, "name", "desc");

        // then - 결과 검증
        assertThat(firstPage).hasSize(5);

        // 내림차순 확인 (높은 숫자가 앞에 와야 함)
        boolean isDescending = true;
        for (int i = 0; i < firstPage.size() - 1; i++) {
            int current = extractNumber(firstPage.get(i).getName());
            int next = extractNumber(firstPage.get(i + 1).getName());
            if (current < next) {
                isDescending = false;
                break;
            }
        }

        assertThat(isDescending).isTrue();
    }

    // 브랜드명에서 숫자 추출하는 헬퍼 메서드
    private int extractNumber(String brandName) {
        String numStr = brandName.replaceAll("\\D+", "");
        return numStr.isEmpty() ? 0 : Integer.parseInt(numStr);
    }
}