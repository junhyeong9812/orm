package com.benchmark.orm.domain.order.mapper;

import com.benchmark.orm.domain.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insert(Order order);
    void update(Order order);
    void deleteById(Long id);
    Order findById(Long id);
    List<Order> findAll();
    List<Order> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    List<Order> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
    List<Order> findAllWithPagingAndSorting(@Param("offset") int offset, @Param("limit") int limit,
                                            @Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
}
