package com.benchmark.orm.domain.user.service;

import com.benchmark.orm.domain.user.dto.UserRequestDto;
import com.benchmark.orm.domain.user.dto.UserResponseDto;
import com.benchmark.orm.domain.user.dto.UserSearchDto;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.mapper.UserMapper;
import com.benchmark.orm.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserService 인터페이스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto saveUserJpa(UserRequestDto userDto) {
        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);
        return UserResponseDto.fromEntity(savedUser);
    }

    @Override
    @Transactional
    public String saveUserMyBatis(UserRequestDto userDto) {
        User user = userDto.toEntity();
        userMapper.insert(user);
        return "User created successfully with MyBatis";
    }

    @Override
    public Optional<UserResponseDto> findUserByIdJpa(Long id) {
        return userRepository.findById(id)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public UserResponseDto findUserByIdMyBatis(Long id) {
        User user = userMapper.findById(id);
        return user != null ? UserResponseDto.fromEntity(user) : null;
    }

    @Override
    public List<UserResponseDto> findAllUsersJpa() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> findAllUsersMyBatis() {
        return userMapper.findAll().stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDto> findUserByEmailJpql(String email) {
        return userRepository.findByEmailJpql(email)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Optional<UserResponseDto> findUserByEmailQueryDsl(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Optional<UserResponseDto> findUserByUsernameJpql(String username) {
        return userRepository.findByUsernameJpql(username)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Optional<UserResponseDto> findUserByUsernameQueryDsl(String username) {
        return userRepository.findByUsername(username)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Page<UserResponseDto> findUsersWithPagingJpa(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userDtos = userPage.getContent().stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(userDtos, pageable, userPage.getTotalElements());
    }

    @Override
    public Page<UserResponseDto> findUsersWithPagingQueryDsl(Pageable pageable) {
        Page<User> userPage = userRepository.findAllWithPaging(pageable);

        List<UserResponseDto> userDtos = userPage.getContent().stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(userDtos, pageable, userPage.getTotalElements());
    }

    @Override
    public List<UserResponseDto> findUsersWithPagingMyBatis(int offset, int limit) {
        return userMapper.findAllWithPaging(offset, limit).stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> findUsersWithSortingJpa(Sort sort) {
        return userRepository.findAll(sort).stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> findUsersWithSortingQueryDsl(Sort sort) {
        return userRepository.findAllWithSorting(sort).stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> findUsersWithSortingMyBatis(String sortColumn, String sortDirection) {
        return userMapper.findAllWithSorting(sortColumn, sortDirection).stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponseDto> findUsersWithPagingAndSortingJpa(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userDtos = userPage.getContent().stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(userDtos, pageable, userPage.getTotalElements());
    }

    @Override
    public List<UserResponseDto> findUsersWithPagingAndSortingMyBatis(int offset, int limit, String sortColumn, String sortDirection) {
        return userMapper.findAllWithPagingAndSorting(offset, limit, sortColumn, sortDirection).stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDto> findUserWithProfileJpql(Long userId) {
        return userRepository.findUserWithProfileJpql(userId)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Optional<UserResponseDto> findUserWithProfileQueryDsl(Long userId) {
        return userRepository.findUserWithProfile(userId)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Optional<UserResponseDto> findUserWithAddressesJpql(Long userId) {
        return userRepository.findUserWithAddressesJpql(userId)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Optional<UserResponseDto> findUserWithAddressesQueryDsl(Long userId) {
        return userRepository.findUserWithAddresses(userId)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public Optional<UserResponseDto> findUserWithProfileAndAddressesQueryDsl(Long userId) {
        return userRepository.findUserWithProfileAndAddresses(userId)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public List<UserResponseDto> searchUsersByKeywordJpql(String keyword) {
        return userRepository.searchUsersByKeywordJpql(keyword).stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> searchUsers(UserSearchDto searchDto) {
        // 검색 조건에 따라 동적으로 쿼리 생성 (QueryDSL 활용)
        // 단순 구현으로 키워드 검색만 수행
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().isEmpty()) {
            return searchUsersByKeywordJpql(searchDto.getKeyword());
        }

        // 기본적으로 모든 사용자 반환
        return findAllUsersJpa();
    }

    @Override
    @Transactional
    public UserResponseDto updateUserJpa(Long id, UserRequestDto userDto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // ID 설정하여 새 엔티티로 변환
                    User updatedUser = userDto.toEntity();

                    // 업데이트된 유저 저장
                    User savedUser = userRepository.save(updatedUser);
                    return UserResponseDto.fromEntity(savedUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public String updateUserMyBatis(Long id, UserRequestDto userDto) {
        User existingUser = userMapper.findById(id);

        if (existingUser == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        // ID 설정하여 새 엔티티로 변환
        User updatedUser = userDto.toEntity();
        userMapper.update(updatedUser);

        return "User updated successfully with MyBatis";
    }

    @Override
    @Transactional
    public String deleteUserJpa(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return "User deleted successfully with JPA";
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public String deleteUserMyBatis(Long id) {
        User existingUser = userMapper.findById(id);

        if (existingUser == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userMapper.deleteById(id);
        return "User deleted successfully with MyBatis";
    }
}