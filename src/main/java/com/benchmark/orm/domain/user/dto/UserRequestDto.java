package com.benchmark.orm.domain.user.dto;

import com.benchmark.orm.domain.user.entity.Address;
import com.benchmark.orm.domain.user.entity.Image;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 사용자 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    private String username;
    private String email;
    private UserProfileDto profile;
    private List<AddressDto> addresses;

    /**
     * DTO를 엔티티로 변환
     * @return User 엔티티
     */
    public User toEntity() {
        User user = new User();

        // User 필드 설정
        if (id != null) {
            try {
                // id 설정을 위한 Reflection 또는 Setter 사용
                java.lang.reflect.Field idField = User.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(user, id);
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }
        }

        // username과 email 설정을 위한 Reflection 또는 Setter 사용
        try {
            java.lang.reflect.Field usernameField = User.class.getDeclaredField("username");
            usernameField.setAccessible(true);
            usernameField.set(user, username);

            java.lang.reflect.Field emailField = User.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(user, email);
        } catch (Exception e) {
            // Reflection 실패 시 무시
        }

        return user;
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
         * DTO를 엔티티로 변환
         * @param user 사용자 엔티티 (연관관계 설정용)
         * @return Address 엔티티
         */
        public Address toEntity(User user) {
            Address address = new Address();

            // Address 필드 설정을 위한 Reflection 또는 Setter 사용
            try {
                if (id != null) {
                    java.lang.reflect.Field idField = Address.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(address, id);
                }

                java.lang.reflect.Field zipcodeField = Address.class.getDeclaredField("zipcode");
                zipcodeField.setAccessible(true);
                zipcodeField.set(address, zipcode);

                java.lang.reflect.Field detailField = Address.class.getDeclaredField("detail");
                detailField.setAccessible(true);
                detailField.set(address, detail);

                java.lang.reflect.Field isDefaultField = Address.class.getDeclaredField("isDefault");
                isDefaultField.setAccessible(true);
                isDefaultField.set(address, isDefault);

                java.lang.reflect.Field userField = Address.class.getDeclaredField("user");
                userField.setAccessible(true);
                userField.set(address, user);
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }

            return address;
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
         * DTO를 엔티티로 변환
         * @return Image 엔티티
         */
        public Image toEntity() {
            Image image = new Image();

            // Image 필드 설정을 위한 Reflection 또는 Setter 사용
            try {
                if (id != null) {
                    java.lang.reflect.Field idField = Image.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(image, id);
                }

                java.lang.reflect.Field urlField = Image.class.getDeclaredField("url");
                urlField.setAccessible(true);
                urlField.set(image, url);

                java.lang.reflect.Field altTextField = Image.class.getDeclaredField("altText");
                altTextField.setAccessible(true);
                altTextField.set(image, altText);
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }

            return image;
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
         * DTO를 엔티티로 변환
         * @param user 사용자 엔티티 (연관관계 설정용)
         * @return UserProfile 엔티티
         */
        public UserProfile toEntity(User user) {
            UserProfile profile = new UserProfile();

            // UserProfile 필드 설정을 위한 Reflection 또는 Setter 사용
            try {
                if (id != null) {
                    java.lang.reflect.Field idField = UserProfile.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(profile, id);
                }

                java.lang.reflect.Field nicknameField = UserProfile.class.getDeclaredField("nickname");
                nicknameField.setAccessible(true);
                nicknameField.set(profile, nickname);

                java.lang.reflect.Field genderField = UserProfile.class.getDeclaredField("gender");
                genderField.setAccessible(true);
                genderField.set(profile, gender);

                java.lang.reflect.Field userField = UserProfile.class.getDeclaredField("user");
                userField.setAccessible(true);
                userField.set(profile, user);

                if (profileImage != null) {
                    java.lang.reflect.Field profileImageField = UserProfile.class.getDeclaredField("profileImage");
                    profileImageField.setAccessible(true);
                    profileImageField.set(profile, profileImage.toEntity());
                }
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }

            return profile;
        }
    }
}