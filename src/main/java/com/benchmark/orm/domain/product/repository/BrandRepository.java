package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
