package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 이미지 매퍼 인터페이스
 */
@Mapper
public interface ImageMapper {
    /**
     * 이미지 저장
     *
     * @param image 저장할 이미지 정보
     */
    void insert(Image image);

    /**
     * 이미지 수정
     *
     * @param image 수정할 이미지 정보
     */
    void update(Image image);

    /**
     * 이미지 삭제
     *
     * @param id 삭제할 이미지 ID
     */
    void deleteById(Long id);

    /**
     * 이미지 조회
     *
     * @param id 조회할 이미지 ID
     * @return 이미지 정보
     */
    Image findById(Long id);

    /**
     * URL로 이미지 조회
     *
     * @param url 이미지 URL
     * @return 이미지 정보
     */
    Image findByUrl(String url);

    /**
     * 모든 이미지 조회
     *
     * @return 이미지 목록
     */
    List<Image> findAll();

    /**
     * 페이징된 이미지 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 이미지 목록
     */
    List<Image> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
}