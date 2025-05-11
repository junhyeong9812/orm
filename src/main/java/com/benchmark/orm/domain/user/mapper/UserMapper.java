package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 사용자 매퍼 인터페이스
 */
@Mapper
public interface UserMapper {
    /**
     * 사용자 저장
     *
     * @param user 저장할 사용자 정보
     */
    void insert(User user);

    /**
     * 사용자 수정
     *
     * @param user 수정할 사용자 정보
     */
    void update(User user);

    /**
     * 사용자 삭제
     *
     * @param id 삭제할 사용자 ID
     */
    void deleteById(Long id);

    /**
     * 사용자 조회
     *
     * @param id 조회할 사용자 ID
     * @return 사용자 정보
     */
    User findById(Long id);

    /**
     * 이메일로 사용자 조회
     *
     * @param email 이메일
     * @return 사용자 정보
     */
    User findByEmail(String email);

    /**
     * 사용자명으로 사용자 조회
     *
     * @param username 사용자명
     * @return 사용자 정보
     */
    User findByUsername(String username);

    /**
     * 모든 사용자 조회
     *
     * @return 사용자 목록
     */
    List<User> findAll();

    /**
     * 페이징된 사용자 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 사용자 목록
     */
    List<User> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 정렬된 사용자 조회
     *
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 사용자 목록
     */
    List<User> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);

    /**
     * 페이징 및 정렬된 사용자 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 사용자 목록
     */
    List<User> findAllWithPagingAndSorting(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 사용자 검색
     *
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 검색된 사용자 리스트
     */
    List<User> searchUsers(
            @Param("searchDto") UserSearchDto searchDto,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 사용자 총 개수 조회
     *
     * @param searchDto 검색 조건 DTO
     * @return 검색된 사용자 총 개수
     */
    int countBySearchDto(@Param("searchDto") UserSearchDto searchDto);

    /**
     * 사용자와 프로필 정보 함께 조회
     *
     * @param id 사용자 ID
     * @return 사용자 정보 (프로필 포함)
     */
    User findUserWithProfile(Long id);

    /**
     * 사용자와 주소 정보 함께 조회
     *
     * @param id 사용자 ID
     * @return 사용자 정보 (주소 포함)
     */
    User findUserWithAddresses(Long id);
}