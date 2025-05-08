package com.benchmark.orm.domain.user.repository;

import com.benchmark.orm.domain.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
