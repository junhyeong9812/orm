# ORM & DBMS 비교 분석 프로젝트

## 프로젝트 개요

이 프로젝트는 Java 백엔드 개발에서 사용되는 ORM 기술인 **MyBatis**, **JPA**, 그리고 **QueryDSL**의 차이를 비교 분석하고, 다양한 데이터베이스 시스템과 함께 사용하는 방법을 실습하는 것을 목표로 합니다.

또한, 대표적인 관계형 데이터베이스(RDBMS)인 **PostgreSQL**과 **MySQL**, 그리고 NoSQL 데이터베이스인 **Redis**, **MongoDB**, **Elasticsearch**를 **Docker 환경에서 직접 실행**하며, 각 데이터베이스의 구조적 특성과 사용 목적에 대해 정리합니다.

## 프로젝트 목표

1. JPA와 MyBatis의 차이점 분석
2. 각 ORM 프레임워크의 특징 및 장단점 이해
3. 다양한 데이터베이스 시스템에서의 성능 비교
4. QueryDSL과 MyBatis SQL Builder 패턴 비교
5. 실무에서의 선택 기준 및 Best Practice 도출

## 프로젝트 구조

```
orm-benchmark/
├── src/
│   ├── main/
│   │   ├── java/com/benchmark/orm/
│   │   │   ├── domain/
│   │   │   │   ├── order/
│   │   │   │   ├── product/
│   │   │   │   └── user/
│   │   │   ├── global/
│   │   │   └── OrmApplication.java
│   │   └── resources/
│   │       ├── mapper/
│   │       └── application.yml
│   └── test/
└── docker/
    ├── docker-compose.yml
    ├── mysql/
    ├── postgres/
    ├── mongodb/
    ├── redis/
    └── elasticsearch/
```

## ORM 기술 비교

### MyBatis
- **특징**: SQL Mapper 기반으로, SQL을 직접 작성함으로써 세밀한 쿼리 제어가 가능
- **장점**: 학습 곡선이 낮고, SQL에 대한 직접적인 제어 가능
- **단점**: 유지보수 시 SQL이 복잡해질 수 있음, 데이터베이스와 높은 결합도
- **사용 사례**: 복잡한 레거시 시스템, 세밀한 SQL 튜닝이 필요한 경우

### JPA
- **특징**: Java 객체와 DB 테이블 매핑을 자동화하는 ORM, Hibernate 구현체와 함께 사용
- **장점**: 선언형 코드로 생산성이 높음, 데이터베이스와 낮은 결합도로 DB 변경 용이
- **단점**: 학습 곡선이 가파름, 복잡한 쿼리 및 성능 튜닝이 어려울 수 있음
- **사용 사례**: 도메인 중심 설계, 객체 지향적 애플리케이션

### QueryDSL
- **특징**: JPA의 정적 타입 쿼리 빌더, 동적 쿼리를 타입 안정성 있게 작성 가능
- **장점**: 컴파일 시점 오류 검출, 유지보수와 리팩토링에 강함
- **단점**: 추가 설정 및 빌드 과정 필요
- **사용 사례**: 복잡한 동적 쿼리가 필요한 엔터프라이즈 애플리케이션

## 지원하는 데이터베이스

### RDBMS (관계형 데이터베이스)

관계형 데이터베이스는 **테이블 간 관계(Relation)** 를 기반으로 데이터를 관리하며, **SQL(Structured Query Language)** 을 사용합니다. 데이터 무결성, 트랜잭션 보장, 정형화된 스키마 기반 설계가 특징입니다.

#### H2 (인메모리)
- 개발 및 테스트 환경에 적합한 경량 데이터베이스
- 메모리 모드와 파일 모드 모두 지원
- 웹 콘솔 제공

#### PostgreSQL
- 오픈소스이며 기능이 매우 강력함 (Window Function, JSONB, GIS 기능 등)
- ACID 트랜잭션과 높은 확장성 보유
- 대규모 시스템이나 정밀한 데이터 모델링에 적합

#### MySQL
- 가볍고 빠른 RDBMS로, 스타트업 및 소규모 프로젝트에서 광범위하게 사용
- MariaDB로 포크되어 사용되기도 하며, LAMP 스택의 기본 DB
- 복제 및 샤딩 등으로 확장성도 갖춤

### NoSQL (Not Only SQL)

NoSQL은 **비정형/반정형 데이터**, **수평 확장**, **높은 쓰기 성능**이 필요한 경우에 사용됩니다. 데이터 모델링 유연성과 분산처리, 실시간 처리에 적합합니다.

#### MongoDB (Document)
- JSON 유사 문서(Document) 기반 데이터 저장
- 스키마 유연성이 높아 초기 설계 부담이 적고, 중첩 구조 표현이 쉬움
- 웹, 모바일, 빅데이터 환경에서 많이 사용됨

#### Elasticsearch (Search Engine / Document)
- 전문 검색 엔진. 문서 기반으로 색인 후, 고속 검색 제공
- 토큰화, n-gram, 한국어 분석기(노리) 등의 고급 검색 기능을 지원
- 로그 분석, 추천 시스템, 검색 최적화 등에 활용됨

#### Redis (Key-Value)
- 메모리 기반 Key-Value 저장소로, 매우 빠른 응답 속도 제공
- 주로 **캐시, 세션 저장소, 실시간 처리** 용도로 사용됨
- 자료구조형 저장(Key-List, Set 등)과 TTL 설정 지원

## 도커 컨테이너 실행 방법

프로젝트 루트 디렉토리에서 다음 명령어를 실행합니다:

```bash
cd docker
docker-compose up -d
```

모든 컨테이너를 중지하고 제거하려면:

```bash
docker-compose down -v
```

## 애플리케이션 실행

특정 데이터베이스로 실행하려면 아래의 프로필 중 하나를 선택합니다:

```bash
# H2 인메모리 데이터베이스 (기본값)
./gradlew bootRun

# MySQL 데이터베이스
./gradlew bootRun --args='--spring.profiles.active=mysql'

# PostgreSQL 데이터베이스
./gradlew bootRun --args='--spring.profiles.active=postgres'

# MongoDB
./gradlew bootRun --args='--spring.profiles.active=mongodb'

# Elasticsearch
./gradlew bootRun --args='--spring.profiles.active=elasticsearch'

# Redis
./gradlew bootRun --args='--spring.profiles.active=redis'
```

## 주요 구현 기능

1. JPA 기반 데이터 접근
   - EntityManager 직접 사용
   - Spring Data JPA Repository 사용
   - QueryDSL 사용

2. MyBatis 기반 데이터 접근
   - XML 매핑 파일 사용
   - 애노테이션 기반 매핑 사용
   - Dynamic SQL 사용

3. 다양한 조인 및 조건문 구현
   - JPA: JPQL, Criteria API, QueryDSL
   - MyBatis: XML, 동적 SQL

4. 성능 벤치마크
   - 각 데이터베이스별 성능 측정
   - 대용량 데이터 처리 성능 비교
   - 쿼리 복잡도에 따른 성능 변화 분석

## 웹 인터페이스

각 데이터베이스 관리 도구:

- **H2 콘솔**: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:ormdb)
- **Kibana (Elasticsearch)**: http://localhost:5601
- **MongoDB**: MongoDB Compass 클라이언트 사용 (URL: mongodb://localhost:27017)
- **Redis**: Redis Commander 또는 Redis Desktop Manager 사용

## JPA vs MyBatis 비교 연구

### 특징 비교

| 특성 | JPA | MyBatis |
|------|-----|---------|
| 추상화 수준 | 높음 (객체 중심) | 중간 (SQL 중심) |
| 학습 곡선 | 가파름 | 완만함 |
| 쿼리 제어 | 간접적 | 직접적 |
| 성능 튜닝 | 복잡함 | 상대적으로 쉬움 |
| 코드량 | 적음 | 많음 (XML 포함) |
| 동적 쿼리 | QueryDSL 사용 | 동적 SQL 태그 사용 |

### 결합도 비교

- **JPA**: 데이터베이스와 낮은 결합도, 데이터베이스 변경 용이
- **MyBatis**: 데이터베이스와 높은 결합도, SQL에 의존적

### 주요 이슈

- **N+1 문제**: JPA에서는 주의가 필요하며, MyBatis에서는 직접 조인 쿼리 작성
- **복잡한 쿼리**: JPA에서는 네이티브 쿼리 또는 QueryDSL, MyBatis에서는 직접 SQL 작성
- **대량 데이터 처리**: 배치 처리를 위한 각 프레임워크별 최적화 방법 비교

## MyBatis와 JPA/QueryDSL 간 SQL 생성 패턴 상세 분석

테스트 코드와 로그를 분석한 결과, MyBatis와 JPA/QueryDSL 간에 실제 생성되는 SQL에 있어 명확한 차이점을 확인했습니다. 아래는 각 기능별로 생성되는 SQL 패턴을 상세히 비교한 내용입니다.

### 1. 기본 CRUD 작업 비교

#### 1.1 데이터 삽입(INSERT)

**MyBatis**
```sql
==>  Preparing: INSERT INTO users (username, email, created_at, updated_at) 
     VALUES (?, ?, COALESCE(?, CURRENT_TIMESTAMP), COALESCE(?, CURRENT_TIMESTAMP))
==> Parameters: 매퍼테스트(String), mapper@example.com(String), null, null
```

**JPA/Hibernate**
```sql
/* insert for
   com.benchmark.orm.domain.user.entity.User */insert 
into
   users (created_at, created_by, email, modified_by, updated_at, username, id) 
values
   (?, ?, ?, ?, ?, ?, default)
```

**차이점**:
- MyBatis는 XML에 정확히 정의된 쿼리를 실행하며, `COALESCE` 함수로 타임스탬프 기본값을 직접 설정
- JPA는 엔티티 객체를 기반으로 Hibernate가 쿼리를 생성하며, 파라미터 바인딩 순서와 컬럼 구성이 다름
- JPA는 주석으로 원본 엔티티 정보를 포함

#### 1.2 데이터 조회(SELECT)

**MyBatis (ID로 조회)**
```sql
==>  Preparing: SELECT * FROM users WHERE id = ?
==> Parameters: 6(Long)
```

**JPA/Hibernate (ID로 조회)**
```sql
select
   u1_0.id,
   u1_0.created_at,
   u1_0.created_by,
   u1_0.email,
   u1_0.modified_by,
   u1_0.updated_at,
   u1_0.username 
from
   users u1_0 
where
   u1_0.id=?
```

**차이점**:
- MyBatis는 `SELECT *`를 사용하여 모든 컬럼을 가져옴
- JPA는 엔티티에 매핑된 컬럼을 명시적으로 선택
- JPA는 테이블과 컬럼에 별칭(u1_0)을 자동 생성

#### 1.3 데이터 수정(UPDATE)

**MyBatis**
```sql
==>  Preparing: UPDATE users SET username = ?, email = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?
==> Parameters: 수정후(String), after@example.com(String), 9(Long)
```

**JPA/Hibernate**
```sql
/* update
   com.benchmark.orm.domain.user.entity.User */ update
   users 
set
   created_at=?,
   created_by=?,
   email=?,
   modified_by=?,
   updated_at=?,
   username=? 
where
   id=?
```

**차이점**:
- MyBatis는 명시적으로 변경할 컬럼만 업데이트
- JPA는 더티 체킹을 통해 모든 컬럼을 업데이트하는 경향이 있음
- MyBatis는 `updated_at`을 자동으로 `CURRENT_TIMESTAMP`로 설정

#### 1.4 데이터 삭제(DELETE)

**MyBatis**
```sql
==>  Preparing: DELETE FROM users WHERE id = ?
==> Parameters: 6(Long)
```

**JPA/Hibernate**
```sql
/* delete from
   com.benchmark.orm.domain.user.entity.User */ delete 
from
   users 
where
   id=?
```

**차이점**:
- 기본 DELETE 쿼리는 두 기술 모두 유사
- JPA는 원본 엔티티 정보를 주석으로 포함

### 2. 복잡한 조회 작업 비교

#### 2.1 연관 관계 조회(JOIN)

**MyBatis (사용자와 프로필 함께 조회)**
```sql
==>  Preparing: SELECT u.id, u.username, u.email, u.created_at, u.updated_at,
        p.id as profile_id, p.nickname, p.gender,
        i.id as image_id, i.url as image_url, i.alt_text
        FROM users u
        LEFT JOIN user_profile p ON u.id = p.user_id
        LEFT JOIN image i ON p.image_id = i.id
        WHERE u.id = ?
```

**JPA/QueryDSL (사용자와 프로필 함께 조회)**
```sql
/* select
   user 
from
   User user   
left join
   fetch
   user.profile as userProfile 
where
   user.id = ?1 */ 
select
   u1_0.id,
   u1_0.created_at,
   u1_0.created_by,
   u1_0.email,
   u1_0.modified_by,
   p1_0.id,
   p1_0.created_at,
   p1_0.gender,
   p1_0.nickname,
   p1_0.image_id,
   p1_0.updated_at,
   u1_0.updated_at,
   u1_0.username 
from
   users u1_0 
left join
   user_profile p1_0 
      on u1_0.id=p1_0.user_id 
where
   u1_0.id=?
```

**차이점**:
- MyBatis는 직접 테이블 및 컬럼 별칭을 설정
- JPA는 `left join fetch` 구문을 사용하여 N+1 문제 방지
- JPA는 엔티티 객체 관점에서 쿼리를 작성하고 Hibernate가 SQL로 변환
- MyBatis는 이미지까지 한 번에 조회하는 3중 조인이 명시적

#### 2.2 다중 연관 관계 조회

**MyBatis (사용자와 주소 함께 조회)**
```sql
==>  Preparing: SELECT u.id, u.username, u.email, u.created_at, u.updated_at,
        a.id as address_id, a.zipcode, a.detail, a.is_default
        FROM users u
        LEFT JOIN address a ON u.id = a.user_id
        WHERE u.id = ?
```

**QueryDSL (프로필과 주소 모두 조회)**
```sql
/* select
   distinct user 
from
   User user   
left join
   fetch
   user.profile as userProfile   
left join
   fetch
   user.addresses as address 
where
   user.id = ?1 */ 
select
   distinct u1_0.id,
   a1_0.user_id,
   a1_0.id,
   a1_0.created_at,
   /* ... 중략 ... */
   u1_0.updated_at,
   u1_0.username 
from
   users u1_0 
left join
   user_profile p1_0 
      on u1_0.id=p1_0.user_id 
left join
   address a1_0 
      on u1_0.id=a1_0.user_id 
where
   u1_0.id=?
```

**차이점**:
- QueryDSL은 `distinct` 키워드를 사용하여 중복 행 제거 (컬렉션 조인 시 발생)
- MyBatis는 필요한 컬럼만 명시적으로 선택
- JPA/QueryDSL은 두 연관 관계를 동시에 페치 조인으로 가져옴

#### 2.3 동적 조건 검색

**MyBatis**
```sql
==>  Preparing: SELECT * FROM users
        WHERE username LIKE CONCAT('%', ?, '%')
        ORDER BY id asc LIMIT ? OFFSET ?
==> Parameters: 검색테스트(String), 10(Integer), 0(Integer)
```

**QueryDSL**
```sql
/* select
   user 
from
   User user 
where
   lower(user.username) like ?1 escape '!' 
order by
   user.id asc */ 
select
   u1_0.id,
   /* ... 중략 ... */
   u1_0.username 
from
   users u1_0 
where
   lower(u1_0.username) like ? escape '!' 
order by
   u1_0.id 
offset
   ? rows 
fetch
   first ? rows only
```

**차이점**:
- MyBatis는 `CONCAT` 함수로 와일드카드를 처리
- QueryDSL은 `lower()` 함수로 대소문자 구분 없는 검색 지원
- QueryDSL은 `escape '!'` 구문으로 특수문자 처리
- 페이징 구문: MyBatis는 `LIMIT ? OFFSET ?`, JPA는 `offset ? rows fetch first ? rows only`

#### 2.4 통계 쿼리

**MyBatis (검색 조건에 따른 사용자 수 조회)**
```sql
==>  Preparing: SELECT COUNT(*) FROM users
        WHERE email LIKE CONCAT('%', ?, '%')
==> Parameters: search(String)
```

**QueryDSL**
```sql
/* select
   count(user) 
from
   User user 
where
   lower(user.email) like ?1 escape '!' */ 
select
   count(u1_0.id) 
from
   users u1_0 
where
   lower(u1_0.email) like ? escape '!'
```

**차이점**:
- MyBatis는 `COUNT(*)`를 사용
- QueryDSL은 `count(u1_0.id)`를 사용하여 특정 컬럼 카운팅
- QueryDSL은 대소문자 구분 없는 검색을 위해 `lower()` 함수 적용

### 3. 정렬 및 페이징

**MyBatis**
```sql
==>  Preparing: SELECT * FROM users ORDER BY username asc
==> Parameters: 
```

**QueryDSL**
```sql
/* select
   user 
from
   User user 
order by
   user.username asc */ 
select
   u1_0.id,
   /* ... 중략 ... */
   u1_0.username 
from
   users u1_0 
order by
   u1_0.username
```

**차이점**:
- 기본 정렬 기능은 유사하게 구현
- QueryDSL은 원본 JPQL 쿼리를 주석으로 포함
- JPA는 엔티티 필드 기준으로 정렬하고 Hibernate가 컬럼명으로 변환
- MyBatis는 컬럼명을 직접 지정

### 4. 로깅 및 파라미터 바인딩 형태

**MyBatis**
```
==>  Preparing: [SQL 쿼리]
==> Parameters: value1(Type1), value2(Type2), ...
<==    Updates: 1
```

**JPA/Hibernate**
```
/* JPQL 또는 객체 관점 힌트 */
[SQL 쿼리]
Hibernate: [반복 출력되는 SQL]
```

**차이점**:
- MyBatis는 SQL 준비, 파라미터 바인딩, 영향받은 행 수를 명확히 구분하여 로깅
- JPA는 JPQL 주석과 실제 SQL을 함께 로깅
- MyBatis는 파라미터 타입을 포함하여 로깅하므로 디버깅이 용이
- Hibernate는 동일 SQL이 여러 번 로깅되는 경향이 있음

## 프로젝트 진행 중 얻은 인사이트

### 동적 쿼리 처리 방식 차이

이 프로젝트를 진행하면서 기존에 가지고 있던 관점이 크게 바뀌었습니다. 처음에는 JPA/QueryDSL에서는 자바 코드 기반 빌더 패턴으로 동적 쿼리를 처리하고, MyBatis에서는 서비스 레이어에서 빌더 패턴으로 SQL을 조립해야 한다고 생각했습니다. 그러나 실제 구현과 테스트를 진행하면서 MyBatis의 XML 기반 동적 SQL 기능이 매우 강력하고 효과적이라는 것을 발견했습니다.

#### 발견한 점:

1. **MyBatis 동적 SQL**
   - `<if>`, `<choose>`, `<where>`, `<sql>`, `<include>` 등의 태그를 사용하여 XML 내에서도 복잡한 조건부 쿼리를 구성할 수 있음
   - 서비스 레이어에서 빌더 패턴을 사용하지 않고도 매퍼 파일 내에서 동적 SQL 처리가 가능
   - 코드 예시:
     ```xml
     <sql id="searchCondition">
         <where>
             <if test="keyword != null">
                 name LIKE CONCAT('%', #{keyword}, '%')
             </if>
             <if test="minPrice != null">
                 AND price >= #{minPrice}
             </if>
         </where>
     </sql>
     
     <select id="searchProducts" resultMap="productResultMap">
         SELECT * FROM product
         <include refid="searchCondition"/>
         ORDER BY ${sortColumn} ${sortDirection}
         LIMIT #{limit} OFFSET #{offset}
     </select>
     ```

2. **QueryDSL vs MyBatis XML**
   - QueryDSL: 자바 코드 기반으로 타입 안전한 쿼리 구성, 컴파일 타임 오류 검출
   - MyBatis XML: XML 기반 동적 쿼리 구성, 런타임 오류 검출
   - 두 방식 모두 복잡한 조건부 쿼리를 효과적으로 처리할 수 있음

### SQL 쿼리 일관성과 생성 패턴

JPA와 MyBatis는 SQL 쿼리 생성 방식에서 근본적인 차이가 있어, 각각 다른 장단점을 가집니다.

#### JPA/Hibernate:
- 자바 코드로 쿼리를 추상화하여 표현하고, Hibernate가 실제 SQL로 변환
- 버전 변경이나 구현체 변경 시 생성되는 SQL이 달라질 수 있음
- 최적화가 보장되지 않을 수 있으며, 실행될 정확한 SQL을 예측하기 어려울 수 있음
- 객체 지향적 설계에 더 적합하고 개발 생산성이 높음

#### MyBatis:
- SQL을 명시적으로 XML에 작성하므로 실행될 쿼리가 명확함
- 버전이 변경되어도 동일한 SQL이 실행되어 쿼리의 일관성이 보장됨
- 성능 최적화 및 튜닝이 직접적이고 명확함
- 데이터베이스에 더 의존적이지만, SQL 제어가 정밀함

이러한 차이로 인해 기존 MyBatis 사용자가 JPA로 전환할 때 여러 의구심을 가질 수 있습니다. 특히 복잡한 쿼리나 성능에 민감한 애플리케이션에서는 투명하고 예측 가능한 SQL 생성이 중요하기 때문입니다.

### 실무 적용 관점

실제 프로젝트에서는 두 기술의 장단점을 고려하여 적절히 선택하거나 혼합 사용하는 것이 바람직합니다:

- **JPA/QueryDSL**: 도메인 중심 설계, 객체 관계가 복잡한 비즈니스 로직 구현, 단순 CRUD 작업
- **MyBatis**: 복잡한 조회 쿼리, 레거시 시스템 연동, 성능 최적화가 중요한 부분

두 기술을 함께 사용하는 경우, JPA는 엔티티 관리와 단순 CRUD에, MyBatis는 복잡한 조회 쿼리에 활용하는 방식으로 각 기술의 장점을 최대화할 수 있습니다.

## 향후 정리 방향

- ORM 별 CRUD 예제 코드 비교
- DB별 성능 측정 로그 수집 및 분석
- 실무에서의 선택 기준 및 Best Practice A/B 테스트
- 각 데이터베이스별 특성 분석은 별도 프로젝트로 분리 예정 (DB별 방언 차이 등)

## 참고 사항

- 프로젝트는 Spring Boot 3.4.5와 Java 17 기반으로 개발되었습니다.
- 모든 데이터베이스는 도커 컨테이너로 제공되므로 로컬 설치가 필요 없습니다.
- Elasticsearch를 관리하기 위한 Kibana는 `http://localhost:5601`로 접속할 수 있습니다.
- MySQL과 PostgreSQL 데이터베이스는 한글 인코딩(UTF-8)이 기본 설정되어 있습니다.
- MongoDB와 Redis는 필요시 인증 정보를 사용할 수 있습니다.