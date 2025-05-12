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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepositoryCustom 구현체 테스트
 * <p>
 * QueryDSL 커스텀 구현체를 통한 사용자 데이터 접근 테스트
 */
@DataJpaTest
@Import(UserRepositoryTestConfig.class) // QueryDSL 설정을 위한 테스트 설정 임포트
public class UserRepositoryCustomTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("이메일로 사용자 조회 테스트 (QueryDSL)")
    public void findByEmailTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("이메일조회")
                .email("findbyemail@example.com")
                .build();
        userRepository.save(user);

        // when - 이메일로 사용자 조회 (QueryDSL 구현체 사용)
        Optional<User> foundUser = userRepository.findByEmail("findbyemail@example.com");

        // then - 결과 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("이메일조회");
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 테스트 (QueryDSL)")
    public void findByUsernameTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("사용자명조회")
                .email("findbyusername@example.com")
                .build();
        userRepository.save(user);

        // when - 사용자명으로 사용자 조회 (QueryDSL 구현체 사용)
        Optional<User> foundUser = userRepository.findByUsername("사용자명조회");

        // then - 결과 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("findbyusername@example.com");
    }

    @Test
    @DisplayName("페이징된 사용자 조회 테스트 (QueryDSL)")
    public void findAllWithPagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                    .username("페이징" + i)
                    .email("paging" + i + "@example.com")
                    .build();
            userRepository.save(user);
        }

        // when - QueryDSL로 페이징 조회
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = userRepository.findAllWithPaging(pageable);

        // then - 결과 검증
        assertThat(userPage.getContent()).isNotEmpty();
        assertThat(userPage.getSize()).isEqualTo(10);
        assertThat(userPage.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("정렬된 사용자 조회 테스트 (QueryDSL)")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 사용자 추가
        User userA = User.builder()
                .username("A정렬사용자")
                .email("a@example.com")
                .build();
        userRepository.save(userA);

        User userZ = User.builder()
                .username("Z정렬사용자")
                .email("z@example.com")
                .build();
        userRepository.save(userZ);

        // when - QueryDSL로 정렬 조회 (사용자명 기준 오름차순)
        Sort sort = Sort.by(Sort.Direction.ASC, "username");
        List<User> sortedUsers = userRepository.findAllWithSorting(sort);

        // then - 결과 검증 (정렬 순서 확인)
        assertThat(sortedUsers).isNotEmpty();

        // 사용자명이 A로 시작하는 사용자가 Z로 시작하는 사용자보다 앞에 있는지 확인
        int indexA = -1, indexZ = -1;
        for (int i = 0; i < sortedUsers.size(); i++) {
            if (sortedUsers.get(i).getUsername().equals("A정렬사용자")) {
                indexA = i;
            }
            if (sortedUsers.get(i).getUsername().equals("Z정렬사용자")) {
                indexZ = i;
            }
        }

        if (indexA != -1 && indexZ != -1) {
            assertThat(indexA).isLessThan(indexZ);
        }
    }

    @Test
    @DisplayName("사용자와 프로필 함께 조회 테스트 (QueryDSL)")
    public void findUserWithProfileTest() {
        // given - 테스트용 사용자, 이미지 및 프로필 생성
        User user = User.builder()
                .username("프로필조회")
                .email("profile@example.com")
                .build();
        userRepository.save(user);

        Image image = Image.builder()
                .url("https://example.com/profile.jpg")
                .altText("프로필 이미지")
                .build();
        imageRepository.save(image);

        UserProfile profile = UserProfile.builder()
                .nickname("프로필닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileRepository.save(profile);

        // JPA 관계 설정
        user.connectProfile(profile);
        userRepository.save(user);

        // when - QueryDSL로 사용자와 프로필 함께 조회
        Optional<User> userWithProfile = userRepository.findUserWithProfile(user.getId());

        // then - 결과 검증
        assertThat(userWithProfile).isPresent();
        assertThat(userWithProfile.get().getProfile()).isNotNull();
        assertThat(userWithProfile.get().getProfile().getNickname()).isEqualTo("프로필닉네임");
    }

    @Test
    @DisplayName("사용자와 주소 함께 조회 테스트 (QueryDSL)")
    public void findUserWithAddressesTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("주소조회")
                .email("address@example.com")
                .build();
        userRepository.save(user);

        // 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("1000" + i)
                    .detail("테스트 주소 " + i)
                    .isDefault(i == 1) // 첫번째 주소만 기본 주소로 설정
                    .user(user)
                    .build();
            addressRepository.save(address);

            // 양방향 관계 설정
            user.addAddress(address);
        }
        userRepository.save(user);

        // when - QueryDSL로 사용자와 주소 함께 조회
        Optional<User> userWithAddresses = userRepository.findUserWithAddresses(user.getId());

        // then - 결과 검증
        assertThat(userWithAddresses).isPresent();
        assertThat(userWithAddresses.get().getAddresses()).hasSize(3);
    }

    @Test
    @DisplayName("프로필과 주소 정보를 모두 포함한 사용자 조회 테스트 (QueryDSL)")
    public void findUserWithProfileAndAddressesTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("프로필주소조회")
                .email("profile-address@example.com")
                .build();
        userRepository.save(user);

        // 프로필 추가
        Image image = Image.builder()
                .url("https://example.com/profile-address.jpg")
                .altText("프로필 주소 조회 이미지")
                .build();
        imageRepository.save(image);

        UserProfile profile = UserProfile.builder()
                .nickname("프로필주소닉네임")
                .gender("남성")
                .user(user)
                .profileImage(image)
                .build();
        userProfileRepository.save(profile);

        // 프로필 연결
        user.connectProfile(profile);

        // 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("3000" + i)
                    .detail("프로필 주소 테스트 " + i)
                    .isDefault(i == 1) // 첫번째 주소만 기본 주소로 설정
                    .user(user)
                    .build();

            // 주소 연결
            user.addAddress(address);
        }
        userRepository.save(user);

        // when - QueryDSL로 프로필과 주소 정보를 모두 포함한 사용자 조회
        Optional<User> userWithProfileAndAddresses = userRepository.findUserWithProfileAndAddresses(user.getId());

        // then - 결과 검증
        assertThat(userWithProfileAndAddresses).isPresent();
        User foundUser = userWithProfileAndAddresses.get();
        assertThat(foundUser.getProfile()).isNotNull();
        assertThat(foundUser.getProfile().getNickname()).isEqualTo("프로필주소닉네임");
        assertThat(foundUser.getAddresses()).hasSize(3);
    }

    @Test
    @DisplayName("검색 조건을 이용한 사용자 검색 테스트 (QueryDSL)")
    public void searchUsersTest() {
        // given - 검색용 사용자 추가
        User user1 = User.builder()
                .username("검색테스트A")
                .email("search1@example.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("일반사용자")
                .email("search2@example.com")
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .username("검색테스트B")
                .email("normal@example.com")
                .build();
        userRepository.save(user3);

        // when - 키워드 검색
        UserSearchDto keywordDto = new UserSearchDto();
        keywordDto.setKeyword("검색");
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> keywordResults = userRepository.searchUsers(keywordDto, pageable);

        // then - 키워드 검색 결과 검증
        assertThat(keywordResults.getContent()).isNotEmpty();

        int keywordMatchCount = 0;
        for (User user : keywordResults.getContent()) {
            if (user.getUsername().contains("검색") || user.getEmail().contains("검색")) {
                keywordMatchCount++;
            }
        }
        assertThat(keywordMatchCount).isGreaterThan(0);

        // when - 사용자명으로 검색
        UserSearchDto usernameDto = new UserSearchDto();
        usernameDto.setUsername("검색테스트");
        Page<User> usernameResults = userRepository.searchUsers(usernameDto, pageable);

        // then - 사용자명 검색 결과 검증
        assertThat(usernameResults.getContent()).isNotEmpty();

        int usernameMatchCount = 0;
        for (User user : usernameResults.getContent()) {
            if (user.getUsername().equals("검색테스트A") || user.getUsername().equals("검색테스트B")) {
                usernameMatchCount++;
            }
        }
        assertThat(usernameMatchCount).isGreaterThan(0);

        // when - 이메일로 검색
        UserSearchDto emailDto = new UserSearchDto();
        emailDto.setEmail("search");
        Page<User> emailResults = userRepository.searchUsers(emailDto, pageable);

        // then - 이메일 검색 결과 검증
        assertThat(emailResults.getContent()).isNotEmpty();

        int emailMatchCount = 0;
        for (User user : emailResults.getContent()) {
            if (user.getEmail().contains("search")) {
                emailMatchCount++;
            }
        }
        assertThat(emailMatchCount).isGreaterThan(0);
    }
}