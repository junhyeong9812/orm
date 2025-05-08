package com.benchmark.orm.domain.user.entity;

import com.benchmark.orm.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zipcode;
    private String detail;
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
