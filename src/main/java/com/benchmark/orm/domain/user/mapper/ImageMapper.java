package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {
    void insert(Image image);
    void update(Image image);
    void deleteById(Long id);
    Image findById(Long id);
    List<Image> findAll();
    List<Image> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    List<Image> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
    List<Image> findAllWithPagingAndSorting(@Param("offset") int offset, @Param("limit") int limit,
                                            @Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
}
