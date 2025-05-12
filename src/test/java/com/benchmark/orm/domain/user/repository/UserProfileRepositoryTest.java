package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.entity.Image;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.entity.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserProfileRepository 테스트
 * <p>
 * JPA Repository를 통한 사용자 프로필 데이터 접근 테스트
 */
@DataJpaTest
public class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("사용자 프로필 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given - 테스트용 사용자 및 이미지 생성
        User user = User.builder()
                .username("프로필테스트")
                .email("profile@example.com")
                .build();
        userRepository.save(user);

        Image image = Image.builder()
                .url("https://example.com/profile-test.jpg")
                .altText("프로필 테스트 이미지")
                .build();
        imageRepository.save(image);

        // 프로필 생성
        UserProfile profile = UserProfile.builder()
                .nickname("테스트닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();

        // when - 프로필 저장
        UserProfile savedProfile = userProfileRepository.save(profile);

        // then - ID로 조회 및 결과 검증
        Optional<UserProfile> foundProfile = userProfileRepository.findById(savedProfile.getId());
        assertThat(foundProfile).isPresent();
        assertThat(foundProfile.get().getNickname()).isEqualTo("테스트닉네임");
        assertThat(foundProfile.get().getGender()).isEqualTo("남성");
        assertThat(foundProfile.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(foundProfile.get().getProfileImage().getId()).isEqualTo(image.getId());
    }

    @Test
    @DisplayName("프로필 정보 업데이트 테스트")
    public void updateTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("프로필수정")
                .email("profile-update@example.com")
                .build();
        userRepository.save(user);

        Image image = Image.builder()
                .url("https://example.com/profile-update.jpg")
                .altText("프로필 수정 테스트 이미지")
                .build();
        imageRepository.save(image);

        UserProfile profile = UserProfile.builder()
                .nickname("수정전닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileRepository.save(profile);

        // when - 프로필 정보 수정
        profile.updateInfo("수정후닉네임", "여성");
        UserProfile updatedProfile = userProfileRepository.save(profile);

        // then - 결과 검증
        assertThat(updatedProfile.getNickname()).isEqualTo("수정후닉네임");
        assertThat(updatedProfile.getGender()).isEqualTo("여성");
    }

    @Test
    @DisplayName("프로필 삭제 테스트")
    public void deleteTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("프로필삭제")
                .email("profile-delete@example.com")
                .build();
        userRepository.save(user);

        Image image = Image.builder()
                .url("https://example.com/profile-delete.jpg")
                .altText("프로필 삭제 테스트 이미지")
                .build();
        imageRepository.save(image);

        UserProfile profile = UserProfile.builder()
                .nickname("삭제테스트")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileRepository.save(profile);

        // 삭제 전 존재 확인
        assertThat(userProfileRepository.findById(profile.getId())).isPresent();

        // when - 프로필 삭제
        userProfileRepository.deleteById(profile.getId());

        // then - 삭제 후 존재 여부 확인
        assertThat(userProfileRepository.findById(profile.getId())).isEmpty();
    }

    @Test
    @DisplayName("프로필 이미지 변경 테스트")
    public void changeProfileImageTest() {
        // given - 테스트용 사용자, 이미지, 프로필 생성
        User user = User.builder()
                .username("이미지변경")
                .email("change-image@example.com")
                .build();
        userRepository.save(user);

        Image oldImage = Image.builder()
                .url("https://example.com/old-image.jpg")
                .altText("이전 이미지")
                .build();
        imageRepository.save(oldImage);

        UserProfile profile = UserProfile.builder()
                .nickname("이미지변경닉네임")
                .gender("남성")
                .user(user)
                .profileImage(oldImage)
                .build();
        userProfileRepository.save(profile);

        // 새 이미지 생성
        Image newImage = Image.builder()
                .url("https://example.com/new-image.jpg")
                .altText("새 이미지")
                .build();
        imageRepository.save(newImage);

        // when - 프로필 이미지 변경
        profile.changeProfileImage(newImage);
        UserProfile updatedProfile = userProfileRepository.save(profile);

        // then - 결과 검증
        assertThat(updatedProfile.getProfileImage().getId()).isEqualTo(newImage.getId());
        assertThat(updatedProfile.getProfileImage().getUrl()).isEqualTo("https://example.com/new-image.jpg");
    }

    @Test
    @DisplayName("사용자-프로필 양방향 관계 테스트")
    public void userProfileBidirectionalTest() {
        // given - 테스트용 사용자, 이미지 생성
        User user = User.builder()
                .username("양방향테스트")
                .email("bidirectional@example.com")
                .build();
        userRepository.save(user);

        Image image = Image.builder()
                .url("https://example.com/bi-test.jpg")
                .altText("양방향 테스트 이미지")
                .build();
        imageRepository.save(image);

        // 프로필 생성 및 사용자에 연결
        UserProfile profile = UserProfile.builder()
                .nickname("양방향닉네임")
                .gender("남성")
                .profileImage(image)
                .build();
        profile.assignUser(user); // 프로필->사용자 설정
        user.connectProfile(profile); // 사용자->프로필 설정

        userProfileRepository.save(profile);
        userRepository.save(user);

        // when - 사용자 및 프로필 조회
        User foundUser = userRepository.findById(user.getId()).orElseThrow();
        UserProfile foundProfile = userProfileRepository.findById(profile.getId()).orElseThrow();

        // then - 양방향 관계 검증
        assertThat(foundUser.getProfile().getId()).isEqualTo(profile.getId());
        assertThat(foundProfile.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("페이징 및 정렬 테스트")
    public void pagingAndSortingTest() {
        // given - 테스트용 사용자, 이미지, 프로필 여러개 생성
        int initialCount = (int) userProfileRepository.count();

        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .username("페이징" + i)
                    .email("paging" + i + "@example.com")
                    .build();
            userRepository.save(user);

            Image image = Image.builder()
                    .url("https://example.com/paging" + i + ".jpg")
                    .altText("페이징 테스트 이미지 " + i)
                    .build();
            imageRepository.save(image);

            UserProfile profile = UserProfile.builder()
                    .nickname("닉네임" + i)
                    .gender(i % 2 == 0 ? "여성" : "남성")
                    .user(user)
                    .profileImage(image)
                    .build();
            userProfileRepository.save(profile);
        }

        // when - 페이징 적용하여 조회
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "nickname"));
        Page<UserProfile> profilePage = userProfileRepository.findAll(pageRequest);

        // then - 결과 검증
        assertThat(profilePage.getContent()).isNotEmpty();
        assertThat(profilePage.getContent().size()).isEqualTo(5);
        assertThat(profilePage.getTotalElements()).isGreaterThanOrEqualTo(initialCount + 10);

        // 정렬 확인 (nickname 기준)
        List<UserProfile> content = profilePage.getContent();
        for (int i = 0; i < content.size() - 1; i++) {
            assertThat(content.get(i).getNickname().compareTo(content.get(i + 1).getNickname())).isLessThanOrEqualTo(0);
        }
    }

    @Test
    @DisplayName("사용자 삭제시 프로필 cascade 삭제 테스트")
    public void cascadeDeleteTest() {
        // given - 테스트용 사용자, 이미지, 프로필 생성
        User user = User.builder()
                .username("cascade테스트")
                .email("cascade@example.com")
                .build();
        userRepository.save(user);

        Image image = Image.builder()
                .url("https://example.com/cascade-test.jpg")
                .altText("Cascade 테스트 이미지")
                .build();
        imageRepository.save(image);

        UserProfile profile = UserProfile.builder()
                .nickname("Cascade닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileRepository.save(profile);

        // 양방향 관계 설정
        user.connectProfile(profile);
        userRepository.save(user);

        Long profileId = profile.getId();

        // when - 사용자 삭제 (cascade = CascadeType.ALL, orphanRemoval = true 설정됨)
        userRepository.delete(user);

        // then - 프로필도 함께 삭제되었는지 확인
        assertThat(userProfileRepository.findById(profileId)).isEmpty();

        // 하지만 이미지는 남아있어야 함
        assertThat(imageRepository.findById(image.getId())).isPresent();
    }
}