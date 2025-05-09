package com.benchmark.orm.domain.user.controller;

import com.benchmark.orm.domain.user.dto.UserRequestDto;
import com.benchmark.orm.domain.user.dto.UserResponseDto;
import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 사용자 관련 API 컨트롤러
 * 각 ORM 기술별 성능 비교를 위한 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * JPA로 사용자 생성
     * @param userRequestDto 사용자 요청 DTO
     * @return 생성된 사용자 정보
     */
    @PostMapping("/jpa")
    public ResponseEntity<UserResponseDto> createUserJpa(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto savedUser = userService.saveUserJpa(userRequestDto);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * MyBatis로 사용자 생성
     * @param userRequestDto 사용자 요청 DTO
     * @return 결과 메시지
     */
    @PostMapping("/mybatis")
    public ResponseEntity<Map<String, String>> createUserMyBatis(@RequestBody UserRequestDto userRequestDto) {
        String result = userService.saveUserMyBatis(userRequestDto);
        return ResponseEntity.ok(Map.of("message", result));
    }

    /**
     * JPA로 ID별 사용자 조회
     * @param id 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/jpa/{id}")
    public ResponseEntity<UserResponseDto> getUserByIdJpa(@PathVariable Long id) {
        return userService.findUserByIdJpa(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * MyBatis로 ID별 사용자 조회
     * @param id 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/mybatis/{id}")
    public ResponseEntity<UserResponseDto> getUserByIdMyBatis(@PathVariable Long id) {
        UserResponseDto user = userService.findUserByIdMyBatis(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    /**
     * JPA로 모든 사용자 조회
     * @return 사용자 목록
     */
    @GetMapping("/jpa")
    public ResponseEntity<List<UserResponseDto>> getAllUsersJpa() {
        return ResponseEntity.ok(userService.findAllUsersJpa());
    }

    /**
     * MyBatis로 모든 사용자 조회
     * @return 사용자 목록
     */
    @GetMapping("/mybatis")
    public ResponseEntity<List<UserResponseDto>> getAllUsersMyBatis() {
        return ResponseEntity.ok(userService.findAllUsersMyBatis());
    }

    /**
     * JPQL로 이메일별 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 정보
     */
    @GetMapping("/jpql/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmailJpql(@PathVariable String email) {
        return userService.findUserByEmailJpql(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 이메일별 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 정보
     */
    @GetMapping("/querydsl/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmailQueryDsl(@PathVariable String email) {
        return userService.findUserByEmailQueryDsl(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 사용자명별 사용자 조회
     * @param username 사용자명
     * @return 사용자 정보
     */
    @GetMapping("/jpql/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsernameJpql(@PathVariable String username) {
        return userService.findUserByUsernameJpql(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 사용자명별 사용자 조회
     * @param username 사용자명
     * @return 사용자 정보
     */
    @GetMapping("/querydsl/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsernameQueryDsl(@PathVariable String username) {
        return userService.findUserByUsernameQueryDsl(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPA로 페이징된 사용자 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 사용자 정보
     */
    @GetMapping("/jpa/paging")
    public ResponseEntity<Page<UserResponseDto>> getUsersWithPagingJpa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.findUsersWithPagingJpa(PageRequest.of(page, size)));
    }

    /**
     * QueryDSL로 페이징된 사용자 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 사용자 정보
     */
    @GetMapping("/querydsl/paging")
    public ResponseEntity<Page<UserResponseDto>> getUsersWithPagingQueryDsl(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.findUsersWithPagingQueryDsl(PageRequest.of(page, size)));
    }

    /**
     * MyBatis로 페이징된 사용자 조회
     * @param offset 시작 위치
     * @param limit 제한
     * @return 페이징된 사용자 정보
     */
    @GetMapping("/mybatis/paging")
    public ResponseEntity<List<UserResponseDto>> getUsersWithPagingMyBatis(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(userService.findUsersWithPagingMyBatis(offset, limit));
    }

    /**
     * JPA로 정렬된 사용자 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 사용자 정보
     */
    @GetMapping("/jpa/sorting")
    public ResponseEntity<List<UserResponseDto>> getUsersWithSortingJpa(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(userService.findUsersWithSortingJpa(sort));
    }

    /**
     * QueryDSL로 정렬된 사용자 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 사용자 정보
     */
    @GetMapping("/querydsl/sorting")
    public ResponseEntity<List<UserResponseDto>> getUsersWithSortingQueryDsl(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(userService.findUsersWithSortingQueryDsl(sort));
    }

    /**
     * MyBatis로 정렬된 사용자 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 사용자 정보
     */
    @GetMapping("/mybatis/sorting")
    public ResponseEntity<List<UserResponseDto>> getUsersWithSortingMyBatis(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(userService.findUsersWithSortingMyBatis(sortBy, direction));
    }

    /**
     * JPA로 페이징 및 정렬된 사용자 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 페이징 및 정렬된 사용자 정보
     */
    @GetMapping("/jpa/paging-sorting")
    public ResponseEntity<Page<UserResponseDto>> getUsersWithPagingAndSortingJpa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return ResponseEntity.ok(userService.findUsersWithPagingAndSortingJpa(
                PageRequest.of(page, size, Sort.by(sortDirection, sortBy))));
    }

    /**
     * MyBatis로 페이징 및 정렬된 사용자 조회
     * @param offset 시작 위치
     * @param limit 제한
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 페이징 및 정렬된 사용자 정보
     */
    @GetMapping("/mybatis/paging-sorting")
    public ResponseEntity<List<UserResponseDto>> getUsersWithPagingAndSortingMyBatis(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(userService.findUsersWithPagingAndSortingMyBatis(
                offset, limit, sortBy, direction));
    }

    /**
     * JPQL로 프로필과 함께 사용자 조회
     * @param id 사용자 ID
     * @return 프로필이 포함된 사용자 정보
     */
    @GetMapping("/jpql/{id}/with-profile")
    public ResponseEntity<UserResponseDto> getUserWithProfileJpql(@PathVariable Long id) {
        return userService.findUserWithProfileJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 프로필과 함께 사용자 조회
     * @param id 사용자 ID
     * @return 프로필이 포함된 사용자 정보
     */
    @GetMapping("/querydsl/{id}/with-profile")
    public ResponseEntity<UserResponseDto> getUserWithProfileQueryDsl(@PathVariable Long id) {
        return userService.findUserWithProfileQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 주소와 함께 사용자 조회
     * @param id 사용자 ID
     * @return 주소가 포함된 사용자 정보
     */
    @GetMapping("/jpql/{id}/with-addresses")
    public ResponseEntity<UserResponseDto> getUserWithAddressesJpql(@PathVariable Long id) {
        return userService.findUserWithAddressesJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 주소와 함께 사용자 조회
     * @param id 사용자 ID
     * @return 주소가 포함된 사용자 정보
     */
    @GetMapping("/querydsl/{id}/with-addresses")
    public ResponseEntity<UserResponseDto> getUserWithAddressesQueryDsl(@PathVariable Long id) {
        return userService.findUserWithAddressesQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 프로필 및 주소와 함께 사용자 조회
     * @param id 사용자 ID
     * @return 프로필 및 주소가 포함된 사용자 정보
     */
    @GetMapping("/querydsl/{id}/with-profile-addresses")
    public ResponseEntity<UserResponseDto> getUserWithProfileAndAddressesQueryDsl(@PathVariable Long id) {
        return userService.findUserWithProfileAndAddressesQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 키워드 기반 사용자 검색
     * @param keyword 검색 키워드
     * @return 검색된 사용자 목록
     */
    @GetMapping("/jpql/search")
    public ResponseEntity<List<UserResponseDto>> searchUsersByKeywordJpql(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsersByKeywordJpql(keyword));
    }

    /**
     * 검색 조건으로 사용자 검색
     * @param searchDto 검색 조건 DTO
     * @return 검색된 사용자 목록
     */
    @PostMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(@RequestBody UserSearchDto searchDto) {
        return ResponseEntity.ok(userService.searchUsers(searchDto));
    }

    /**
     * JPA로 사용자 정보 업데이트
     * @param id 사용자 ID
     * @param userRequestDto 업데이트할 사용자 DTO
     * @return 업데이트된 사용자 정보
     */
    @PutMapping("/jpa/{id}")
    public ResponseEntity<UserResponseDto> updateUserJpa(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto updatedUser = userService.updateUserJpa(id, userRequestDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * MyBatis로 사용자 정보 업데이트
     * @param id 사용자 ID
     * @param userRequestDto 업데이트할 사용자 DTO
     * @return 결과 메시지
     */
    @PutMapping("/mybatis/{id}")
    public ResponseEntity<Map<String, String>> updateUserMyBatis(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        try {
            String result = userService.updateUserMyBatis(id, userRequestDto);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * JPA로 사용자 삭제
     * @param id 사용자 ID
     * @return 결과 메시지
     */
    @DeleteMapping("/jpa/{id}")
    public ResponseEntity<Map<String, String>> deleteUserJpa(@PathVariable Long id) {
        try {
            String result = userService.deleteUserJpa(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * MyBatis로 사용자 삭제
     * @param id 사용자 ID
     * @return 결과 메시지
     */
    @DeleteMapping("/mybatis/{id}")
    public ResponseEntity<Map<String, String>> deleteUserMyBatis(@PathVariable Long id) {
        try {
            String result = userService.deleteUserMyBatis(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}