package com.benchmark.orm.domain.user.service;

import com.benchmark.orm.domain.user.dto.*;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.mapper.UserMapper;
import com.benchmark.orm.global.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMyBatisService {

    private final UserMapper userMapper;

    /**
     * 모든 사용자 조회 (간단 버전)
     */
    public List<UserSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<User> users = userMapper.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, users.size());

        return users.stream()
                .map(UserSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 모든 사용자 조회 (상세 버전)
     */
    public List<UserResponseDto> findAllDetailed() {
        long startTime = System.currentTimeMillis();
        List<User> users = userMapper.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findAllDetailed - 실행시간: {}ms, 결과 수: {}", endTime - startTime, users.size());

        return users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 사용자 조회
     */
    public UserResponseDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        User user = userMapper.findById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findById - 실행시간: {}ms", endTime - startTime);

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + id);
        }
        return UserResponseDto.fromEntity(user);
    }

    /**
     * 이메일로 사용자 조회
     */
    public UserResponseDto findByEmail(String email) {
        long startTime = System.currentTimeMillis();
        User user = userMapper.findByEmail(email);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findByEmail - 실행시간: {}ms", endTime - startTime);

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. 이메일: " + email);
        }
        return UserResponseDto.fromEntity(user);
    }

    /**
     * 사용자명으로 사용자 조회
     */
    public UserResponseDto findByUsername(String username) {
        long startTime = System.currentTimeMillis();
        User user = userMapper.findByUsername(username);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findByUsername - 실행시간: {}ms", endTime - startTime);

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. 사용자명: " + username);
        }
        return UserResponseDto.fromEntity(user);
    }

    /**
     * 페이징 조회
     */
    public PageDto<UserSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        List<User> users = userMapper.findAllWithPagingAndSorting(offset, size, sortBy, sortDirection);

        // 전체 개수 조회
        List<User> allUsers = userMapper.findAll();
        long totalElements = allUsers.size();

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findAllWithPaging - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, users.size(), totalElements);

        List<UserSimpleDto> content = users.stream()
                .map(UserSimpleDto::from)
                .collect(Collectors.toList());

        return PageDto.of(content, page, size, totalElements);
    }

    /**
     * 정렬 조회
     */
    public List<UserSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();
        List<User> users = userMapper.findAllWithSorting(sortBy, sortDirection);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, users.size());

        return users.stream()
                .map(UserSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 프로필 정보와 함께 사용자 조회
     */
    public UserResponseDto findUserWithProfile(Long userId) {
        long startTime = System.currentTimeMillis();
        User user = userMapper.findUserWithProfile(userId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findUserWithProfile - 실행시간: {}ms", endTime - startTime);

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        return UserResponseDto.fromEntity(user);
    }

    /**
     * 주소 정보와 함께 사용자 조회
     */
    public UserResponseDto findUserWithAddresses(Long userId) {
        long startTime = System.currentTimeMillis();
        User user = userMapper.findUserWithAddresses(userId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User findUserWithAddresses - 실행시간: {}ms", endTime - startTime);

        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        return UserResponseDto.fromEntity(user);
    }

    /**
     * 검색 조건으로 사용자 검색
     */
    public PageDto<UserSimpleDto> searchUsers(UserSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        List<User> users = userMapper.searchUsers(searchDto, offset, size, sortBy, sortDirection);
        int totalCount = userMapper.countBySearchDto(searchDto);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User searchUsers - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, users.size(), totalCount);

        List<UserSimpleDto> content = users.stream()
                .map(UserSimpleDto::from)
                .collect(Collectors.toList());

        return PageDto.of(content, page, size, totalCount);
    }

    /**
     * 사용자 생성
     */
    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        User user = requestDto.toEntity();
        userMapper.insert(user);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User createUser - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(user);
    }

    /**
     * 사용자 수정
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + id);
        }

        // 기존 사용자 정보 업데이트
        if (requestDto.getUsername() != null || requestDto.getEmail() != null) {
            existingUser.updateInfo(
                    requestDto.getUsername() != null ? requestDto.getUsername() : existingUser.getUsername(),
                    requestDto.getEmail() != null ? requestDto.getEmail() : existingUser.getEmail()
            );
        }

        userMapper.update(existingUser);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User updateUser - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(existingUser);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(Long id) {
        long startTime = System.currentTimeMillis();

        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + id);
        }

        userMapper.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] User deleteUser - 실행시간: {}ms", endTime - startTime);
    }
}