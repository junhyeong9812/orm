package com.benchmark.orm.domain.product.entity;

import com.benchmark.orm.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private boolean isThumbnail;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
