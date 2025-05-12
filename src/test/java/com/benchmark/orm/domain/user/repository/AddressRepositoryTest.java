package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.entity.Address;
import com.benchmark.orm.domain.user.entity.User;
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
 * AddressRepository 테스트
 * <p>
 * JPA Repository를 통한 주소 데이터 접근 테스트
 */
@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("주소 저장 및 조회 테스트")
    public void saveAndFindByIdTest() {
        // given - 테스트용 사용자 및 주소 생성
        User user = User.builder()
                .username("주소테스트")
                .email("address@example.com")
                .build();
        userRepository.save(user);

        Address address = Address.builder()
                .zipcode("12345")
                .detail("서울시 강남구 테스트로 123")
                .isDefault(false)
                .user(user)
                .build();

        // when - 주소 저장
        Address savedAddress = addressRepository.save(address);

        // then - ID로 조회 및 결과 검증
        Optional<Address> foundAddress = addressRepository.findById(savedAddress.getId());
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getZipcode()).isEqualTo("12345");
        assertThat(foundAddress.get().getDetail()).isEqualTo("서울시 강남구 테스트로 123");
        assertThat(foundAddress.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("주소 정보 업데이트 테스트")
    public void updateTest() {
        // given - 테스트용 사용자 및 주소 생성
        User user = User.builder()
                .username("주소수정")
                .email("address-update@example.com")
                .build();
        userRepository.save(user);

        Address address = Address.builder()
                .zipcode("12345")
                .detail("서울시 강남구 테스트로 123")
                .isDefault(false)
                .user(user)
                .build();
        addressRepository.save(address);

        // when - 주소 정보 수정
        address.updateInfo("54321", "서울시 서초구 테스트로 456");
        Address updatedAddress = addressRepository.save(address);

        // then - 결과 검증
        assertThat(updatedAddress.getZipcode()).isEqualTo("54321");
        assertThat(updatedAddress.getDetail()).isEqualTo("서울시 서초구 테스트로 456");
    }

    @Test
    @DisplayName("주소 삭제 테스트")
    public void deleteTest() {
        // given - 테스트용 사용자 및 주소 생성
        User user = User.builder()
                .username("주소삭제")
                .email("address-delete@example.com")
                .build();
        userRepository.save(user);

        Address address = Address.builder()
                .zipcode("12345")
                .detail("서울시 강남구 테스트로 123")
                .isDefault(false)
                .user(user)
                .build();
        addressRepository.save(address);

        // 삭제 전 존재 확인
        assertThat(addressRepository.findById(address.getId())).isPresent();

        // when - 주소 삭제
        addressRepository.deleteById(address.getId());

        // then - 삭제 후 존재 여부 확인
        assertThat(addressRepository.findById(address.getId())).isEmpty();
    }

    @Test
    @DisplayName("기본 주소 설정 테스트")
    public void markAsDefaultTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("기본주소")
                .email("default-address@example.com")
                .build();
        userRepository.save(user);

        // 여러 주소 추가
        Address[] addresses = new Address[3];
        for (int i = 0; i < 3; i++) {
            addresses[i] = Address.builder()
                    .zipcode("2000" + (i + 1))
                    .detail("기본주소 테스트 " + (i + 1))
                    .isDefault(i == 0) // 첫번째 주소만 기본 주소로 설정
                    .user(user)
                    .build();
            addressRepository.save(addresses[i]);

            user.addAddress(addresses[i]);
        }
        userRepository.save(user);

        // when - 두번째 주소를 기본 주소로 변경
        addresses[1].markAsDefault(true);
        addresses[0].markAsDefault(false); // 기존 기본 주소 해제
        addressRepository.save(addresses[1]);
        addressRepository.save(addresses[0]);

        // then - 기본 주소 변경 확인
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        Address newDefaultAddress = updatedUser.findDefaultAddress();
        assertThat(newDefaultAddress).isNotNull();
        assertThat(newDefaultAddress.getId()).isEqualTo(addresses[1].getId());
        assertThat(newDefaultAddress.getDetail()).contains("기본주소 테스트 2");
    }

    @Test
    @DisplayName("사용자 삭제시 주소 cascade 삭제 테스트")
    public void cascadeDeleteTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("cascade테스트")
                .email("cascade@example.com")
                .build();
        userRepository.save(user);

        // 여러 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("3000" + i)
                    .detail("Cascade 테스트 주소 " + i)
                    .isDefault(i == 1)
                    .user(user)
                    .build();
            addressRepository.save(address);

            user.addAddress(address);
        }
        userRepository.save(user);

        // 주소 ID 저장
        Long[] addressIds = user.getAddresses().stream()
                .map(Address::getId)
                .toArray(Long[]::new);

        // when - 사용자 삭제 (cascade = CascadeType.ALL, orphanRemoval = true 설정됨)
        userRepository.delete(user);

        // then - 주소도 함께 삭제되었는지 확인
        for (Long addressId : addressIds) {
            assertThat(addressRepository.findById(addressId)).isEmpty();
        }
    }

    @Test
    @DisplayName("페이징된 주소 조회 테스트")
    public void pagingTest() {
        // given - 테스트용 사용자 생성
        User user = User.builder()
                .username("페이징테스트")
                .email("paging@example.com")
                .build();
        userRepository.save(user);

        // 여러 주소 추가
        for (int i = 1; i <= 10; i++) {
            Address address = Address.builder()
                    .zipcode("4000" + i)
                    .detail("페이징 테스트 주소 " + i)
                    .isDefault(i == 1)
                    .user(user)
                    .build();
            addressRepository.save(address);

            user.addAddress(address);
        }
        userRepository.save(user);

        // when - 페이징 적용하여 조회
        Pageable pageable = PageRequest.of(0, 5); // 첫 페이지 (5개)
        Page<Address> addressPage = addressRepository.findAll(pageable);

        // then - 결과 검증
        assertThat(addressPage.getContent()).isNotEmpty();
        assertThat(addressPage.getContent().size()).isEqualTo(5);
        assertThat(addressPage.getTotalElements()).isGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("사용자ID로 주소 목록 조회 테스트")
    public void findAddressesByUserTest() {
        // given - 테스트용 사용자 생성
        User user1 = User.builder()
                .username("주소목록1")
                .email("address-list1@example.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("주소목록2")
                .email("address-list2@example.com")
                .build();
        userRepository.save(user2);

        // 사용자1 주소 추가
        for (int i = 1; i <= 3; i++) {
            Address address = Address.builder()
                    .zipcode("5000" + i)
                    .detail("사용자1 주소 " + i)
                    .isDefault(i == 1)
                    .user(user1)
                    .build();
            addressRepository.save(address);
            user1.addAddress(address);
        }
        userRepository.save(user1);

        // 사용자2 주소 추가
        for (int i = 1; i <= 2; i++) {
            Address address = Address.builder()
                    .zipcode("6000" + i)
                    .detail("사용자2 주소 " + i)
                    .isDefault(i == 1)
                    .user(user2)
                    .build();
            addressRepository.save(address);
            user2.addAddress(address);
        }
        userRepository.save(user2);

        // when - 사용자로 주소 목록 필터링
        List<Address> user1Addresses = user1.getAddresses();
        List<Address> user2Addresses = user2.getAddresses();

        // then - 결과 검증
        assertThat(user1Addresses).hasSize(3);
        assertThat(user2Addresses).hasSize(2);

        // 각 사용자의 주소만 포함되었는지 확인
        for (Address address : user1Addresses) {
            assertThat(address.getUser().getId()).isEqualTo(user1.getId());
        }

        for (Address address : user2Addresses) {
            assertThat(address.getUser().getId()).isEqualTo(user2.getId());
        }
    }
}