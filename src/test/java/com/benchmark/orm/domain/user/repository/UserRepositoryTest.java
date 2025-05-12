package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.Address;
import com.benchmark.orm.domain.user.entity.Image;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.entity.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepository 테스트
 * <p>
 * JPA Repository 및 JPQL 메서드를 통한 사용자 데이터 접근 테스트
 */
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("사용자 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("리포지토리테스트")
                .email("repository@example.com")
                .build();

        // when - 사용자 저장
        User savedUser = userRepository.save(user);

        // then - ID로 조회 및 결과 검증
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("리포지토리테스트");
        assertThat(foundUser.get().getEmail()).isEqualTo("repository@example.com");
    }

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("수정전")
                .email("before@example.com")
                .build();
        userRepository.save(user);

        // when - 사용자 정보 수정
        user.updateInfo("수정후", "after@example.com");
        userRepository.save(user);

        // then - 결과 검증
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getUsername()).isEqualTo("수정후");
        assertThat(updatedUser.getEmail()).isEqualTo("after@example.com");
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    public void deleteTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("삭제테스트")
                .email("delete@example.com")
                .build();
        userRepository.save(user);

        // 삭제 전 존재 확인
        assertThat(userRepository.findById(user.getId())).isPresent();

        // when - 사용자 삭제
        userRepository.deleteById(user.getId());

        // then - 삭제 후 존재 여부 확인
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    @DisplayName("JPQL로 이메일 조회 테스트")
    public void findByEmailJpqlTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("JPQL이메일")
                .email("jpql@example.com")
                .build();
        userRepository.save(user);

        // when - JPQL로 이메일 조회
        Optional<User> foundUser = userRepository.findByEmailJpql("jpql@example.com");

        // then - 결과 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("JPQL이메일");
    }

    @Test
    @DisplayName("JPQL로 사용자명 조회 테스트")
    public void findByUsernameJpqlTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("JPQL사용자명")
                .email("jpqlusername@example.com")
                .build();
        userRepository.save(user);

        // when - JPQL로 사용자명 조회
        Optional<User> foundUser = userRepository.findByUsernameJpql("JPQL사용자명");

        // then - 결과 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("jpqlusername@example.com");
    }

    @Test
    @DisplayName("JPQL로 사용자와 프로필 함께 조회 테스트")
    public void findUserWithProfileJpqlTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("JPQL프로필")
                .email("jpqlprofile@example.com")
                .build();
        userRepository.save(user);

        Image image = Image.builder()
                .url("https://example.com/jpql-profile.jpg")
                .altText("JPQL 프로필 이미지")
                .build();
        imageRepository.save(image);

        UserProfile profile = UserProfile.builder()
                .nickname("JPQL닉네임")
                .gender("여성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileRepository.save(profile);

        // JPA 관계 설정
        user.connectProfile(profile);
        userRepository.save(user);

        // when - JPQL로 사용자와 프로필 함께 조회
        Optional<User> userWithProfile = userRepository.findUserWithProfileJpql(user.getId());

        // then - 결과 검증
        assertThat(userWithProfile).isPresent();
        assertThat(userWithProfile.get().getProfile()).isNotNull();
        assertThat(userWithProfile.get().getProfile().getNickname()).isEqualTo("JPQL닉네임");
    }

    @Test
    @DisplayName("JPQL로 사용자와 주소 함께 조회 테스트")
    public void findUserWithAddressesJpqlTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("JPQL주소")
                .email("jpqladdress@example.com")
                .build();
        userRepository.save(user);

        // 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("2000" + i)
                    .detail("JPQL 테스트 주소 " + i)
                    .isDefault(i == 1) // 첫번째 주소만 기본 주소로 설정
                    .user(user)
                    .build();
            addressRepository.save(address);

            // 양방향 관계 설정
            user.addAddress(address);
        }
        userRepository.save(user);

        // when - JPQL로 사용자와 주소 함께 조회
        Optional<User> userWithAddresses = userRepository.findUserWithAddressesJpql(user.getId());

        // then - 결과 검증
        assertThat(userWithAddresses).isPresent();
        assertThat(userWithAddresses.get().getAddresses()).hasSize(3);
    }

    @Test
    @DisplayName("JPQL 키워드로 사용자 검색 테스트")
    public void searchUsersByKeywordJpqlTest() {
        // given - 검색용 사용자 추가
        User user1 = User.builder()
                .username("검색키워드A")
                .email("search1@example.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("일반사용자")
                .email("search2@example.com")
                .build();
        userRepository.save(user2);

        // when - JPQL로 키워드 검색
        List<User> results = userRepository.searchUsersByKeywordJpql("검색");

        // then - 결과 검증
        assertThat(results).isNotEmpty();

        boolean hasMatch = false;
        for (User user : results) {
            if (user.getUsername().equals("검색키워드A")) {
                hasMatch = true;
                break;
            }
        }
        assertThat(hasMatch).isTrue();
    }

    @Test
    @DisplayName("JPQL 사용자명으로 사용자 검색 테스트")
    public void searchUsersByUsernameJpqlTest() {
        // given - 검색용 사용자 추가
        User user1 = User.builder()
                .username("검색이름A")
                .email("username1@example.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("검색이름B")
                .email("username2@example.com")
                .build();
        userRepository.save(user2);

        // when - JPQL로 사용자명 검색
        List<User> results = userRepository.searchUsersByUsernameJpql("검색이름");

        // then - 결과 검증
        assertThat(results).hasSize(2);

        int matchCount = 0;
        for (User user : results) {
            if (user.getUsername().equals("검색이름A") || user.getUsername().equals("검색이름B")) {
                matchCount++;
            }
        }
        assertThat(matchCount).isEqualTo(2);
    }

    @Test
    @DisplayName("JPQL 이메일로 사용자 검색 테스트")
    public void searchUsersByEmailJpqlTest() {
        // given - 검색용 사용자 추가
        User user1 = User.builder()
                .username("이메일A")
                .email("emailsearch1@example.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("이메일B")
                .email("emailsearch2@example.com")
                .build();
        userRepository.save(user2);

        // when - JPQL로 이메일 검색
        List<User> results = userRepository.searchUsersByEmailJpql("emailsearch");

        // then - 결과 검증
        assertThat(results).hasSize(2);

        int matchCount = 0;
        for (User user : results) {
            if (user.getEmail().equals("emailsearch1@example.com") ||
                    user.getEmail().equals("emailsearch2@example.com")) {
                matchCount++;
            }
        }
        assertThat(matchCount).isEqualTo(2);
    }

    @Test
    @DisplayName("JPQL 복합 조건으로 사용자 검색 테스트")
    public void searchUsersJpqlTest() {
        // given - 검색용 사용자 추가
        User user1 = User.builder()
                .username("복합검색A")
                .email("complex1@example.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("복합검색B")
                .email("complex2@example.com")
                .build();
        userRepository.save(user2);

        // when - JPQL 복합 조건 검색
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> results = userRepository.searchUsersJpql("복합검색", "", "", pageable);

        // then - 검색 결과 검증
        assertThat(results.getContent()).isNotEmpty();
        assertThat(results.getContent().size()).isEqualTo(2);

        int matchCount = 0;
        for (User user : results.getContent()) {
            if (user.getUsername().equals("복합검색A") || user.getUsername().equals("복합검색B")) {
                matchCount++;
            }
        }
        assertThat(matchCount).isEqualTo(2);
    }
}