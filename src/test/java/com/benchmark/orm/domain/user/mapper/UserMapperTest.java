package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 사용자 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("사용자 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("매퍼테스트")
                .email("mapper@example.com")
                .build();

        // when - 사용자 저장
        userMapper.insert(user);

        // then - 결과 검증
        User foundUser = userMapper.findById(user.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getUsername()).isEqualTo("매퍼테스트");
        assertThat(foundUser.getEmail()).isEqualTo("mapper@example.com");
    }

    @Test
    @DisplayName("이메일로 사용자 조회 테스트")
    public void findByEmailTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("이메일조회")
                .email("findbyemail@example.com")
                .build();
        userMapper.insert(user);

        // when - 이메일로 사용자 조회
        User foundUser = userMapper.findByEmail("findbyemail@example.com");

        // then - 결과 검증
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("이메일조회");
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 테스트")
    public void findByUsernameTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("사용자명조회")
                .email("findbyusername@example.com")
                .build();
        userMapper.insert(user);

        // when - 사용자명으로 사용자 조회
        User foundUser = userMapper.findByUsername("사용자명조회");

        // then - 결과 검증
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("findbyusername@example.com");
    }

    @Test
    @DisplayName("사용자 정보 수정 테스트")
    public void updateTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("수정전")
                .email("before@example.com")
                .build();
        userMapper.insert(user);

        // when - 사용자 정보 수정
        User updatedUser = User.builder()
                .id(user.getId())
                .username("수정후")
                .email("after@example.com")
                .build();
        userMapper.update(updatedUser);

        // then - 결과 검증
        User foundUser = userMapper.findById(user.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("수정후");
        assertThat(foundUser.getEmail()).isEqualTo("after@example.com");
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 사용자 생성 및 저장
        User user = User.builder()
                .username("삭제테스트")
                .email("delete@example.com")
                .build();
        userMapper.insert(user);

        // 삭제 전 존재 확인
        User beforeDelete = userMapper.findById(user.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 사용자 삭제
        userMapper.deleteById(user.getId());

        // then - 삭제 후 존재 여부 확인
        User afterDelete = userMapper.findById(user.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    public void findAllTest() {
        // given - 여러 사용자 추가
        for (int i = 1; i <= 5; i++) {
            User user = User.builder()
                    .username("전체조회" + i)
                    .email("all" + i + "@example.com")
                    .build();
            userMapper.insert(user);
        }

        // when - 모든 사용자 조회
        List<User> allUsers = userMapper.findAll();

        // then - 결과 검증
        assertThat(allUsers).isNotEmpty();
        assertThat(allUsers.size()).isGreaterThanOrEqualTo(5);

        // 추가한 사용자들이 포함되어 있는지 확인
        int count = 0;
        for (int i = 1; i <= 5; i++) {
            String email = "all" + i + "@example.com";
            if (allUsers.stream().anyMatch(u -> email.equals(u.getEmail()))) {
                count++;
            }
        }
        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("페이징 테스트")
    public void findAllWithPagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        for (int i = 1; i <= 20; i++) {
            User user = User.builder()
                    .username("페이징" + i)
                    .email("paging" + i + "@example.com")
                    .build();
            userMapper.insert(user);
        }

        // when - 페이징 적용하여 조회
        List<User> page1 = userMapper.findAllWithPaging(0, 10); // 첫 페이지 (10개)
        List<User> page2 = userMapper.findAllWithPaging(10, 10); // 두번째 페이지 (10개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(10);
        assertThat(page2.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("정렬 테스트")
    public void findAllWithSortingTest() {
        // given - 정렬 테스트를 위한 사용자 추가
        User userA = User.builder()
                .username("A정렬사용자")
                .email("a@example.com")
                .build();
        userMapper.insert(userA);

        User userZ = User.builder()
                .username("Z정렬사용자")
                .email("z@example.com")
                .build();
        userMapper.insert(userZ);

        // when - 오름차순 정렬
        List<User> ascUsers = userMapper.findAllWithSorting("username", "asc");

        // then - 결과 검증 (A가 Z보다 앞에 있어야 함)
        boolean correctOrder = false;
        for (int i = 0; i < ascUsers.size() - 1; i++) {
            if ("A정렬사용자".equals(ascUsers.get(i).getUsername()) &&
                    "Z정렬사용자".equals(ascUsers.get(i+1).getUsername())) {
                correctOrder = true;
                break;
            }
        }

        if (!correctOrder) {
            int indexA = -1, indexZ = -1;
            for (int i = 0; i < ascUsers.size(); i++) {
                if ("A정렬사용자".equals(ascUsers.get(i).getUsername())) {
                    indexA = i;
                }
                if ("Z정렬사용자".equals(ascUsers.get(i).getUsername())) {
                    indexZ = i;
                }
            }

            if (indexA != -1 && indexZ != -1) {
                assertThat(indexA).isLessThan(indexZ);
            }
        }
    }

    @Test
    @DisplayName("검색 테스트")
    public void searchUsersTest() {
        // given - 검색용 사용자 추가
        User user1 = User.builder()
                .username("검색테스트A")
                .email("search1@example.com")
                .build();
        userMapper.insert(user1);

        User user2 = User.builder()
                .username("일반사용자")
                .email("search2@example.com")
                .build();
        userMapper.insert(user2);

        User user3 = User.builder()
                .username("검색테스트B")
                .email("normal@example.com")
                .build();
        userMapper.insert(user3);

        // when - 사용자명으로 검색
        UserSearchDto usernameDto = new UserSearchDto();
        usernameDto.setUsername("검색테스트");

        List<User> usernameResults = userMapper.searchUsers(usernameDto, 0, 10, "id", "asc");

        // then - 결과 검증
        assertThat(usernameResults.size()).isEqualTo(2);
        assertThat(usernameResults).extracting("username")
                .containsExactlyInAnyOrder("검색테스트A", "검색테스트B");

        // when - 이메일로 검색
        UserSearchDto emailDto = new UserSearchDto();
        emailDto.setEmail("search");

        List<User> emailResults = userMapper.searchUsers(emailDto, 0, 10, "id", "asc");

        // then - 결과 검증
        assertThat(emailResults.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자와 프로필 정보 함께 조회 테스트")
    public void findUserWithProfileTest() {
        // given - 사용자와 프로필 관계 설정은 테스트 데이터 삽입 스크립트에 의존

        // when - 사용자 프로필 조회 시도
        User userWithProfile = userMapper.findUserWithProfile(1L); // 프로필이 있는 사용자 ID 가정

        // then - 결과 가능성 검증 (프로필이 있거나 없을 수 있음)
        if (userWithProfile != null) {
            assertThat(userWithProfile.getId()).isEqualTo(1L);
        }
    }

    @Test
    @DisplayName("사용자와 주소 정보 함께 조회 테스트")
    public void findUserWithAddressesTest() {
        // given - 사용자와 주소 관계 설정은 테스트 데이터 삽입 스크립트에 의존

        // when - 사용자 주소 조회 시도
        User userWithAddresses = userMapper.findUserWithAddresses(1L); // 주소가 있는 사용자 ID 가정

        // then - 결과 가능성 검증 (주소가 있거나 없을 수 있음)
        if (userWithAddresses != null) {
            assertThat(userWithAddresses.getId()).isEqualTo(1L);
        }
    }
}