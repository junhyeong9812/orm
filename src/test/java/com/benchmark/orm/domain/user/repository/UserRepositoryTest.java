package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.Address;
import com.benchmark.orm.domain.user.entity.Image;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.entity.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepository 테스트
 * <p>
 * JPA와 QueryDSL을 사용한 사용자 리포지토리 테스트
 */
@SpringBootTest
@Transactional // 테스트 후 롤백
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ImageRepository imageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("사용자 저장 테스트")
    public void saveUserTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("테스트사용자")
                .email("test@example.com")
                .build();

        // when - 사용자 저장
        User savedUser = userRepository.save(user);

        // then - 저장된 사용자 정보 검증
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("테스트사용자");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("사용자 조회 테스트 - findById")
    public void findUserByIdTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("조회테스트")
                .email("find@example.com")
                .build();
        User savedUser = userRepository.save(user);

        // when - ID로 사용자 조회
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then - 조회된 사용자 정보 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("조회테스트");
        assertThat(foundUser.get().getEmail()).isEqualTo("find@example.com");
    }

    @Test
    @DisplayName("이메일로 사용자 조회 테스트 - QueryDSL")
    public void findByEmailWithQueryDslTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("이메일테스트")
                .email("email@example.com")
                .build();
        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        // when - QueryDSL을 사용하여 이메일로 사용자 조회
        Optional<User> foundUser = userRepository.findByEmail("email@example.com");

        // then - 조회된 사용자 정보 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("이메일테스트");
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 테스트 - QueryDSL")
    public void findByUsernameWithQueryDslTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("사용자명테스트")
                .email("username@example.com")
                .build();
        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        // when - QueryDSL을 사용하여 사용자명으로 사용자 조회
        Optional<User> foundUser = userRepository.findByUsername("사용자명테스트");

        // then - 조회된 사용자 정보 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("username@example.com");
    }

    @Test
    @DisplayName("페이징 및 정렬 테스트")
    public void pagingAndSortingTest() {
        // given - 여러 사용자 생성 및 저장
        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                    .username("사용자" + i)
                    .email("user" + i + "@example.com")
                    .build();
            userRepository.save(user);
        }

        entityManager.flush();
        entityManager.clear();

        // when - 페이징 및 정렬 적용하여 사용자 조회
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "username"));
        Page<User> userPage = userRepository.findAllWithPaging(pageable);

        // then - 페이징 및 정렬 결과 검증
        assertThat(userPage.getTotalElements()).isEqualTo(20); // 전체 개수
        assertThat(userPage.getContent().size()).isEqualTo(10); // 페이지 크기

        // 사용자명 내림차순 정렬 확인
        List<User> users = userPage.getContent();
        for (int i = 0; i < users.size() - 1; i++) {
            assertThat(users.get(i).getUsername().compareTo(users.get(i + 1).getUsername())).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    @DisplayName("사용자 및 프로필 연관관계 테스트")
    public void userProfileRelationshipTest() {
        // given
        // 1. 사용자 생성 및 저장
        User user = User.builder()
                .username("프로필테스트")
                .email("profile@example.com")
                .build();
        userRepository.save(user);

        // 2. 프로필 이미지 생성 및 저장
        Image profileImage = Image.builder()
                .url("/images/profile.jpg")
                .altText("프로필 이미지")
                .build();
        imageRepository.save(profileImage);

        // 3. 사용자 프로필 생성 및 연결
        UserProfile profile = UserProfile.builder()
                .nickname("닉네임")
                .gender("남성")
                .profileImage(profileImage)
                .build();

        // 양방향 관계 설정
        user.connectProfile(profile);
        userProfileRepository.save(profile);

        entityManager.flush();
        entityManager.clear();

        // when - 프로필 정보와 함께 사용자 조회
        Optional<User> userWithProfile = userRepository.findUserWithProfile(user.getId());

        // then - 조회된 사용자 및 프로필 정보 검증
        assertThat(userWithProfile).isPresent();
        assertThat(userWithProfile.get().getProfile()).isNotNull();
        assertThat(userWithProfile.get().getProfile().getNickname()).isEqualTo("닉네임");
        assertThat(userWithProfile.get().getProfile().getGender()).isEqualTo("남성");
        assertThat(userWithProfile.get().getProfile().getProfileImage()).isNotNull();
        assertThat(userWithProfile.get().getProfile().getProfileImage().getUrl()).isEqualTo("/images/profile.jpg");
    }

    @Test
    @DisplayName("사용자 및 주소 연관관계 테스트")
    public void userAddressRelationshipTest() {
        // given
        // 1. 사용자 생성 및 저장
        User user = User.builder()
                .username("주소테스트")
                .email("address@example.com")
                .build();
        userRepository.save(user);

        // 2. 주소 생성 및 연결
        Address address1 = Address.builder()
                .zipcode("12345")
                .detail("서울시 강남구")
                .isDefault(true)
                .build();

        Address address2 = Address.builder()
                .zipcode("54321")
                .detail("서울시 서초구")
                .isDefault(false)
                .build();

        // 양방향 관계 설정
        user.addAddress(address1);
        user.addAddress(address2);

        addressRepository.save(address1);
        addressRepository.save(address2);

        entityManager.flush();
        entityManager.clear();

        // when - 주소 정보와 함께 사용자 조회
        Optional<User> userWithAddresses = userRepository.findUserWithAddresses(user.getId());

        // then - 조회된 사용자 및 주소 정보 검증
        assertThat(userWithAddresses).isPresent();
        assertThat(userWithAddresses.get().getAddresses()).hasSize(2);

        // 기본 주소 확인
        Address defaultAddress = userWithAddresses.get().findDefaultAddress();
        assertThat(defaultAddress).isNotNull();
        assertThat(defaultAddress.getDetail()).isEqualTo("서울시 강남구");
    }

    @Test
    @DisplayName("사용자 동적 검색 테스트")
    public void searchUsersTest() {
        // given - 다양한 사용자 생성 및 저장
        User user1 = User.builder().username("검색테스트A").email("search1@example.com").build();
        User user2 = User.builder().username("검색테스트B").email("search2@example.com").build();
        User user3 = User.builder().username("일반사용자").email("regular@example.com").build();
        User user4 = User.builder().username("특별사용자").email("special@search.com").build();

        userRepository.saveAll(List.of(user1, user2, user3, user4));

        entityManager.flush();
        entityManager.clear();

        // when - 키워드로 사용자 검색 (사용자명)
        UserSearchDto usernameSearchDto = UserSearchDto.builder()
                .username("검색테스트")
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> usernameResult = userRepository.searchUsers(usernameSearchDto, pageable);

        // then - 검색 결과 검증
        assertThat(usernameResult.getTotalElements()).isEqualTo(2);
        assertThat(usernameResult.getContent())
                .extracting(User::getUsername)
                .containsExactlyInAnyOrder("검색테스트A", "검색테스트B");

        // when - 키워드로 사용자 검색 (이메일)
        UserSearchDto emailSearchDto = UserSearchDto.builder()
                .email("search")
                .build();

        Page<User> emailResult = userRepository.searchUsers(emailSearchDto, pageable);

        // then - 검색 결과 검증
        assertThat(emailResult.getTotalElements()).isEqualTo(3); // search1, search2, special@search.com

        // when - 복합 조건으로 사용자 검색
        UserSearchDto combinedSearchDto = UserSearchDto.builder()
                .username("특별")
                .email("search")
                .build();

        Page<User> combinedResult = userRepository.searchUsers(combinedSearchDto, pageable);

        // then - 검색 결과 검증
        assertThat(combinedResult.getTotalElements()).isEqualTo(1);
        assertThat(combinedResult.getContent().get(0).getUsername()).isEqualTo("특별사용자");
        assertThat(combinedResult.getContent().get(0).getEmail()).isEqualTo("special@search.com");
    }
}