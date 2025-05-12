package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CategoryMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 카테고리 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("카테고리 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 카테고리 생성
        Category category = Category.builder()
                .name("테스트 카테고리")
                .build();

        // when - 카테고리 저장
        categoryMapper.insert(category);

        // ID 설정 확인
        assertThat(category.getId()).isNotNull();

        // then - 결과 검증
        Category foundCategory = categoryMapper.findById(category.getId());
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getId()).isEqualTo(category.getId());
        assertThat(foundCategory.getName()).isEqualTo("테스트 카테고리");
    }

    @Test
    @DisplayName("부모-자식 관계 카테고리 저장 테스트")
    public void insertWithParentTest() {
        // given - 부모 카테고리 생성 및 저장
        Category parentCategory = Category.builder()
                .name("부모 카테고리")
                .build();
        categoryMapper.insert(parentCategory);

        // ID 설정 확인
        assertThat(parentCategory.getId()).isNotNull();

        // 자식 카테고리 생성
        Category childCategory = Category.builder()
                .name("자식 카테고리")
                .parent(parentCategory)
                .build();

        // when - 자식 카테고리 저장
        categoryMapper.insert(childCategory);

        // ID 설정 확인
        assertThat(childCategory.getId()).isNotNull();

        // then - 결과 검증
        Category foundChildCategory = categoryMapper.findById(childCategory.getId());
        assertThat(foundChildCategory).isNotNull();
        assertThat(foundChildCategory.getName()).isEqualTo("자식 카테고리");

        // 부모 카테고리 확인
        assertThat(foundChildCategory.getParent()).isNotNull();
        assertThat(foundChildCategory.getParent().getId()).isEqualTo(parentCategory.getId());
        assertThat(foundChildCategory.getParent().getName()).isEqualTo("부모 카테고리");
    }

    @Test
    @DisplayName("카테고리 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 카테고리 생성 및 저장
        Category category = Category.builder()
                .name("수정전")
                .build();
        categoryMapper.insert(category);

        // ID 설정 확인
        assertThat(category.getId()).isNotNull();

        // when - 카테고리 정보 수정
        Category updatedCategory = Category.builder()
                .id(category.getId())
                .name("수정후")
                .build();
        categoryMapper.update(updatedCategory);

        // then - 결과 검증
        Category foundCategory = categoryMapper.findById(category.getId());
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("수정후");
    }

    @Test
    @DisplayName("부모 카테고리 수정 테스트")
    public void updateParentTest() {
        // given - 카테고리 생성 및 저장
        Category originalParent = Category.builder()
                .name("원래 부모")
                .build();
        categoryMapper.insert(originalParent);
        assertThat(originalParent.getId()).isNotNull();

        Category newParent = Category.builder()
                .name("새 부모")
                .build();
        categoryMapper.insert(newParent);
        assertThat(newParent.getId()).isNotNull();

        Category child = Category.builder()
                .name("자식 카테고리")
                .parent(originalParent)
                .build();
        categoryMapper.insert(child);
        assertThat(child.getId()).isNotNull();

        // 초기 상태 확인
        Category initialChild = categoryMapper.findById(child.getId());
        assertThat(initialChild.getParent()).isNotNull();
        assertThat(initialChild.getParent().getId()).isEqualTo(originalParent.getId());

        // when - 부모 카테고리 수정
        Category updatedChild = Category.builder()
                .id(child.getId())
                .name(child.getName())
                .parent(newParent)
                .build();
        categoryMapper.update(updatedChild);

        // then - 결과 검증
        Category foundChild = categoryMapper.findById(child.getId());
        assertThat(foundChild).isNotNull();
        assertThat(foundChild.getParent()).isNotNull();
        assertThat(foundChild.getParent().getId()).isEqualTo(newParent.getId());
        assertThat(foundChild.getParent().getName()).isEqualTo("새 부모");
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 카테고리 생성 및 저장
        Category category = Category.builder()
                .name("삭제테스트")
                .build();
        categoryMapper.insert(category);
        assertThat(category.getId()).isNotNull();

        // 삭제 전 존재 확인
        Category beforeDelete = categoryMapper.findById(category.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 카테고리 삭제
        categoryMapper.deleteById(category.getId());

        // then - 삭제 후 존재 여부 확인
        Category afterDelete = categoryMapper.findById(category.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("모든 카테고리 조회 테스트")
    public void findAllTest() {
        // given - 여러 카테고리 추가
        for (int i = 1; i <= 5; i++) {
            Category category = Category.builder()
                    .name("전체조회" + i)
                    .build();
            categoryMapper.insert(category);
            assertThat(category.getId()).isNotNull();
        }

        // when - 모든 카테고리 조회
        List<Category> allCategories = categoryMapper.findAll();

        // then - 결과 검증
        assertThat(allCategories).isNotEmpty();
        assertThat(allCategories.size()).isGreaterThanOrEqualTo(5);

        // 추가한 카테고리들이 포함되어 있는지 확인
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            String name = "전체조회" + i;
            if (allCategories.stream().anyMatch(c -> name.equals(c.getName()))) {
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
            Category category = Category.builder()
                    .name("페이징" + i)
                    .build();
            categoryMapper.insert(category);
            assertThat(category.getId()).isNotNull();
        }

        // when - 페이징 적용하여 조회
        List<Category> page1 = categoryMapper.findAllWithPaging(0, 10); // 첫 페이지 (10개)
        List<Category> page2 = categoryMapper.findAllWithPaging(10, 10); // 두번째 페이지 (10개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(10);
        assertThat(page2.size()).isEqualTo(10);

        // 페이지별로 다른 데이터가 조회되는지 확인
        assertThat(page1).extracting("id")
                .doesNotContainAnyElementsOf(page2.stream().map(Category::getId).toList());
    }

    @Test
    @DisplayName("정렬 테스트")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 카테고리 추가
        Category categoryA = Category.builder()
                .name("A카테고리")
                .build();
        categoryMapper.insert(categoryA);
        assertThat(categoryA.getId()).isNotNull();

        Category categoryZ = Category.builder()
                .name("Z카테고리")
                .build();
        categoryMapper.insert(categoryZ);
        assertThat(categoryZ.getId()).isNotNull();

        // when - 오름차순 정렬
        List<Category> ascCategories = categoryMapper.findAllWithSorting("name", "asc");

        // then - 결과 검증 (A가 Z보다 앞에 있어야 함)
        boolean correctOrder = false;
        for (int i = 0; i < ascCategories.size() - 1; i++) {
            if ("A카테고리".equals(ascCategories.get(i).getName()) &&
                    "Z카테고리".equals(ascCategories.get(i+1).getName())) {
                correctOrder = true;
                break;
            }
        }

        if (!correctOrder) {
            int indexA = -1, indexZ = -1;
            for (int i = 0; i < ascCategories.size(); i++) {
                if ("A카테고리".equals(ascCategories.get(i).getName())) {
                    indexA = i;
                }
                if ("Z카테고리".equals(ascCategories.get(i).getName())) {
                    indexZ = i;
                }
            }

            if (indexA != -1 && indexZ != -1) {
                assertThat(indexA).isLessThan(indexZ);
            }
        }
    }

    @Test
    @DisplayName("페이징 및 정렬 함께 적용 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given - 테스트용 데이터 생성
        for (int i = 1; i <= 20; i++) {
            Category category = Category.builder()
                    .name("카테고리" + i)
                    .build();
            categoryMapper.insert(category);
            assertThat(category.getId()).isNotNull();
        }

        // when - 페이징 및 정렬 적용하여 조회 (이름 내림차순)
        List<Category> firstPage = categoryMapper.findAllWithPagingAndSorting(0, 5, "name", "desc");

        // then - 결과 검증
        assertThat(firstPage).hasSize(5);

        // 내림차순 확인
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

    // 카테고리명에서 숫자 추출하는 헬퍼 메서드
    private int extractNumber(String categoryName) {
        String numStr = categoryName.replaceAll("\\D+", "");
        return numStr.isEmpty() ? 0 : Integer.parseInt(numStr);
    }
}