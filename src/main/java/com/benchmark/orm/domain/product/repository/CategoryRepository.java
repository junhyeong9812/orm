package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
