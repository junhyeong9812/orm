package com.benchmark.orm.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {
    private String keyword; // 사용자명이나 이메일로 검색
    private String username;
    private String email;

    // 정렬 관련 필드
    private String sortBy; // 정렬 기준 필드
    private String sortDirection; // 정렬 방향 (asc/desc)
}