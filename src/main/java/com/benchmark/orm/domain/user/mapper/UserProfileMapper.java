package com.benchmark.orm.domain.user.mapper;

import com.benchmark.orm.domain.user.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserProfileMapper {

    void insert(UserProfile profile);

    void update(UserProfile profile);

    void deleteById(Long id);

    UserProfile findById(Long id);

    List<UserProfile> findAll();

    List<UserProfile> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    List<UserProfile> findAllWithSorting(@Param("sortColumn") String sortColumn,
                                         @Param("sortDirection") String sortDirection);

    List<UserProfile> findAllWithPagingAndSorting(@Param("offset") int offset,
                                                  @Param("limit") int limit,
                                                  @Param("sortColumn") String sortColumn,
                                                  @Param("sortDirection") String sortDirection);
}
