package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.entity.Image;
import com.benchmark.orm.domain.user.entity.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ImageRepository 테스트
 * <p>
 * JPA Repository를 통한 이미지 데이터 접근 테스트
 */
@DataJpaTest
@Import(UserRepositoryTestConfig.class)
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("이미지 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given - 테스트용 이미지 생성
        Image image = Image.builder()
                .url("https://example.com/test-image.jpg")
                .altText("테스트 이미지")
                .build();

        // when - 이미지 저장
        Image savedImage = imageRepository.save(image);

        // then - ID로 조회 및 결과 검증
        Optional<Image> foundImage = imageRepository.findById(savedImage.getId());
        assertThat(foundImage).isPresent();
        assertThat(foundImage.get().getUrl()).isEqualTo("https://example.com/test-image.jpg");
        assertThat(foundImage.get().getAltText()).isEqualTo("테스트 이미지");
    }

    @Test
    @DisplayName("이미지 정보 업데이트 테스트")
    public void updateTest() {
        // given - 테스트용 이미지 생성 및 저장
        Image image = Image.builder()
                .url("https://example.com/before-update.jpg")
                .altText("수정 전 이미지")
                .build();
        imageRepository.save(image);

        // when - 이미지 정보 수정
        image.updateInfo("https://example.com/after-update.jpg", "수정 후 이미지");
        Image updatedImage = imageRepository.save(image);

        // then - 결과 검증
        assertThat(updatedImage.getUrl()).isEqualTo("https://example.com/after-update.jpg");
        assertThat(updatedImage.getAltText()).isEqualTo("수정 후 이미지");
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    public void deleteTest() {
        // given - 테스트용 이미지 생성 및 저장
        Image image = Image.builder()
                .url("https://example.com/delete-test.jpg")
                .altText("삭제 테스트 이미지")
                .build();
        imageRepository.save(image);

        // 삭제 전 존재 확인
        assertThat(imageRepository.findById(image.getId())).isPresent();

        // when - 이미지 삭제
        imageRepository.deleteById(image.getId());

        // then - 삭제 후 존재 여부 확인
        assertThat(imageRepository.findById(image.getId())).isEmpty();
    }

    @Test
    @DisplayName("여러 이미지 저장 및 조회 테스트")
    public void saveAllAndFindAllTest() {
        // given - 테스트용 이미지 여러개 생성
        Image image1 = Image.builder()
                .url("https://example.com/batch-image1.jpg")
                .altText("일괄 테스트 이미지1")
                .build();

        Image image2 = Image.builder()
                .url("https://example.com/batch-image2.jpg")
                .altText("일괄 테스트 이미지2")
                .build();

        Image image3 = Image.builder()
                .url("https://example.com/batch-image3.jpg")
                .altText("일괄 테스트 이미지3")
                .build();

        List<Image> images = Arrays.asList(image1, image2, image3);

        // when - 이미지 일괄 저장
        List<Image> savedImages = imageRepository.saveAll(images);

        // then - 결과 검증
        assertThat(savedImages).hasSize(3);

        // 모든 이미지 조회
        List<Image> allImages = imageRepository.findAll();
        assertThat(allImages.size()).isGreaterThanOrEqualTo(3);

        // 저장한 이미지들이 모두 포함되어 있는지 확인
        int matchCount = 0;
        for (Image savedImage : savedImages) {
            for (Image image : allImages) {
                if (savedImage.getId().equals(image.getId())) {
                    matchCount++;
                    break;
                }
            }
        }
        assertThat(matchCount).isEqualTo(3);
    }

    @Test
    @DisplayName("정렬된 이미지 조회 테스트")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 이미지 추가
        Image imageA = Image.builder()
                .url("https://example.com/A-image.jpg")
                .altText("A 정렬 테스트 이미지")
                .build();
        imageRepository.save(imageA);

        Image imageZ = Image.builder()
                .url("https://example.com/Z-image.jpg")
                .altText("Z 정렬 테스트 이미지")
                .build();
        imageRepository.save(imageZ);

        // when - URL 기준으로 오름차순 정렬하여 조회
        Sort sort = Sort.by(Sort.Direction.ASC, "url");
        List<Image> sortedImages = imageRepository.findAll(sort);

        // then - 결과 검증 (A로 시작하는 URL이 Z로 시작하는 URL보다 앞에 위치해야 함)
        int indexA = -1, indexZ = -1;
        for (int i = 0; i < sortedImages.size(); i++) {
            if (sortedImages.get(i).getUrl().contains("/A-image")) {
                indexA = i;
            }
            if (sortedImages.get(i).getUrl().contains("/Z-image")) {
                indexZ = i;
            }
        }

        // A가 Z보다 앞에 있는지 확인
        if (indexA != -1 && indexZ != -1) {
            assertThat(indexA).isLessThan(indexZ);
        }
    }

    @Test
    @DisplayName("페이징된 이미지 조회 테스트")
    public void pagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        int initialCount = (int) imageRepository.count();

        for (int i = 1; i <= 10; i++) {
            Image image = Image.builder()
                    .url("https://example.com/paging-image" + i + ".jpg")
                    .altText("페이징 테스트 이미지 " + i)
                    .build();
            imageRepository.save(image);
        }

        // when - 페이징 적용하여 조회
        PageRequest pageRequest = PageRequest.of(0, 5); // 첫 페이지 (5개)
        Page<Image> imagePage = imageRepository.findAll(pageRequest);

        // then - 결과 검증
        assertThat(imagePage.getContent()).isNotEmpty();
        assertThat(imagePage.getContent().size()).isEqualTo(5);
        assertThat(imagePage.getTotalElements()).isGreaterThanOrEqualTo(initialCount + 10);
    }

    @Test
    @DisplayName("이미지와 프로필 연결 테스트")
    public void imageProfileRelationTest() {
        // given - 테스트용 이미지 생성 및 저장
        Image image = Image.builder()
                .url("https://example.com/profile-test.jpg")
                .altText("프로필 테스트 이미지")
                .build();
        imageRepository.save(image);

        // 프로필 생성 및 이미지 연결
        UserProfile profile = UserProfile.builder()
                .nickname("이미지테스트")
                .gender("남성")
                .profileImage(image)
                .build();
        userProfileRepository.save(profile);

        // when - 프로필에서 이미지 참조 제거
        profile.changeProfileImage(null); // 이미지 참조 제거
        userProfileRepository.save(profile);

        // 영속성 컨텍스트 갱신
        entityManager.flush();
        entityManager.clear();

        // then - 이미지 삭제 확인 및 프로필 상태 확인
        // orphanRemoval=true 설정으로 인해 이미지가 자동 삭제되어야 함
        assertThat(imageRepository.findById(image.getId())).isEmpty();

        // 프로필은 남아있지만 이미지 참조가 null이 되었는지 확인
        Optional<UserProfile> updatedProfile = userProfileRepository.findById(profile.getId());
        assertThat(updatedProfile).isPresent();
        assertThat(updatedProfile.get().getProfileImage()).isNull();
    }

    @Test
    @DisplayName("이미지 존재 여부 확인 테스트")
    public void existsByIdTest() {
        // given - 테스트용 이미지 생성 및 저장
        Image image = Image.builder()
                .url("https://example.com/exists-test.jpg")
                .altText("존재 테스트 이미지")
                .build();
        imageRepository.save(image);

        // when & then - 존재 여부 확인
        assertThat(imageRepository.existsById(image.getId())).isTrue();
        assertThat(imageRepository.existsById(999L)).isFalse(); // 존재하지 않는 ID
    }
}