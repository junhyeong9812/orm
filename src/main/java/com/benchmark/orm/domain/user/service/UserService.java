package com.benchmark.orm.domain.user.service;

import com.benchmark.orm.domain.user.dto.UserRequestDto;
import com.benchmark.orm.domain.user.dto.UserResponseDto;
import com.benchmark.orm.domain.user.dto.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface UserService {

    /**
     * JPA를 사용하여 사용자 저장
     * @param userDto 저장할 사용자 DTO
     * @return 저장된 사용자 응답 DTO
     */
    UserResponseDto saveUserJpa(UserRequestDto userDto);

    /**
     * MyBatis를 사용하여 사용자 저장
     * @param userDto 저장할 사용자 DTO
     * @return 결과 메시지
     */
    String saveUserMyBatis(UserRequestDto userDto);

    /**
     * JPA를 사용하여 ID로 사용자 조회
     * @param id 사용자 ID
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserByIdJpa(Long id);

    /**
     * MyBatis를 사용하여 ID로 사용자 조회
     * @param id 사용자 ID
     * @return 사용자 응답 DTO
     */
    UserResponseDto findUserByIdMyBatis(Long id);

    /**
     * JPA를 사용하여 모든 사용자 조회
     * @return 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> findAllUsersJpa();

    /**
     * MyBatis를 사용하여 모든 사용자 조회
     * @return 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> findAllUsersMyBatis();

    /**
     * JPQL을 사용하여 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserByEmailJpql(String email);

    /**
     * QueryDSL을 사용하여 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserByEmailQueryDsl(String email);

    /**
     * JPQL을 사용하여 사용자명으로 사용자 조회
     * @param username 사용자명
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserByUsernameJpql(String username);

    /**
     * QueryDSL을 사용하여 사용자명으로 사용자 조회
     * @param username 사용자명
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserByUsernameQueryDsl(String username);

    /**
     * JPA를 사용하여 페이징으로 사용자 조회
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 응답 DTO 객체
     */
    Page<UserResponseDto> findUsersWithPagingJpa(Pageable pageable);

    /**
     * QueryDSL을 사용하여 페이징으로 사용자 조회
     * @param pageable 페이징 정보
     * @return 페이징된 사용자 응답 DTO 객체
     */
    Page<UserResponseDto> findUsersWithPagingQueryDsl(Pageable pageable);

    /**
     * MyBatis를 사용하여 페이징으로 사용자 조회
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @return 페이징된 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> findUsersWithPagingMyBatis(int offset, int limit);

    /**
     * JPA를 사용하여 정렬로 사용자 조회
     * @param sort 정렬 정보
     * @return 정렬된 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> findUsersWithSortingJpa(Sort sort);

    /**
     * QueryDSL을 사용하여 정렬로 사용자 조회
     * @param sort 정렬 정보
     * @return 정렬된 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> findUsersWithSortingQueryDsl(Sort sort);

    /**
     * MyBatis를 사용하여 정렬로 사용자 조회
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 정렬된 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> findUsersWithSortingMyBatis(String sortColumn, String sortDirection);

    /**
     * JPA를 사용하여 페이징 및 정렬로 사용자 조회
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징 및 정렬된 사용자 응답 DTO 객체
     */
    Page<UserResponseDto> findUsersWithPagingAndSortingJpa(Pageable pageable);

    /**
     * MyBatis를 사용하여 페이징 및 정렬로 사용자 조회
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 페이징 및 정렬된 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> findUsersWithPagingAndSortingMyBatis(int offset, int limit, String sortColumn, String sortDirection);

    /**
     * JPQL을 사용하여 프로필 정보와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserWithProfileJpql(Long userId);

    /**
     * QueryDSL을 사용하여 프로필 정보와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserWithProfileQueryDsl(Long userId);

    /**
     * JPQL을 사용하여 주소 정보와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserWithAddressesJpql(Long userId);

    /**
     * QueryDSL을 사용하여 주소 정보와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserWithAddressesQueryDsl(Long userId);

    /**
     * QueryDSL을 사용하여 프로필 및 주소 정보와 함께 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 응답 DTO Optional 객체
     */
    Optional<UserResponseDto> findUserWithProfileAndAddressesQueryDsl(Long userId);

    /**
     * JPQL을 사용하여 키워드로 사용자 검색
     * @param keyword 검색 키워드
     * @return 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> searchUsersByKeywordJpql(String keyword);

    /**
     * 검색 조건을 이용한 사용자 검색
     * @param searchDto 검색 조건 DTO
     * @return 사용자 응답 DTO 리스트
     */
    List<UserResponseDto> searchUsers(UserSearchDto searchDto);

    /**
     * JPA를 사용하여 사용자 정보 업데이트
     * @param id 사용자 ID
     * @param userDto 업데이트할 사용자 DTO
     * @return 업데이트된 사용자 응답 DTO
     */
    UserResponseDto updateUserJpa(Long id, UserRequestDto userDto);

    /**
     * MyBatis를 사용하여 사용자 정보 업데이트
     * @param id 사용자 ID
     * @param userDto 업데이트할 사용자 DTO
     * @return 결과 메시지
     */
    String updateUserMyBatis(Long id, UserRequestDto userDto);

    /**
     * JPA를 사용하여 사용자 삭제
     * @param id 삭제할 사용자 ID
     * @return 결과 메시지
     */
    String deleteUserJpa(Long id);

    /**
     * MyBatis를 사용하여 사용자 삭제
     * @param id 삭제할 사용자 ID
     * @return 결과 메시지
     */
    String deleteUserMyBatis(Long id);
}