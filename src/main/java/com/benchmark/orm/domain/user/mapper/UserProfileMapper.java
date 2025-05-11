package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 사용자 프로필 매퍼 인터페이스
 */
@Mapper
public interface UserProfileMapper {
    /**
     * 사용자 프로필 저장
     *
     * @param userProfile 저장할 사용자 프로필 정보
     */
    void insert(UserProfile userProfile);

    /**
     * 사용자 프로필 수정
     *
     * @param userProfile 수정할 사용자 프로필 정보
     */
    void update(UserProfile userProfile);

    /**
     * 사용자 프로필 삭제
     *
     * @param id 삭제할 사용자 프로필 ID
     */
    void deleteById(Long id);

    /**
     * 사용자 프로필 조회
     *
     * @param id 조회할 사용자 프로필 ID
     * @return 사용자 프로필 정보
     */
    UserProfile findById(Long id);

    /**
     * 사용자 ID로 프로필 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 프로필 정보
     */
    UserProfile findByUserId(Long userId);

    /**
     * 모든 사용자 프로필 조회
     *
     * @return 사용자 프로필 목록
     */
    List<UserProfile> findAll();

    /**
     * 페이징된 사용자 프로필 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 사용자 프로필 목록
     */
    List<UserProfile> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 사용자 프로필과 이미지 정보 함께 조회
     *
     * @param id 사용자 프로필 ID
     * @return 사용자 프로필 정보 (이미지 포함)
     */
    UserProfile findProfileWithImage(Long id);

    /**
     * 사용자 ID로 사용자 프로필과 이미지 정보 함께 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 프로필 정보 (이미지 포함)
     */
    UserProfile findProfileWithImageByUserId(Long userId);
}