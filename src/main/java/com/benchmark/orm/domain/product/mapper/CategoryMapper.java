package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    void insert(Category category);
    void update(Category category);
    void deleteById(Long id);
    Category findById(Long id);
    List<Category> findAll();
    List<Category> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    List<Category> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
    List<Category> findAllWithPagingAndSorting(@Param("offset") int offset, @Param("limit") int limit,
                                               @Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
}
