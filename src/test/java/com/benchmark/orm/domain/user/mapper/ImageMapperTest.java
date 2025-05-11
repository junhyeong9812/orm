package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ImageMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 이미지 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ImageMapperTest {

    @Autowired
    private ImageMapper imageMapper;

    @Test
    @DisplayName("이미지 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 이미지 생성
        Image image = Image.builder()
                .url("https://example.com/test-image.jpg")
                .altText("테스트 이미지")
                .build();

        // when - 이미지 저장
        imageMapper.insert(image);

        // then - 결과 검증
        Image savedImage = imageMapper.findById(image.getId());
        assertThat(savedImage).isNotNull();
        assertThat(savedImage.getUrl()).isEqualTo("https://example.com/test-image.jpg");
        assertThat(savedImage.getAltText()).isEqualTo("테스트 이미지");
    }

    @Test
    @DisplayName("이미지 수정 테스트")
    public void updateTest() {
        // given - 테스트용 이미지 생성 및 저장
        Image image = Image.builder()
                .url("https://example.com/before-update.jpg")
                .altText("수정 전 이미지")
                .build();
        imageMapper.insert(image);

        // when - 이미지 정보 수정
        image.updateInfo("https://example.com/after-update.jpg", "수정 후 이미지");
        imageMapper.update(image);

        // then - 결과 검증
        Image updatedImage = imageMapper.findById(image.getId());
        assertThat(updatedImage).isNotNull();
        assertThat(updatedImage.getUrl()).isEqualTo("https://example.com/after-update.jpg");
        assertThat(updatedImage.getAltText()).isEqualTo("수정 후 이미지");
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 이미지 생성 및 저장
        Image image = Image.builder()
                .url("https://example.com/delete-test.jpg")
                .altText("삭제 테스트 이미지")
                .build();
        imageMapper.insert(image);

        // 삭제 전 존재 확인
        Image beforeDelete = imageMapper.findById(image.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 이미지 삭제
        imageMapper.deleteById(image.getId());

        // then - 삭제 후 존재 여부 확인
        Image afterDelete = imageMapper.findById(image.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("URL로 이미지 조회 테스트")
    public void findByUrlTest() {
        // given - 테스트용 이미지 생성 및 저장
        String uniqueUrl = "https://example.com/unique-image-" + System.currentTimeMillis() + ".jpg";
        Image image = Image.builder()
                .url(uniqueUrl)
                .altText("URL 조회 테스트 이미지")
                .build();
        imageMapper.insert(image);

        // when - URL로 이미지 조회
        Image foundImage = imageMapper.findByUrl(uniqueUrl);

        // then - 결과 검증
        assertThat(foundImage).isNotNull();
        assertThat(foundImage.getUrl()).isEqualTo(uniqueUrl);
        assertThat(foundImage.getAltText()).isEqualTo("URL 조회 테스트 이미지");
    }

    @Test
    @DisplayName("모든 이미지 조회 테스트")
    public void findAllTest() {
        // given - 테스트용 이미지 여러개 추가
        int initialCount = imageMapper.findAll().size();

        for (int i = 1; i <= 5; i++) {
            Image image = Image.builder()
                    .url("https://example.com/all-test-" + i + ".jpg")
                    .altText("전체조회 테스트 이미지 " + i)
                    .build();
            imageMapper.insert(image);
        }

        // when - 모든 이미지 조회
        List<Image> images = imageMapper.findAll();

        // then - 결과 검증
        assertThat(images).isNotEmpty();
        assertThat(images.size()).isEqualTo(initialCount + 5);
    }

    @Test
    @DisplayName("페이징된 이미지 조회 테스트")
    public void findAllWithPagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        for (int i = 1; i <= 10; i++) {
            Image image = Image.builder()
                    .url("https://example.com/paging-test-" + i + ".jpg")
                    .altText("페이징 테스트 이미지 " + i)
                    .build();
            imageMapper.insert(image);
        }

        // when - 페이징 적용하여 조회
        List<Image> page1 = imageMapper.findAllWithPaging(0, 5); // 첫 페이지 (5개)
        List<Image> page2 = imageMapper.findAllWithPaging(5, 5); // 두번째 페이지 (5개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(5);
        assertThat(page2.size()).isGreaterThanOrEqualTo(5); // 이미 존재하는 이미지들이 있을 수 있어 최소 5개 이상
    }
}