package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    void insert(Address address);
    void update(Address address);
    void deleteById(Long id);
    Address findById(Long id);
    List<Address> findAll();
    List<Address> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    List<Address> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
    List<Address> findAllWithPagingAndSorting(@Param("offset") int offset, @Param("limit") int limit,
                                              @Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);
}
