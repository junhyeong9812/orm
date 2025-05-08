package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BrandMapper {
    void insert(Brand brand);
    void update(Brand brand);
    void deleteById(Long id);
    Brand findById(Long id);
    List<Brand> findAll();
    List<Brand> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    List<Brand> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
    List<Brand> findAllWithPagingAndSorting(@Param("offset") int offset, @Param("limit") int limit,
                                            @Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
}
