package com.benchmark.orm.domain.product.mapper;

import com.benchmark.orm.domain.product.dto.ProductSearchDto;
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
    List<Product> findAllWithPagingAndSorting(@Param("offset") int offset,
                                              @Param("limit") int limit,
                                              @Param("sortColumn") String sortColumn,
                                              @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 상품 검색
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 검색된 상품 리스트
     */
    List<Product> searchProducts(@Param("searchDto") ProductSearchDto searchDto,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit,
                                 @Param("sortColumn") String sortColumn,
                                 @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 상품 총 개수 조회
     * @param searchDto 검색 조건 DTO
     * @return 검색된 상품 총 개수
     */
    int countBySearchDto(@Param("searchDto") ProductSearchDto searchDto);
}