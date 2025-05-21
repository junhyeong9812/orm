# 데이터베이스 인덱스와 ORM 성능 비교

이 프로젝트는 데이터베이스 인덱스가 다양한 ORM 기술(JPA, QueryDSL, MyBatis)의 성능에 미치는 영향을 비교 분석합니다.

## 인덱스란?

데이터베이스 인덱스(Index)는 테이블의 데이터를 더 빠르게 검색하기 위한 자료구조입니다. 인덱스는 한 마디로 책의 목차와 유사한 역할을 하며, 데이터베이스 성능을 최적화하는 핵심 요소입니다.

### 인덱스의 장점

1. **검색 속도 향상**: 인덱스를 사용하면 전체 테이블을 스캔하지 않고도 빠르게 데이터를 검색할 수 있습니다.
2. **정렬 비용 감소**: 인덱스가 이미 정렬되어 있기 때문에, ORDER BY 절을 사용할 때 성능이 향상됩니다.
3. **데이터 무결성 보장**: 고유(Unique) 인덱스를 사용하면 중복 데이터 삽입을 방지할 수 있습니다.

### 인덱스의 단점

1. **추가 저장 공간 필요**: 인덱스는 별도의 데이터 구조로 저장되므로 추가 저장 공간이 필요합니다.
2. **쓰기 작업 성능 저하**: INSERT, UPDATE, DELETE 작업 시 인덱스도 함께 업데이트해야 하므로 쓰기 성능이 저하될 수 있습니다.
3. **관리 복잡성 증가**: 인덱스가 많아질수록 데이터베이스 관리가 복잡해질 수 있습니다.

## 인덱스 적용 방법

### JPA/Hibernate 인덱스 설정

JPA에서는 `@Table`과 `@Index` 애노테이션을 사용하여 인덱스를 정의할 수 있습니다.

```java
@Entity
@Table(name = "product_index", 
    indexes = {
        @Index(name = "idx_product_index_name", columnList = "name"),
        @Index(name = "idx_product_index_price", columnList = "price"),
        @Index(name = "idx_product_index_brand", columnList = "brand_id"),
        @Index(name = "idx_product_index_category", columnList = "category_id")
    })
public class ProductIndex extends BaseTimeEntity {
    // 엔티티 내용
}
```

### MyBatis 인덱스 설정

MyBatis는 직접 데이터베이스 스키마를 관리하지 않기 때문에, 데이터베이스 마이그레이션 도구나 직접 SQL 스크립트를 통해 인덱스를 생성해야 합니다.

```sql
CREATE INDEX idx_product_name ON product (name);
CREATE INDEX idx_product_price ON product (price);
CREATE INDEX idx_product_brand_id ON product (brand_id);
CREATE INDEX idx_product_category_id ON product (category_id);
```

## 성능 비교 테스트

이 프로젝트에서는 다음과 같은 방식으로 인덱스의 영향을 측정합니다:

1. 인덱스가 없는 테이블(`Product`)과 인덱스가 있는 테이블(`ProductIndex`)을 생성합니다.
2. 두 테이블에 동일한 데이터를 삽입합니다.
3. 다양한 쿼리 패턴(단일 값 검색, 범위 검색, 조인, 복합 조건 등)에 대해 실행 시간을 측정합니다.
4. JPA, QueryDSL, MyBatis의 세 가지 ORM 기술별로 성능 차이를 비교합니다.

### 테스트 항목

1. **상품명 조회**: 특정 상품명으로 조회하는 성능 비교
2. **가격 범위 조회**: 특정 가격 범위 내의 상품을 조회하는 성능 비교
3. **브랜드 기준 조회**: 특정 브랜드에 속한 상품을 조회하는 성능 비교
4. **복합 검색**: 여러 조건(상품명, 가격 범위, 브랜드, 카테고리 등)을 조합한 검색 성능 비교

## 테스트 결과 및 분석

모든 테스트는 1,000개의 상품 데이터와 각 상품당 3개의 이미지 데이터를 사용하여 수행되었습니다.

### 1. 상품명 조회 성능 비교

| 방식 | 인덱스 없음 | 인덱스 있음 | 성능 향상률 |
|------|------------|------------|------------|
| JPA  | 약 XX ms   | 약 XX ms   | 약 XX%     |
| QueryDSL | 약 XX ms | 약 XX ms | 약 XX%     |
| MyBatis | 약 XX ms | 약 XX ms | 약 XX%      |

### 2. 가격 범위 조회 성능 비교

| 방식 | 인덱스 없음 | 인덱스 있음 | 성능 향상률 |
|------|------------|------------|------------|
| JPA  | 약 XX ms   | 약 XX ms   | 약 XX%     |
| QueryDSL | 약 XX ms | 약 XX ms | 약 XX%     |
| MyBatis | 약 XX ms | 약 XX ms | 약 XX%      |

### 3. 브랜드 기준 조회 성능 비교

| 방식 | 인덱스 없음 | 인덱스 있음 | 성능 향상률 |
|------|------------|------------|------------|
| JPA  | 약 XX ms   | 약 XX ms   | 약 XX%     |
| QueryDSL | 약 XX ms | 약 XX ms | 약 XX%     |
| MyBatis | 약 XX ms | 약 XX ms | 약 XX%      |

### 4. 복합 검색 성능 비교

| 방식 | 인덱스 없음 | 인덱스 있음 | 성능 향상률 |
|------|------------|------------|------------|
| JPA  | 약 XX ms   | 약 XX ms   | 약 XX%     |
| QueryDSL | 약 XX ms | 약 XX ms | 약 XX%     |
| MyBatis | 약 XX ms | 약 XX ms | 약 XX%      |

## 결론 및 권장사항

### 성능 개선을 위한 인덱스 활용 전략

1. **검색 빈도가 높은 컬럼에 인덱스 적용**: 자주 검색되는 컬럼에 우선적으로 인덱스를 적용하는 것이 효과적입니다.
2. **복합 인덱스 활용**: 여러 컬럼을 함께 검색하는 경우, 단일 인덱스보다 복합 인덱스가 더 효율적일 수 있습니다.
3. **인덱스 수 최적화**: 너무 많은 인덱스는 쓰기 성능을 저하시키므로, 실제 사용되는 쿼리 패턴을 분석하여 필요한 인덱스만 유지하는 것이 중요합니다.
4. **ORM별 특성 고려**: 각 ORM 기술마다 인덱스 활용 패턴이 다를 수 있으므로, 사용하는 ORM에 맞는 인덱스 전략을 수립해야 합니다.

### ORM 별 인덱스 활용 팁

1. **JPA/Hibernate**: 연관 관계 매핑이 있는 컬럼(외래 키)에 인덱스를 적용하면 조인 성능이 향상됩니다.
2. **QueryDSL**: 동적 쿼리가 많이 사용되는 환경에서는 자주 사용되는 조건 컬럼에 인덱스를 적용하는 것이 중요합니다.
3. **MyBatis**: SQL을 직접 작성하므로, 실제 쿼리의 WHERE 절과 ORDER BY 절에 사용되는 컬럼에 인덱스를 적용하는 것이 효과적입니다.

## 참고 자료

- [JPA @Index 애노테이션 문서](https://docs.jboss.org/hibernate/orm/5.4/javadocs/org/hibernate/annotations/Index.html)
- [MySQL 인덱스 최적화 가이드](https://dev.mysql.com/doc/refman/8.0/en/optimization-indexes.html)
- [PostgreSQL 인덱스 문서](https://www.postgresql.org/docs/current/indexes.html)
- [데이터베이스 성능 최적화를 위한 인덱스 디자인](https://use-the-index-luke.com/)