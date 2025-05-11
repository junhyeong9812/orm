package com.benchmark.orm.domain.user.entity;

import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    /**
     * 사용자 정보 업데이트
     *
     * @param username 변경할 사용자명
     * @param email 변경할 이메일
     * @return 업데이트된 사용자 엔티티
     */
    public User updateInfo(String username, String email) {
        this.username = username;
        this.email = email;
        return this;
    }

    /**
     * 주소 추가
     *
     * @param address 추가할 주소
     * @return 현재 사용자 엔티티
     */
    public User addAddress(Address address) {
        this.addresses.add(address);
        address.assignUser(this); // 양방향 관계 설정
        return this;
    }

    /**
     * 주소 제거
     *
     * @param address 제거할 주소
     * @return 현재 사용자 엔티티
     */
    public User removeAddress(Address address) {
        this.addresses.remove(address);
        address.assignUser(null); // 양방향 관계 해제
        return this;
    }

    /**
     * 프로필 연결
     *
     * @param profile 연결할 프로필
     * @return 현재 사용자 엔티티
     */
    public User connectProfile(UserProfile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.assignUser(this); // 양방향 관계 설정
        }
        return this;
    }

    /**
     * 기본 주소 찾기
     *
     * @return 기본으로 설정된 주소, 없으면 null
     */
    public Address findDefaultAddress() {
        return this.addresses.stream()
                .filter(Address::isDefault)
                .findFirst()
                .orElse(null);
    }

    /**
     * 특정 상태의 주문 목록 조회
     *
     * @param status 조회할 주문 상태
     * @return 해당 상태의 주문 목록
     */
    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        return this.orders.stream()
                .filter(order -> order.getStatus() == status)
                .toList();
    }

    /**
     * 배송 중인 주문 목록 조회
     *
     * @return 배송 중인 주문 목록
     */
    public List<Order> getShippingOrders() {
        return findOrdersByStatus(Order.OrderStatus.SHIPPED);
    }

    /**
     * 배송 완료된 주문 목록 조회
     *
     * @return 배송 완료된 주문 목록
     */
    public List<Order> getDeliveredOrders() {
        return findOrdersByStatus(Order.OrderStatus.DELIVERED);
    }

    /**
     * 취소된 주문 목록 조회
     *
     * @return 취소된 주문 목록
     */
    public List<Order> getCancelledOrders() {
        return findOrdersByStatus(Order.OrderStatus.CANCELLED);
    }

    /**
     * 특정 기간의 주문 총액 계산
     *
     * @return 해당 기간의 주문 총액
     */
    public int calculateTotalOrderAmount() {
        return this.orders.stream()
                .mapToInt(Order::calculateTotalPrice)
                .sum();
    }
}