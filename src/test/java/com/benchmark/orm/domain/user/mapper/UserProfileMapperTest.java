package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.Image;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.entity.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserProfileMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 사용자 프로필 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserProfileMapperTest {

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Test
    @DisplayName("사용자 프로필 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 사용자와 이미지 생성
        User user = User.builder()
                .username("프로필테스트")
                .email("profile@example.com")
                .build();
        userMapper.insert(user);

        Image image = Image.builder()
                .url("https://example.com/profile-test.jpg")
                .altText("프로필 테스트 이미지")
                .build();
        imageMapper.insert(image);

        // 프로필 생성
        UserProfile profile = UserProfile.builder()
                .nickname("테스트닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();

        // when - 프로필 저장
        userProfileMapper.insert(profile);

        // then - 결과 검증
        UserProfile savedProfile = userProfileMapper.findById(profile.getId());
        assertThat(savedProfile).isNotNull();
        assertThat(savedProfile.getNickname()).isEqualTo("테스트닉네임");
        assertThat(savedProfile.getGender()).isEqualTo("남성");
        assertThat(savedProfile.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedProfile.getProfileImage().getId()).isEqualTo(image.getId());
    }

    @Test
    @DisplayName("프로필 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("프로필수정")
                .email("profile-update@example.com")
                .build();
        userMapper.insert(user);

        Image image = Image.builder()
                .url("https://example.com/profile-update.jpg")
                .altText("프로필 수정 테스트 이미지")
                .build();
        imageMapper.insert(image);

        UserProfile profile = UserProfile.builder()
                .nickname("수정전닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileMapper.insert(profile);

        // when - 프로필 정보 수정
        profile.updateInfo("수정후닉네임", "여성");
        userProfileMapper.update(profile);

        // then - 결과 검증
        UserProfile updatedProfile = userProfileMapper.findById(profile.getId());
        assertThat(updatedProfile).isNotNull();
        assertThat(updatedProfile.getNickname()).isEqualTo("수정후닉네임");
        assertThat(updatedProfile.getGender()).isEqualTo("여성");
    }

    @Test
    @DisplayName("프로필 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("프로필삭제")
                .email("profile-delete@example.com")
                .build();
        userMapper.insert(user);

        Image image = Image.builder()
                .url("https://example.com/profile-delete.jpg")
                .altText("프로필 삭제 테스트 이미지")
                .build();
        imageMapper.insert(image);

        UserProfile profile = UserProfile.builder()
                .nickname("삭제테스트")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileMapper.insert(profile);

        // 삭제 전 존재 확인
        UserProfile beforeDelete = userProfileMapper.findById(profile.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 프로필 삭제
        userProfileMapper.deleteById(profile.getId());

        // then - 삭제 후 존재 여부 확인
        UserProfile afterDelete = userProfileMapper.findById(profile.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("사용자 ID로 프로필 조회 테스트")
    public void findByUserIdTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("프로필사용자조회")
                .email("profile-user@example.com")
                .build();
        userMapper.insert(user);

        Image image = Image.builder()
                .url("https://example.com/profile-user.jpg")
                .altText("사용자 프로필 조회 테스트 이미지")
                .build();
        imageMapper.insert(image);

        UserProfile profile = UserProfile.builder()
                .nickname("사용자조회닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileMapper.insert(profile);

        // when - 사용자 ID로 프로필 조회
        UserProfile foundProfile = userProfileMapper.findByUserId(user.getId());

        // then - 결과 검증
        assertThat(foundProfile).isNotNull();
        assertThat(foundProfile.getUser().getId()).isEqualTo(user.getId());
        assertThat(foundProfile.getNickname()).isEqualTo("사용자조회닉네임");
    }

    @Test
    @DisplayName("모든 사용자 프로필 조회 테스트")
    public void findAllTest() {
        // given - 테스트용 프로필 여러개 추가
        int initialCount = userProfileMapper.findAll().size();

        for (int i = 1; i <= 3; i++) {
            User user = User.builder()
                    .username("전체프로필" + i)
                    .email("all-profile" + i + "@example.com")
                    .build();
            userMapper.insert(user);

            Image image = Image.builder()
                    .url("https://example.com/all-profile" + i + ".jpg")
                    .altText("전체 프로필 조회 테스트 이미지 " + i)
                    .build();
            imageMapper.insert(image);

            UserProfile profile = UserProfile.builder()
                    .nickname("전체조회닉네임" + i)
                    .gender(i % 2 == 0 ? "여성" : "남성")
                    .user(user)
                    .profileImage(image)
                    .build();
            userProfileMapper.insert(profile);
        }

        // when - 모든 프로필 조회
        List<UserProfile> profiles = userProfileMapper.findAll();

        // then - 결과 검증
        assertThat(profiles).isNotEmpty();
        assertThat(profiles.size()).isEqualTo(initialCount + 3);
    }

    @Test
    @DisplayName("페이징된 사용자 프로필 조회 테스트")
    public void findAllWithPagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .username("페이징프로필" + i)
                    .email("paging-profile" + i + "@example.com")
                    .build();
            userMapper.insert(user);

            Image image = Image.builder()
                    .url("https://example.com/paging-profile" + i + ".jpg")
                    .altText("페이징 프로필 테스트 이미지 " + i)
                    .build();
            imageMapper.insert(image);

            UserProfile profile = UserProfile.builder()
                    .nickname("페이징닉네임" + i)
                    .gender(i % 2 == 0 ? "여성" : "남성")
                    .user(user)
                    .profileImage(image)
                    .build();
            userProfileMapper.insert(profile);
        }

        // when - 페이징 적용하여 조회
        List<UserProfile> page1 = userProfileMapper.findAllWithPaging(0, 5); // 첫 페이지 (5개)
        List<UserProfile> page2 = userProfileMapper.findAllWithPaging(5, 5); // 두번째 페이지 (5개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(5);
        assertThat(page2.size()).isGreaterThanOrEqualTo(5); // 이미 존재하는 프로필들이 있을 수 있어 최소 5개 이상
    }

    @Test
    @DisplayName("사용자 프로필과 이미지 정보 함께 조회 테스트")
    public void findProfileWithImageTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("프로필이미지조회")
                .email("profile-with-image@example.com")
                .build();
        userMapper.insert(user);

        Image image = Image.builder()
                .url("https://example.com/profile-with-image.jpg")
                .altText("프로필 이미지 조회 테스트 이미지")
                .build();
        imageMapper.insert(image);

        UserProfile profile = UserProfile.builder()
                .nickname("이미지조회닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileMapper.insert(profile);

        // when - 프로필과 이미지 정보 함께 조회
        UserProfile foundProfile = userProfileMapper.findProfileWithImage(profile.getId());

        // then - 결과 검증
        assertThat(foundProfile).isNotNull();
        assertThat(foundProfile.getProfileImage()).isNotNull();
        assertThat(foundProfile.getProfileImage().getUrl()).isEqualTo("https://example.com/profile-with-image.jpg");
    }

    @Test
    @DisplayName("사용자 ID로 사용자 프로필과 이미지 정보 함께 조회 테스트")
    public void findProfileWithImageByUserIdTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("사용자프로필이미지조회")
                .email("user-profile-image@example.com")
                .build();
        userMapper.insert(user);

        Image image = Image.builder()
                .url("https://example.com/user-profile-image.jpg")
                .altText("사용자 프로필 이미지 조회 테스트 이미지")
                .build();
        imageMapper.insert(image);

        UserProfile profile = UserProfile.builder()
                .nickname("사용자이미지조회닉네임")
                .gender("여성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileMapper.insert(profile);

        // when - 사용자 ID로 프로필과 이미지 정보 함께 조회
        UserProfile foundProfile = userProfileMapper.findProfileWithImageByUserId(user.getId());

        // then - 결과 검증
        assertThat(foundProfile).isNotNull();
        assertThat(foundProfile.getUser().getId()).isEqualTo(user.getId());
        assertThat(foundProfile.getProfileImage()).isNotNull();
        assertThat(foundProfile.getProfileImage().getUrl()).isEqualTo("https://example.com/user-profile-image.jpg");
    }
}