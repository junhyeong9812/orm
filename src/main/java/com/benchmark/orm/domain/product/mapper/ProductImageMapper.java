package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductImageMapper {
    void insert(ProductImage image);
    void update(ProductImage image);
    void deleteById(Long id);
    ProductImage findById(Long id);
    List<ProductImage> findAll();
    List<ProductImage> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    List<ProductImage> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
    List<ProductImage> findAllWithPagingAndSorting(@Param("offset") int offset, @Param("limit") int limit,
                                                   @Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
}
