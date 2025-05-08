package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
