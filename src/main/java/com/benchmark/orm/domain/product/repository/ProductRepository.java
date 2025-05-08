package com.benchmark.orm.domain.product.repository;

import com.benchmark.orm.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
