package com.benchmark.orm.domain.user.dto;

import com.benchmark.orm.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 간단한 사용자 DTO (성능 비교용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSimpleDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity to DTO
    public static UserSimpleDto from(User user) {
        return UserSimpleDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}