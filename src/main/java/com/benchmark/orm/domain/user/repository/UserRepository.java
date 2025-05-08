package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
