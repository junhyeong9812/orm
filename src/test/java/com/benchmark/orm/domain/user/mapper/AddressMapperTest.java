package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.Address;
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
 * AddressMapper 테스트
 * <p>
 * MyBatis Mapper를 사용한 주소 관련 데이터 접근 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AddressMapperTest {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserMapper userMapper; // 사용자 정보 필요

    @Test
    @DisplayName("주소 저장 및 조회 테스트")
    public void insertAndFindByIdTest() {
        // given - 사용자 생성 및 저장
        User user = User.builder()
                .username("주소테스트")
                .email("address@example.com")
                .build();
        userMapper.insert(user);

        // 주소 생성
        Address address = Address.builder()
                .zipcode("12345")
                .detail("서울시 강남구 테스트로 123")
                .isDefault(false)
                .user(user)
                .build();

        // when - 주소 저장
        addressMapper.insert(address);

        // then - 결과 검증
        Address savedAddress = addressMapper.findById(address.getId());
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getZipcode()).isEqualTo("12345");
        assertThat(savedAddress.getDetail()).isEqualTo("서울시 강남구 테스트로 123");
        assertThat(savedAddress.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("주소 수정 테스트")
    public void updateTest() {
        // given - 테스트용 사용자 및 주소 생성
        User user = User.builder()
                .username("주소수정")
                .email("address-update@example.com")
                .build();
        userMapper.insert(user);

        Address address = Address.builder()
                .zipcode("12345")
                .detail("서울시 강남구 테스트로 123")
                .isDefault(false)
                .user(user)
                .build();
        addressMapper.insert(address);

        // when - 주소 정보 수정
        address.updateInfo("54321", "서울시 서초구 테스트로 456");
        addressMapper.update(address);

        // then - 결과 검증
        Address updatedAddress = addressMapper.findById(address.getId());
        assertThat(updatedAddress).isNotNull();
        assertThat(updatedAddress.getZipcode()).isEqualTo("54321");
        assertThat(updatedAddress.getDetail()).isEqualTo("서울시 서초구 테스트로 456");
    }

    @Test
    @DisplayName("주소 삭제 테스트")
    public void deleteByIdTest() {
        // given - 테스트용 사용자 및 주소 생성
        User user = User.builder()
                .username("주소삭제")
                .email("address-delete@example.com")
                .build();
        userMapper.insert(user);

        Address address = Address.builder()
                .zipcode("12345")
                .detail("서울시 강남구 테스트로 123")
                .isDefault(false)
                .user(user)
                .build();
        addressMapper.insert(address);

        // 삭제 전 존재 확인
        Address beforeDelete = addressMapper.findById(address.getId());
        assertThat(beforeDelete).isNotNull();

        // when - 주소 삭제
        addressMapper.deleteById(address.getId());

        // then - 삭제 후 존재 여부 확인
        Address afterDelete = addressMapper.findById(address.getId());
        assertThat(afterDelete).isNull();
    }

    @Test
    @DisplayName("사용자 ID로 주소 모두 삭제 테스트")
    public void deleteByUserIdTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("주소일괄삭제")
                .email("address-delete-all@example.com")
                .build();
        userMapper.insert(user);

        // 여러 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("1000" + i)
                    .detail("테스트 주소 " + i)
                    .isDefault(i == 1) // 첫번째 주소만 기본 주소로 설정
                    .user(user)
                    .build();
            addressMapper.insert(address);
        }

        // 삭제 전 존재 확인
        List<Address> beforeAddresses = addressMapper.findByUserId(user.getId());
        assertThat(beforeAddresses).hasSize(3);

        // when - 사용자 ID로 주소 모두 삭제
        addressMapper.deleteByUserId(user.getId());

        // then - 삭제 후 존재 여부 확인
        List<Address> afterAddresses = addressMapper.findByUserId(user.getId());
        assertThat(afterAddresses).isEmpty();
    }

    @Test
    @DisplayName("사용자 ID로 주소 목록 조회 테스트")
    public void findByUserIdTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("주소목록조회")
                .email("address-list@example.com")
                .build();
        userMapper.insert(user);

        // 여러 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("2000" + i)
                    .detail("목록조회 주소 " + i)
                    .isDefault(i == 1) // 첫번째 주소만 기본 주소로 설정
                    .user(user)
                    .build();
            addressMapper.insert(address);
        }

        // when - 사용자 ID로 주소 목록 조회
        List<Address> addresses = addressMapper.findByUserId(user.getId());

        // then - 결과 검증
        assertThat(addresses).hasSize(3);
        assertThat(addresses).allMatch(address -> address.getUser().getId().equals(user.getId()));
    }

    @Test
    @DisplayName("사용자 ID로 기본 주소 조회 테스트")
    public void findDefaultByUserIdTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("기본주소조회")
                .email("default-address@example.com")
                .build();
        userMapper.insert(user);

        // 여러 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("3000" + i)
                    .detail("기본주소 테스트 " + i)
                    .isDefault(i == 2) // 두번째 주소만 기본 주소로 설정
                    .user(user)
                    .build();
            addressMapper.insert(address);
        }

        // when - 사용자 ID로 기본 주소 조회
        Address defaultAddress = addressMapper.findDefaultByUserId(user.getId());

        // then - 결과 검증
        assertThat(defaultAddress).isNotNull();
        assertThat(defaultAddress.isDefault()).isTrue();
        assertThat(defaultAddress.getDetail()).contains("기본주소 테스트 2");
    }

    @Test
    @DisplayName("모든 주소 조회 테스트")
    public void findAllTest() {
        // given - 테스트용 주소 여러개 추가
        User user = User.builder()
                .username("전체주소조회")
                .email("all-addresses@example.com")
                .build();
        userMapper.insert(user);

        int initialCount = addressMapper.findAll().size();

        for (int i = 1; i <= 5; i++) {
            Address address = Address.builder()
                    .zipcode("4000" + i)
                    .detail("전체조회 주소 " + i)
                    .isDefault(false)
                    .user(user)
                    .build();
            addressMapper.insert(address);
        }

        // when - 모든 주소 조회
        List<Address> addresses = addressMapper.findAll();

        // then - 결과 검증
        assertThat(addresses).isNotEmpty();
        assertThat(addresses.size()).isEqualTo(initialCount + 5);
    }

    @Test
    @DisplayName("페이징된 주소 조회 테스트")
    public void findAllWithPagingTest() {
        // given - 페이징 테스트를 위한 데이터 추가
        User user = User.builder()
                .username("페이징주소")
                .email("paging-address@example.com")
                .build();
        userMapper.insert(user);

        for (int i = 1; i <= 10; i++) {
            Address address = Address.builder()
                    .zipcode("5000" + i)
                    .detail("페이징 주소 " + i)
                    .isDefault(false)
                    .user(user)
                    .build();
            addressMapper.insert(address);
        }

        // when - 페이징 적용하여 조회
        List<Address> page1 = addressMapper.findAllWithPaging(0, 5); // 첫 페이지 (5개)
        List<Address> page2 = addressMapper.findAllWithPaging(5, 5); // 두번째 페이지 (5개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(5);
        assertThat(page2.size()).isGreaterThanOrEqualTo(5); // 이미 존재하는 주소들이 있을 수 있어 최소 5개 이상
    }

    @Test
    @DisplayName("사용자 ID로 페이징된 주소 조회 테스트")
    public void findByUserIdWithPagingTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("사용자페이징주소")
                .email("user-paging-address@example.com")
                .build();
        userMapper.insert(user);

        // 여러 주소 추가
        for (int i = 1; i <= 10; i++) {
            Address address = Address.builder()
                    .zipcode("6000" + i)
                    .detail("사용자 페이징 주소 " + i)
                    .isDefault(false)
                    .user(user)
                    .build();
            addressMapper.insert(address);
        }

        // when - 사용자 ID로 페이징된 주소 조회
        List<Address> page1 = addressMapper.findByUserIdWithPaging(user.getId(), 0, 5); // 첫 페이지 (5개)
        List<Address> page2 = addressMapper.findByUserIdWithPaging(user.getId(), 5, 5); // 두번째 페이지 (5개)

        // then - 결과 검증
        assertThat(page1.size()).isEqualTo(5);
        assertThat(page2.size()).isEqualTo(5);
        // 모든 결과가 해당 사용자의 주소인지 확인
        assertThat(page1).allMatch(address -> address.getUser().getId().equals(user.getId()));
        assertThat(page2).allMatch(address -> address.getUser().getId().equals(user.getId()));
    }
}