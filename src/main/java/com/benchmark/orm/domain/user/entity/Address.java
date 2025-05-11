package com.benchmark.orm.domain.user.entity;

import com.benchmark.orm.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zipcode;
    private String detail;
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 주소 정보 업데이트
     *
     * @param zipcode 변경할 우편번호
     * @param detail 변경할 상세주소
     * @return 업데이트된 주소 엔티티
     */
    public Address updateInfo(String zipcode, String detail) {
        this.zipcode = zipcode;
        this.detail = detail;
        return this;
    }

    /**
     * 기본 주소 설정
     *
     * @param isDefault 기본 주소 여부
     * @return 업데이트된 주소 엔티티
     */
    public Address markAsDefault(boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    /**
     * 사용자 할당
     *
     * @param user 할당할 사용자
     * @return 현재 주소 엔티티
     */
    public Address assignUser(User user) {
        this.user = user;
        return this;
    }
}