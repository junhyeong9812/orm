package com.benchmark.orm.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 검색 조건 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {
    private String keyword;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}