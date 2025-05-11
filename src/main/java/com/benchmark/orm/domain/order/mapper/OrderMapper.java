package com.benchmark.orm.domain.order.mapper;

import com.benchmark.orm.domain.order.dto.OrderSearchDto;
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
    List<Order> findAllWithPagingAndSorting(@Param("offset") int offset,
                                            @Param("limit") int limit,
                                            @Param("sortColumn") String sortColumn,
                                            @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 주문 검색
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 검색된 주문 리스트
     */
    List<Order> searchOrders(@Param("searchDto") OrderSearchDto searchDto,
                             @Param("offset") int offset,
                             @Param("limit") int limit,
                             @Param("sortColumn") String sortColumn,
                             @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 주문 총 개수 조회
     * @param searchDto 검색 조건 DTO
     * @return 검색된 주문 총 개수
     */
    int countBySearchDto(@Param("searchDto") OrderSearchDto searchDto);
}