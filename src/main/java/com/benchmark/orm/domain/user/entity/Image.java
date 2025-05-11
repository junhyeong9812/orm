package com.benchmark.orm.domain.user.entity;

import com.benchmark.orm.global.entity.BaseTimeEntity;
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
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String altText;

    /**
     * 이미지 정보 업데이트
     *
     * @param url 변경할 URL
     * @param altText 변경할 대체 텍스트
     * @return 업데이트된 이미지 엔티티
     */
    public Image updateInfo(String url, String altText) {
        this.url = url;
        this.altText = altText;
        return this;
    }
}