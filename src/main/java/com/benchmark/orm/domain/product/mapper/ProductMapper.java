package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    void insert(Product product);
    void update(Product product);
    void deleteById(Long id);
    Product findById(Long id);
    List<Product> findAll();
    List<Product> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    List<Product> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
    List<Product> findAllWithPagingAndSorting(@Param("offset") int offset, @Param("limit") int limit,
                                              @Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
}
