package com.benchmark.orm.domain.user.controller;

import com.benchmark.orm.domain.user.dto.*;
import com.benchmark.orm.domain.user.service.UserJpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/jpa/user")
@RequiredArgsConstructor
public class UserJpaController {

    private final UserJpaService userJpaService;

    /**
     * 모든 사용자 조회 (간단 버전)
     */
    @GetMapping
    public ResponseEntity<List<UserSimpleDto>> getAllUsers() {
        log.info("[JPA] GET /api/jpa/user - 모든 사용자 조회 요청");
        List<UserSimpleDto> users = userJpaService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * 모든 사용자 조회 (상세 버전)
     */
    @GetMapping("/detailed")
    public ResponseEntity<List<UserResponseDto>> getAllUsersDetailed() {
        log.info("[JPA] GET /api/jpa/user/detailed - 모든 사용자 상세 조회 요청");
        List<UserResponseDto> users = userJpaService.findAllDetailed();
        return ResponseEntity.ok(users);
    }

    /**
     * ID로 사용자 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/user/{} - ID로 사용자 조회 요청", id);
        UserResponseDto user = userJpaService.findById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 이메일로 사용자 조회
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        log.info("[JPA] GET /api/jpa/user/email/{} - 이메일로 사용자 조회 요청", email);
        UserResponseDto user = userJpaService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * 사용자명으로 사용자 조회
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        log.info("[JPA] GET /api/jpa/user/username/{} - 사용자명으로 조회 요청", username);
        UserResponseDto user = userJpaService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * 페이징 조회
     */
    @GetMapping("/paging")
    public ResponseEntity<Page<UserSimpleDto>> getUsersWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[JPA] GET /api/jpa/user/paging - 페이징 조회 요청: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);
        Page<UserSimpleDto> users = userJpaService.findAllWithPaging(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }

    /**
     * 정렬 조회
     */
    @GetMapping("/sorting")
    public ResponseEntity<List<UserSimpleDto>> getUsersWithSorting(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[JPA] GET /api/jpa/user/sorting - 정렬 조회 요청: sortBy={}, sortDirection={}",
                sortBy, sortDirection);
        List<UserSimpleDto> users = userJpaService.findAllWithSorting(sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }

    /**
     * 프로필 정보와 함께 사용자 조회
     */
    @GetMapping("/{id}/with-profile")
    public ResponseEntity<UserResponseDto> getUserWithProfile(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/user/{}/with-profile - 프로필 정보와 함께 조회 요청", id);
        UserResponseDto user = userJpaService.findUserWithProfile(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 주소 정보와 함께 사용자 조회
     */
    @GetMapping("/{id}/with-addresses")
    public ResponseEntity<UserResponseDto> getUserWithAddresses(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/user/{}/with-addresses - 주소 정보와 함께 조회 요청", id);
        UserResponseDto user = userJpaService.findUserWithAddresses(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 프로필과 주소 정보 모두 포함한 사용자 조회
     */
    @GetMapping("/{id}/with-all-details")
    public ResponseEntity<UserResponseDto> getUserWithProfileAndAddresses(@PathVariable Long id) {
        log.info("[JPA] GET /api/jpa/user/{}/with-all-details - 모든 상세 정보와 함께 조회 요청", id);
        UserResponseDto user = userJpaService.findUserWithProfileAndAddresses(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 검색 조건으로 사용자 검색
     */
    @GetMapping("/search")
    public ResponseEntity<Page<UserSimpleDto>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        log.info("[JPA] GET /api/jpa/user/search - 검색 요청: keyword={}, username={}, email={}",
                keyword, username, email);

        UserSearchDto searchDto = UserSearchDto.builder()
                .keyword(keyword)
                .username(username)
                .email(email)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        Page<UserSimpleDto> users = userJpaService.searchUsers(searchDto, page, size);
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 생성
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        log.info("[JPA] POST /api/jpa/user - 사용자 생성 요청: {}", requestDto.getUsername());
        UserResponseDto user = userJpaService.createUser(requestDto);
        return ResponseEntity.ok(user);
    }

    /**
     * 사용자 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto requestDto) {
        log.info("[JPA] PUT /api/jpa/user/{} - 사용자 수정 요청", id);
        UserResponseDto user = userJpaService.updateUser(id, requestDto);
        return ResponseEntity.ok(user);
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("[JPA] DELETE /api/jpa/user/{} - 사용자 삭제 요청", id);
        userJpaService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}