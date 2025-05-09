package com.benchmark.orm.domain.user.dto;

import com.benchmark.orm.domain.user.entity.Address;
import com.benchmark.orm.domain.user.entity.Image;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private UserProfileDto profile;
    private List<AddressDto> addresses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 엔티티로부터 DTO 생성
     * @param user 사용자 엔티티
     * @return 사용자 응답 DTO
     */
    public static UserResponseDto fromEntity(User user) {
        if (user == null) {
            return null;
        }

        List<AddressDto> addressDtos = new ArrayList<>();

        if (user.getAddresses() != null) {
            addressDtos = user.getAddresses().stream()
                    .map(AddressDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profile(UserProfileDto.fromEntity(user.getProfile()))
                .addresses(addressDtos)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * 주소 DTO 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private Long id;
        private String zipcode;
        private String detail;
        private boolean isDefault;

        /**
         * 엔티티로부터 DTO 생성
         * @param address 주소 엔티티
         * @return 주소 DTO
         */
        public static AddressDto fromEntity(Address address) {
            if (address == null) {
                return null;
            }

            return AddressDto.builder()
                    .id(address.getId())
                    .zipcode(address.getZipcode())
                    .detail(address.getDetail())
                    .isDefault(address.isDefault())
                    .build();
        }
    }

    /**
     * 이미지 DTO 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageDto {
        private Long id;
        private String url;
        private String altText;

        /**
         * 엔티티로부터 DTO 생성
         * @param image 이미지 엔티티
         * @return 이미지 DTO
         */
        public static ImageDto fromEntity(Image image) {
            if (image == null) {
                return null;
            }

            return ImageDto.builder()
                    .id(image.getId())
                    .url(image.getUrl())
                    .altText(image.getAltText())
                    .build();
        }
    }

    /**
     * 사용자 프로필 DTO 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileDto {
        private Long id;
        private String nickname;
        private String gender;
        private ImageDto profileImage;

        /**
         * 엔티티로부터 DTO 생성
         * @param profile 사용자 프로필 엔티티
         * @return 사용자 프로필 DTO
         */
        public static UserProfileDto fromEntity(UserProfile profile) {
            if (profile == null) {
                return null;
            }

            return UserProfileDto.builder()
                    .id(profile.getId())
                    .nickname(profile.getNickname())
                    .gender(profile.getGender())
                    .profileImage(ImageDto.fromEntity(profile.getProfileImage()))
                    .build();
        }
    }
}