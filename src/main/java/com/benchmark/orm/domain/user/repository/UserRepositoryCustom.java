package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 리포지토리의 커스텀 인터페이스
 * JPA 기본 인터페이스에서 제공하지 않는 기능을 확장하기 위한 인터페이스
 */
public interface UserRepositoryCustom {

    /**
     * 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 Optional 객체
     */
    Optional<User> findByEmail(String email);

    /**
     * 사용자명으로 사용자 조회
     * @param username 사용자명
     * @return 사용자 Optional 객체
     */
    Optional<User> findByUsername(String username);

    /**
     * 페이징 및 정렬 기능을 사용하여 모든 사용자 조회
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 리스트
     */
    Page<User> findAllWithPaging(Pageable pageable);

    /**
     * 특정 정렬 방식으로 모든 사용자 조회
     * @param sort 정렬 정보
     * @return 정렬된 사용자 리스트
     */
    List<User> findAllWithSorting(Sort sort);

    /**
     * 사용자 프로필 정보와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 Optional 객체
     */
    Optional<User> findUserWithProfile(Long userId);

    /**
     * 사용자 주소 정보와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 Optional 객체
     */
    Optional<User> findUserWithAddresses(Long userId);

    /**
     * 프로필과 주소 정보를 모두 포함한 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 Optional 객체
     */
    Optional<User> findUserWithProfileAndAddresses(Long userId);

    /**
     * 검색 조건을 이용한 사용자 검색 (QueryDSL 사용)
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 정보
     */
    Page<User> searchUsers(UserSearchDto searchDto, Pageable pageable);
}