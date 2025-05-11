package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 주소 매퍼 인터페이스
 */
@Mapper
public interface AddressMapper {
    /**
     * 주소 저장
     *
     * @param address 저장할 주소 정보
     */
    void insert(Address address);

    /**
     * 주소 수정
     *
     * @param address 수정할 주소 정보
     */
    void update(Address address);

    /**
     * 주소 삭제
     *
     * @param id 삭제할 주소 ID
     */
    void deleteById(Long id);

    /**
     * 사용자 ID로 주소 모두 삭제
     *
     * @param userId 사용자 ID
     */
    void deleteByUserId(Long userId);

    /**
     * 주소 조회
     *
     * @param id 조회할 주소 ID
     * @return 주소 정보
     */
    Address findById(Long id);

    /**
     * 사용자 ID로 주소 목록 조회
     *
     * @param userId 사용자 ID
     * @return 주소 목록
     */
    List<Address> findByUserId(Long userId);

    /**
     * 사용자 ID로 기본 주소 조회
     *
     * @param userId 사용자 ID
     * @return 기본 주소 정보
     */
    Address findDefaultByUserId(Long userId);

    /**
     * 모든 주소 조회
     *
     * @return 주소 목록
     */
    List<Address> findAll();

    /**
     * 페이징된 주소 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 주소 목록
     */
    List<Address> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 사용자 ID로 페이징된 주소 조회
     *
     * @param userId 사용자 ID
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 주소 목록
     */
    List<Address> findByUserIdWithPaging(
            @Param("userId") Long userId,
            @Param("offset") int offset,
            @Param("limit") int limit);
}