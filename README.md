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

## 향후 정리 방향

- ORM 별 CRUD 예제 코드 비교
- DB별 성능 측정 로그 수집 및 분석
- 실무에서의 선택 기준 및 Best Practice A/B 테스트

## 참고 사항

- 프로젝트는 Spring Boot 3.4.5와 Java 17 기반으로 개발되었습니다.
- 모든 데이터베이스는 도커 컨테이너로 제공되므로 로컬 설치가 필요 없습니다.
- Elasticsearch를 관리하기 위한 Kibana는 `http://localhost:5601`로 접속할 수 있습니다.
- MySQL과 PostgreSQL 데이터베이스는 한글 인코딩(UTF-8)이 기본 설정되어 있습니다.
- MongoDB와 Redis는 필요시 인증 정보를 사용할 수 있습니다.

