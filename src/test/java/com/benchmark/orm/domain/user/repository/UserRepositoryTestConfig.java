package com.benchmark.orm.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * UserRepository 테스트를 위한 설정 클래스
 * <p>
 * QueryDSL 사용을 위한 JPAQueryFactory 빈 등록
 */
@TestConfiguration
public class UserRepositoryTestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * QueryDSL의 JPAQueryFactory 빈 등록
     *
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}