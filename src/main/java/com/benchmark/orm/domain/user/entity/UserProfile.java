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
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String gender;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image profileImage;

    /**
     * 프로필 정보 업데이트
     *
     * @param nickname 변경할 닉네임
     * @param gender 변경할 성별
     * @return 업데이트된 프로필 엔티티
     */
    public UserProfile updateInfo(String nickname, String gender) {
        this.nickname = nickname;
        this.gender = gender;
        return this;
    }

    /**
     * 사용자 할당
     *
     * @param user 할당할 사용자
     * @return 현재 프로필 엔티티
     */
    public UserProfile assignUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * 프로필 이미지 변경
     *
     * @param image 설정할 프로필 이미지
     * @return 현재 프로필 엔티티
     */
    public UserProfile changeProfileImage(Image image) {
        this.profileImage = image;
        return this;
    }
}