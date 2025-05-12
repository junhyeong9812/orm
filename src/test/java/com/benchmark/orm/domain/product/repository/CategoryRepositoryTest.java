package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.Category;
import com.benchmark.orm.domain.user.repository.UserRepositoryTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * CategoryRepository 테스트
 * <p>
 * JPA Repository를 사용한 카테고리 관련 데이터 접근 테스트
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(UserRepositoryTestConfig.class)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("카테고리 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given
        Category category = Category.builder()
                .name("테스트 카테고리")
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("테스트 카테고리");
    }

    @Test
    @DisplayName("부모-자식 관계 카테고리 저장 테스트")
    public void saveWithParentTest() {
        // given
        Category parentCategory = Category.builder()
                .name("부모 카테고리")
                .build();
        categoryRepository.save(parentCategory);

        Category childCategory = Category.builder()
                .name("자식 카테고리")
                .build();
        childCategory.changeParent(parentCategory);

        // when
        Category savedChildCategory = categoryRepository.save(childCategory);

        // then
        Optional<Category> foundChildCategory = categoryRepository.findById(savedChildCategory.getId());
        assertThat(foundChildCategory).isPresent();
        assertThat(foundChildCategory.get().getParent()).isNotNull();
        assertThat(foundChildCategory.get().getParent().getId()).isEqualTo(parentCategory.getId());
        assertThat(foundChildCategory.get().getParent().getName()).isEqualTo("부모 카테고리");
    }

    @Test
    @DisplayName("카테고리 정보 수정 테스트")
    public void updateTest() {
        // given
        Category category = Category.builder()
                .name("수정 전")
                .build();
        Category savedCategory = categoryRepository.save(category);

        // when
        savedCategory.updateInfo("수정 후");
        Category updatedCategory = categoryRepository.save(savedCategory);

        // then
        assertThat(updatedCategory.getName()).isEqualTo("수정 후");
    }

    @Test
    @DisplayName("부모 카테고리 수정 테스트")
    public void changeParentTest() {
        // given
        Category originalParent = Category.builder()
                .name("원래 부모")
                .build();
        categoryRepository.save(originalParent);

        Category newParent = Category.builder()
                .name("새 부모")
                .build();
        categoryRepository.save(newParent);

        Category child = Category.builder()
                .name("자식 카테고리")
                .build();
        child.changeParent(originalParent);
        categoryRepository.save(child);

        // when
        child.changeParent(newParent);
        Category updatedChild = categoryRepository.save(child);

        // then
        assertThat(updatedChild.getParent()).isNotNull();
        assertThat(updatedChild.getParent().getId()).isEqualTo(newParent.getId());
        assertThat(updatedChild.getParent().getName()).isEqualTo("새 부모");
    }

    @Test
    @DisplayName("순환 참조 방지 테스트")
    public void preventCircularReferenceTest() {
        // given
        Category parent = Category.builder()
                .name("부모")
                .build();
        categoryRepository.save(parent);

        Category child = Category.builder()
                .name("자식")
                .build();
        child.changeParent(parent);
        categoryRepository.save(child);

        // when & then
        assertThatThrownBy(() -> {
            parent.changeParent(child);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리는 자기 자신을 부모로 설정할 수 없습니다");
    }

    @Test
    @DisplayName("하위 카테고리 추가 테스트")
    public void addChildTest() {
        // given
        Category parent = Category.builder()
                .name("부모")
                .build();
        categoryRepository.save(parent);

        Category child = Category.builder()
                .name("자식")
                .build();
        categoryRepository.save(child);

        // when
        parent.addChild(child);
        categoryRepository.save(parent);
        categoryRepository.save(child);

        // then
        Category savedParent = categoryRepository.findById(parent.getId()).orElseThrow();
        assertThat(savedParent.getChildren()).hasSize(1);
        assertThat(savedParent.getChildren().get(0).getName()).isEqualTo("자식");

        Category savedChild = categoryRepository.findById(child.getId()).orElseThrow();
        assertThat(savedChild.getParent()).isNotNull();
        assertThat(savedChild.getParent().getId()).isEqualTo(parent.getId());
    }

    @Test
    @DisplayName("하위 카테고리 제거 테스트")
    public void removeChildTest() {
        // given
        Category parent = Category.builder()
                .name("부모")
                .build();
        categoryRepository.save(parent);

        Category child = Category.builder()
                .name("자식")
                .build();
        parent.addChild(child);
        categoryRepository.save(parent);
        categoryRepository.save(child);

        // 변경사항을 DB에 즉시 반영하고 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();

        // 초기 상태 확인 - DB에서 다시 조회
        Long parentId = parent.getId();
        Long childId = child.getId();

        parent = categoryRepository.findById(parentId).orElseThrow();
        assertThat(parent.getChildren()).hasSize(1);

        // when - 다시 조회한 객체로 작업
        parent = categoryRepository.findById(parentId).orElseThrow();
        child = categoryRepository.findById(childId).orElseThrow();

        parent.removeChild(child);
        categoryRepository.save(parent);
        categoryRepository.save(child);

        // 영속성 컨텍스트 다시 초기화
        entityManager.flush();
        entityManager.clear();

        // then - 새로 조회하여 검증
        Category updatedParent = categoryRepository.findById(parentId).orElseThrow();
        assertThat(updatedParent.getChildren()).isEmpty();

        Category updatedChild = categoryRepository.findById(childId).orElseThrow();
        assertThat(updatedChild.getParent()).isNull();
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    public void deleteTest() {
        // given
        Category category = Category.builder()
                .name("삭제 테스트")
                .build();
        Category savedCategory = categoryRepository.save(category);

        // when
        categoryRepository.delete(savedCategory);

        // then
        Optional<Category> afterDelete = categoryRepository.findById(savedCategory.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @DisplayName("모든 카테고리 조회 테스트")
    public void findAllTest() {
        // given
        Category category1 = Category.builder().name("카테고리A").build();
        Category category2 = Category.builder().name("카테고리B").build();
        Category category3 = Category.builder().name("카테고리C").build();

        categoryRepository.saveAll(List.of(category1, category2, category3));

        // when
        List<Category> categories = categoryRepository.findAll();

        // then
        assertThat(categories).hasSize(3);
        assertThat(categories).extracting("name")
                .containsExactlyInAnyOrder("카테고리A", "카테고리B", "카테고리C");
    }

    @Test
    @DisplayName("페이징 및 정렬 테스트")
    public void findAllWithPagingAndSortingTest() {
        // given
        for (int i = 1; i <= 20; i++) {
            Category category = Category.builder()
                    .name("카테고리" + i)
                    .build();
            categoryRepository.save(category);
        }

        // when - 이름 오름차순 정렬, 첫 페이지 (5개)
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());
        Page<Category> firstPage = categoryRepository.findAll(pageable);

        // then
        assertThat(firstPage.getContent()).hasSize(5);
        assertThat(firstPage.getTotalElements()).isGreaterThanOrEqualTo(20);
        assertThat(firstPage.getTotalPages()).isGreaterThanOrEqualTo(4);

        // 이름 정렬 확인
        for (int i = 0; i < firstPage.getContent().size() - 1; i++) {
            String current = firstPage.getContent().get(i).getName();
            String next = firstPage.getContent().get(i + 1).getName();
            assertThat(current.compareTo(next)).isLessThanOrEqualTo(0);
        }
    }

    @Test
    @DisplayName("계층구조 카테고리 조회 테스트")
    public void findHierarchicalCategoriesTest() {
        // given
        // 1단계 카테고리
        Category root = Category.builder().name("루트 카테고리").build();
        categoryRepository.save(root);

        // 2단계 카테고리들
        Category sub1 = Category.builder().name("서브 카테고리1").build();
        Category sub2 = Category.builder().name("서브 카테고리2").build();
        sub1.changeParent(root);
        sub2.changeParent(root);
        categoryRepository.saveAll(List.of(sub1, sub2));

        // 3단계 카테고리들
        Category sub1Child1 = Category.builder().name("서브1 자식1").build();
        Category sub1Child2 = Category.builder().name("서브1 자식2").build();
        Category sub2Child1 = Category.builder().name("서브2 자식1").build();

        sub1Child1.changeParent(sub1);
        sub1Child2.changeParent(sub1);
        sub2Child1.changeParent(sub2);

        categoryRepository.saveAll(List.of(sub1Child1, sub1Child2, sub2Child1));

        // when - 루트 카테고리 조회
        Category foundRoot = categoryRepository.findById(root.getId()).orElseThrow();

        // then
        assertThat(foundRoot.getChildren()).hasSize(2);

        // 자식 카테고리들 확인
        Optional<Category> foundSub1 = foundRoot.getChildren().stream()
                .filter(c -> c.getName().equals("서브 카테고리1"))
                .findFirst();

        assertThat(foundSub1).isPresent();
        assertThat(foundSub1.get().getChildren()).hasSize(2);

        // 손자 카테고리들 확인
        List<String> subChildNames = foundSub1.get().getChildren().stream()
                .map(Category::getName)
                .toList();

        assertThat(subChildNames).containsExactlyInAnyOrder("서브1 자식1", "서브1 자식2");
    }
}