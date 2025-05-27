package com.benchmark.orm.domain.user.controller;

import com.benchmark.orm.domain.user.dto.*;
import com.benchmark.orm.domain.user.service.UserMyBatisService;
import com.benchmark.orm.global.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/mybatis/user")
@RequiredArgsConstructor
public class UserMyBatisController {

    private final UserMyBatisService userMyBatisService;

    /**
     * 모든 사용자 조회 (간단 버전)
     */
    @GetMapping
    public ResponseEntity<List<UserSimpleDto>> getAllUsers() {
        log.info("[MyBatis] GET /api/mybatis/user - 모든 사용자 조회 요청");
        List<UserSimpleDto> users = userMyBatisService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * 모든 사용자 조회 (상세 버전)
     */
    @GetMapping("/detailed")
    public ResponseEntity<List<UserResponseDto>> getAllUsersDetailed() {
        log.info("[MyBatis] GET /api/mybatis/user/detailed - 모든 사용자 상세 조회 요청");
        List<UserResponseDto> users = userMyBatisService.findAllDetailed();
        return ResponseEntity.ok(users);
    }

    /**
     * ID로 사용자 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("[MyBatis] GET /api/mybatis/user/{} - ID로 사용자 조회 요청", id);
        UserResponseDto user = userMyBatisService.findById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 이메일로 사용자 조회
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        log.info("[MyBatis] GET /api/mybatis/user/email/{} - 이메일로 사용자 조회 요청", email);
        UserResponseDto user = userMyBatisService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * 사용자명으로 사용자 조회
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        log.info("[MyBatis] GET /api/mybatis/user/username/{} - 사용자명으로 조회 요청", username);
        UserResponseDto user = userMyBatisService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * 페이징 조회
     */
    @GetMapping("/paging")
    public ResponseEntity<PageDto<UserSimpleDto>> getUsersWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[MyBatis] GET /api/mybatis/user/paging - 페이징 조회 요청: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);
        PageDto<UserSimpleDto> users = userMyBatisService.findAllWithPaging(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }

    /**
     * 정렬 조회
     */
    @GetMapping("/sorting")
    public ResponseEntity<List<UserSimpleDto>> getUsersWithSorting(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[MyBatis] GET /api/mybatis/user/sorting - 정렬 조회 요청: sortBy={}, sortDirection={}",
                sortBy, sortDirection);
        List<UserSimpleDto> users = userMyBatisService.findAllWithSorting(sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }

    /**
     * 프로필 정보와 함께 사용자 조회
     */
    @GetMapping("/{id}/with-profile")
    public ResponseEntity<UserResponseDto> getUserWithProfile(@PathVariable Long id) {
        log.info("[MyBatis] GET /api/mybatis/user/{}/with-profile - 프로필 정보와 함께 조회 요청", id);
        UserResponseDto user = userMyBatisService.findUserWithProfile(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 주소 정보와 함께 사용자 조회
     */
    @GetMapping("/{id}/with-addresses")
    public ResponseEntity<UserResponseDto> getUserWithAddresses(@PathVariable Long id) {
        log.info("[MyBatis] GET /api/mybatis/user/{}/with-addresses - 주소 정보와 함께 조회 요청", id);
        UserResponseDto user = userMyBatisService.findUserWithAddresses(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 검색 조건으로 사용자 검색
     */
    @GetMapping("/search")
    public ResponseEntity<PageDto<UserSimpleDto>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        log.info("[MyBatis] GET /api/mybatis/user/search - 검색 요청: keyword={}, username={}, email={}",
                keyword, username, email);

        UserSearchDto searchDto = UserSearchDto.builder()
                .keyword(keyword)
                .username(username)
                .email(email)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PageDto<UserSimpleDto> users = userMyBatisService.searchUsers(searchDto, page, size);
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 생성
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        log.info("[MyBatis] POST /api/mybatis/user - 사용자 생성 요청: {}", requestDto.getUsername());
        UserResponseDto user = userMyBatisService.createUser(requestDto);
        return ResponseEntity.ok(user);
    }

    /**
     * 사용자 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto requestDto) {
        log.info("[MyBatis] PUT /api/mybatis/user/{} - 사용자 수정 요청", id);
        UserResponseDto user = userMyBatisService.updateUser(id, requestDto);
        return ResponseEntity.ok(user);
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("[MyBatis] DELETE /api/mybatis/user/{} - 사용자 삭제 요청", id);
        userMyBatisService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}