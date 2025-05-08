package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
