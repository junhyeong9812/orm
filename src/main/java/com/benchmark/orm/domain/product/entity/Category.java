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
        // 순환 참조 방지 확인
        if (this.equals(parent)) {
            throw new IllegalArgumentException("카테고리는 자기 자신을 부모로 설정할 수 없습니다.");
        }

        // 이전 부모 카테고리에서 제거
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;

        // 새 부모 카테고리에 추가
        if (parent != null) {
            parent.getChildren().add(this);
        }

        return this;
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

        this.children.add(child);
        child.changeParent(this); // 양방향 관계 설정

        return this;
    }

    /**
     * 하위 카테고리 제거
     *
     * @param child 제거할 하위 카테고리
     * @return 현재 카테고리 엔티티
     */
    public Category removeChild(Category child) {
        this.children.remove(child);
        child.changeParent(null); // 양방향 관계 해제

        return this;
    }
}