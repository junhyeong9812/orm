package com.benchmark.orm.domain.user.service;

import com.benchmark.orm.domain.user.dto.*;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserJpaService {

    private final UserRepository userRepository;

    /**
     * 모든 사용자 조회 (간단 버전)
     */
    public List<UserSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<User> users = userRepository.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, users.size());

        return users.stream()
                .map(UserSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 모든 사용자 조회 (상세 버전)
     */
    public List<UserResponseDto> findAllDetailed() {
        long startTime = System.currentTimeMillis();
        List<User> users = userRepository.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findAllDetailed - 실행시간: {}ms, 결과 수: {}", endTime - startTime, users.size());

        return users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 사용자 조회
     */
    public UserResponseDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + id));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findById - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(user);
    }

    /**
     * 이메일로 사용자 조회
     */
    public UserResponseDto findByEmail(String email) {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. 이메일: " + email));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findByEmail - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(user);
    }

    /**
     * 사용자명으로 사용자 조회
     */
    public UserResponseDto findByUsername(String username) {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. 사용자명: " + username));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findByUsername - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(user);
    }

    /**
     * 페이징 조회
     */
    public Page<UserSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users = userRepository.findAllWithPaging(pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findAllWithPaging - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, users.getTotalElements());

        return users.map(UserSimpleDto::from);
    }

    /**
     * 정렬 조회
     */
    public List<UserSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        List<User> users = userRepository.findAllWithSorting(sort);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
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
        User user = userRepository.findUserWithProfile(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findUserWithProfile - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(user);
    }

    /**
     * 주소 정보와 함께 사용자 조회
     */
    public UserResponseDto findUserWithAddresses(Long userId) {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findUserWithAddresses(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findUserWithAddresses - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(user);
    }

    /**
     * 프로필과 주소 정보 모두 포함한 사용자 조회
     */
    public UserResponseDto findUserWithProfileAndAddresses(Long userId) {
        long startTime = System.currentTimeMillis();
        User user = userRepository.findUserWithProfileAndAddresses(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User findUserWithProfileAndAddresses - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(user);
    }

    /**
     * 검색 조건으로 사용자 검색
     */
    public Page<UserSimpleDto> searchUsers(UserSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users = userRepository.searchUsers(searchDto, pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User searchUsers - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, users.getTotalElements());

        return users.map(UserSimpleDto::from);
    }

    /**
     * 사용자 생성
     */
    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        User user = requestDto.toEntity();
        User savedUser = userRepository.save(user);

        long endTime = System.currentTimeMillis();

        log.info("[JPA] User createUser - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(savedUser);
    }

    /**
     * 사용자 수정
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + id));

        if (requestDto.getUsername() != null || requestDto.getEmail() != null) {
            user.updateInfo(
                    requestDto.getUsername() != null ? requestDto.getUsername() : user.getUsername(),
                    requestDto.getEmail() != null ? requestDto.getEmail() : user.getEmail()
            );
        }

        User savedUser = userRepository.save(user);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User updateUser - 실행시간: {}ms", endTime - startTime);

        return UserResponseDto.fromEntity(savedUser);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(Long id) {
        long startTime = System.currentTimeMillis();

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. ID: " + id);
        }

        userRepository.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] User deleteUser - 실행시간: {}ms", endTime - startTime);
    }
}