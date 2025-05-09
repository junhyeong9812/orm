package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 리포지토리 인터페이스
 * UserRepositoryCustom 확장
 */
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    /**
     * JPQL을 사용한 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 Optional 객체
     */
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailJpql(@Param("email") String email);

    /**
     * JPQL을 사용한 사용자명으로 사용자 조회
     * @param username 사용자명
     * @return 사용자 Optional 객체
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameJpql(@Param("username") String username);

    /**
     * JPQL을 사용한 사용자 프로필과 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 Optional 객체
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile WHERE u.id = :userId")
    Optional<User> findUserWithProfileJpql(@Param("userId") Long userId);

    /**
     * JPQL을 사용한 사용자 주소와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 Optional 객체
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :userId")
    Optional<User> findUserWithAddressesJpql(@Param("userId") Long userId);

    /**
     * JPQL을 사용한 사용자 검색
     * @param keyword 검색 키워드
     * @return 사용자 리스트
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> searchUsersByKeywordJpql(@Param("keyword") String keyword);
}