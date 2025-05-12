package com.benchmark.orm.domain.product.entity;

import com.benchmark.orm.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    /**
     * 카테고리 정보 업데이트
     *
     * @param name 변경할 카테고리명
     * @return 업데이트된 카테고리 엔티티
     */
    public Category updateInfo(String name) {
        this.name = name;
        return this;
    }

    /**
     * 부모 카테고리 변경
     *
     * @param parent 변경할 부모 카테고리
     * @return 현재 카테고리 엔티티
     */
    public Category changeParent(Category parent) {
        // 순환 참조 방지 확인 - 더 엄격한 검사 추가
        if (this.equals(parent)) {
            throw new IllegalArgumentException("카테고리는 자기 자신을 부모로 설정할 수 없습니다.");
        }

        // 추가: 부모가 현재 카테고리의 자식인지 확인 (더 깊은 순환 참조 방지)
        if (parent != null && hasChildRecursively(parent, this)) {
            throw new IllegalArgumentException("카테고리는 자기 자신을 부모로 설정할 수 없습니다.");
        }

        // 이전 부모 카테고리에서 제거
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;

        // 새 부모 카테고리에 추가
        if (parent != null && !parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }

        return this;
    }

    // 재귀적으로 순환 참조 체크하는 메서드 추가
    private boolean hasChildRecursively(Category potentialChild, Category potentialParent) {
        // 바로 아래 자식인지 확인
        if (potentialParent.getChildren().contains(potentialChild)) {
            return true;
        }

        // 자식의 자식들 검사 (재귀)
        for (Category child : potentialParent.getChildren()) {
            if (hasChildRecursively(potentialChild, child)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 하위 카테고리 추가
     *
     * @param child 추가할 하위 카테고리
     * @return 현재 카테고리 엔티티
     */
    public Category addChild(Category child) {
        // 순환 참조 방지 확인
        if (this.equals(child)) {
            throw new IllegalArgumentException("카테고리는 자기 자신을 하위 카테고리로 설정할 수 없습니다.");
        }

        // 이미 추가된 자식인지 확인하여 중복 방지
        if (!this.children.contains(child)) {
            this.children.add(child);

            // 자식의 부모가 이미 this가 아닌 경우에만 부모 변경
            if (child.getParent() != this) {
                child.setParent(this); // 내부 전용 메서드 사용
            }
        }

        return this;
    }

    /**
     * 내부용 부모 설정 메서드 (무한루프 방지)
     */
    private void setParent(Category parent) {
        this.parent = parent;
    }

    /**
     * 하위 카테고리 제거
     *
     * @param child 제거할 하위 카테고리
     * @return 현재 카테고리 엔티티
     */
    public Category removeChild(Category child) {
        if (this.children.contains(child)) {
            this.children.remove(child);
            child.setParent(null); // 내부 전용 메서드 사용
        }
        return this;
    }
}