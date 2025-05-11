package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    void insert(User user);

    void update(User user);

    void deleteById(Long id);

    User findById(Long id);

    List<User> findAll();

    List<User> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    List<User> findAllWithSorting(@Param("sortColumn") String sortColumn,
                                  @Param("sortDirection") String sortDirection);

    List<User> findAllWithPagingAndSorting(@Param("offset") int offset,
                                           @Param("limit") int limit,
                                           @Param("sortColumn") String sortColumn,
                                           @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 사용자 검색
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 검색된 사용자 리스트
     */
    List<User> searchUsers(@Param("searchDto") UserSearchDto searchDto,
                           @Param("offset") int offset,
                           @Param("limit") int limit,
                           @Param("sortColumn") String sortColumn,
                           @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 사용자 총 개수 조회
     * @param searchDto 검색 조건 DTO
     * @return 검색된 사용자 총 개수
     */
    int countBySearchDto(@Param("searchDto") UserSearchDto searchDto);
}