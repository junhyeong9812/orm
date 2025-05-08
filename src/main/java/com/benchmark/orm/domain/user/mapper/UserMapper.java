package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    void insert(User user);

    void update(User user);

    void deleteById(Long id);

    User findById(Long id);

    List<User> findAll();

    List<User> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    List<User> findAllWithSorting(@Param("sortColumn") String sortColumn,
                                  @Param("sortDirection") String sortDirection);

    List<User> findAllWithPagingAndSorting(@Param("offset") int offset,
                                           @Param("limit") int limit,
                                           @Param("sortColumn") String sortColumn,
                                           @Param("sortDirection") String sortDirection);
}
