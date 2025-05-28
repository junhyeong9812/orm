# ORM Benchmark API Documentation

## 📋 개요

이 문서는 ORM 성능 비교 프로젝트의 REST API 엔드포인트를 상세히 설명합니다.
**MyBatis**와 **JPA** 두 가지 데이터 접근 기술로 동일한 기능을 구현하여 성능을 비교할 수 있도록 설계되었습니다.

## 🏗️ API 구조

```
📦 ORM Benchmark APIs
├── 🛍️ Product APIs
│   ├── MyBatis Product (/api/mybatis/product)
│   ├── JPA Product (/api/jpa/product)
│   ├── MyBatis ProductIndex (/api/mybatis/product-index)
│   └── JPA ProductIndex (/api/jpa/product-index)
├── 👤 User APIs
│   ├── MyBatis User (/api/mybatis/user)
│   └── JPA User (/api/jpa/user)
└── 📋 Order APIs
    ├── MyBatis Order (/api/mybatis/order)
    └── JPA Order (/api/jpa/order)
```

## 🔧 공통 사항

### Base URL
```
http://localhost:8080
```

### 공통 응답 헤더
```
Content-Type: application/json
```

### 공통 에러 응답
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "리소스를 찾을 수 없습니다",
  "path": "/api/mybatis/product/999"
}
```

### 페이징 공통 파라미터
- `page`: 페이지 번호 (0부터 시작, 기본값: 0)
- `size`: 페이지 크기 (기본값: 10)
- `sortBy`: 정렬 기준 필드 (기본값: "id")
- `sortDirection`: 정렬 방향 ("asc" 또는 "desc", 기본값: "asc")

---

## 🛍️ Product API

### 기본 정보
- **도메인**: 상품 관리
- **엔티티**: Product, ProductIndex (인덱스 최적화 테스트용)
- **주요 기능**: 상품 CRUD, 검색, 필터링, 페이징

### 🎯 MyBatis Product API (`/api/mybatis/product`)

#### 전체 상품 조회 (간단 버전)
```http
GET /api/mybatis/product
```

**응답 예시:**
```json
[
  {
    "id": 1,
    "name": "노트북 컴퓨터",
    "price": 1500000,
    "brandName": "삼성",
    "categoryName": "전자제품",
    "createdAt": "2024-01-01T09:00:00",
    "updatedAt": "2024-01-15T14:30:00"
  }
]
```

#### 전체 상품 조회 (상세 버전)
```http
GET /api/mybatis/product/detailed
```

**응답 예시:**
```json
[
  {
    "id": 1,
    "name": "노트북 컴퓨터",
    "price": 1500000,
    "brand": {
      "id": 1,
      "name": "삼성",
      "description": "삼성전자"
    },
    "category": {
      "id": 1,
      "name": "전자제품",
      "description": "전자제품 카테고리"
    },
    "images": [
      {
        "id": 1,
        "url": "https://example.com/image1.jpg",
        "altText": "상품 이미지"
      }
    ],
    "createdAt": "2024-01-01T09:00:00",
    "updatedAt": "2024-01-15T14:30:00"
  }
]
```

#### ID로 상품 조회
```http
GET /api/mybatis/product/{id}
```

**Path Parameters:**
- `id` (Long): 상품 ID

**응답 예시:**
```json
{
  "id": 1,
  "name": "노트북 컴퓨터",
  "price": 1500000,
  "brand": {
    "id": 1,
    "name": "삼성"
  },
  "category": {
    "id": 1,
    "name": "전자제품"
  }
}
```

#### 상품명으로 조회
```http
GET /api/mybatis/product/name/{name}
```

**Path Parameters:**
- `name` (String): 상품명

#### 가격 범위로 조회
```http
GET /api/mybatis/product/price?minPrice={minPrice}&maxPrice={maxPrice}
```

**Query Parameters:**
- `minPrice` (Integer): 최소 가격
- `maxPrice` (Integer): 최대 가격

#### 브랜드별 상품 조회
```http
GET /api/mybatis/product/brand/{brandId}
```

**Path Parameters:**
- `brandId` (Long): 브랜드 ID

#### 카테고리별 상품 조회
```http
GET /api/mybatis/product/category/{categoryId}
```

**Path Parameters:**
- `categoryId` (Long): 카테고리 ID

#### 페이징 조회
```http
GET /api/mybatis/product/paging?page={page}&size={size}&sortBy={sortBy}&sortDirection={sortDirection}
```

**Query Parameters:**
- `page` (Integer): 페이지 번호 (기본값: 0)
- `size` (Integer): 페이지 크기 (기본값: 10)
- `sortBy` (String): 정렬 기준 (기본값: "id")
- `sortDirection` (String): 정렬 방향 (기본값: "asc")

**응답 예시 (MyBatis 커스텀 PageDto):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "노트북 컴퓨터",
      "price": 1500000,
      "brandName": "삼성",
      "categoryName": "전자제품"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10,
  "hasNext": true,
  "hasPrevious": false
}
```

#### 정렬 조회
```http
GET /api/mybatis/product/sorting?sortBy={sortBy}&sortDirection={sortDirection}
```

#### 이미지 정보와 함께 조회
```http
GET /api/mybatis/product/{id}/with-images
```

#### 복합 검색
```http
GET /api/mybatis/product/search?keyword={keyword}&minPrice={minPrice}&maxPrice={maxPrice}&brandId={brandId}&categoryId={categoryId}&page={page}&size={size}&sortBy={sortBy}&sortDirection={sortDirection}
```

**Query Parameters:**
- `keyword` (String, Optional): 검색 키워드
- `minPrice` (Integer, Optional): 최소 가격
- `maxPrice` (Integer, Optional): 최대 가격
- `brandId` (Long, Optional): 브랜드 ID
- `categoryId` (Long, Optional): 카테고리 ID
- 페이징 파라미터들

#### 상품 생성
```http
POST /api/mybatis/product
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "새로운 상품",
  "price": 99000,
  "brandId": 1,
  "categoryId": 2
}
```

#### 상품 수정
```http
PUT /api/mybatis/product/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "수정된 상품명",
  "price": 120000
}
```

#### 상품 삭제
```http
DELETE /api/mybatis/product/{id}
```

### 🎯 JPA Product API (`/api/jpa/product`)

JPA Product API는 MyBatis와 동일한 엔드포인트를 제공하지만, 다음과 같은 차이점이 있습니다:

#### 페이징 응답 (Spring Data JPA Page 객체)
```json
{
  "content": [...],
  "pageable": {
    "sort": {
      "sorted": true,
      "direction": "asc",
      "property": "name"
    },
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true,
  "numberOfElements": 10,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "direction": "asc",
    "property": "name"
  },
  "empty": false
}
```

#### JPA 전용 연관 데이터 조회 엔드포인트

**브랜드 정보와 함께 조회:**
```http
GET /api/jpa/product/{id}/with-brand
```

**카테고리 정보와 함께 조회:**
```http
GET /api/jpa/product/{id}/with-category
```

**이미지 정보와 함께 조회:**
```http
GET /api/jpa/product/{id}/with-images
```

**모든 상세 정보와 함께 조회:**
```http
GET /api/jpa/product/{id}/with-all-details
```

### 🎯 ProductIndex API

ProductIndex는 인덱스 최적화 테스트를 위한 별도 엔티티로, Product와 동일한 API 구조를 가집니다.

**MyBatis ProductIndex API:**
```
/api/mybatis/product-index/*
```

**JPA ProductIndex API:**
```
/api/jpa/product-index/*
```

---

## 👤 User API

### 기본 정보
- **도메인**: 사용자 관리
- **엔티티**: User, UserProfile, Address, Image
- **주요 기능**: 사용자 CRUD, 프로필 관리, 주소 관리

### 🎯 MyBatis User API (`/api/mybatis/user`)

#### 전체 사용자 조회 (간단 버전)
```http
GET /api/mybatis/user
```

**응답 예시:**
```json
[
  {
    "id": 1,
    "username": "hong123",
    "email": "hong@example.com",
    "createdAt": "2024-01-01T09:00:00",
    "updatedAt": "2024-01-15T14:30:00"
  }
]
```

#### 전체 사용자 조회 (상세 버전)
```http
GET /api/mybatis/user/detailed
```

**응답 예시:**
```json
[
  {
    "id": 1,
    "username": "hong123",
    "email": "hong@example.com",
    "profile": {
      "id": 1,
      "nickname": "홍길동",
      "gender": "M",
      "profileImage": {
        "id": 1,
        "url": "https://example.com/profile.jpg",
        "altText": "프로필 이미지"
      }
    },
    "addresses": [
      {
        "id": 1,
        "zipcode": "12345",
        "detail": "서울시 강남구 테헤란로 123",
        "isDefault": true
      }
    ],
    "createdAt": "2024-01-01T09:00:00",
    "updatedAt": "2024-01-15T14:30:00"
  }
]
```

#### ID로 사용자 조회
```http
GET /api/mybatis/user/{id}
```

#### 이메일로 사용자 조회
```http
GET /api/mybatis/user/email/{email}
```

**Path Parameters:**
- `email` (String): 사용자 이메일

#### 사용자명으로 조회
```http
GET /api/mybatis/user/username/{username}
```

**Path Parameters:**
- `username` (String): 사용자명

#### 페이징 조회
```http
GET /api/mybatis/user/paging?page={page}&size={size}&sortBy={sortBy}&sortDirection={sortDirection}
```

#### 정렬 조회
```http
GET /api/mybatis/user/sorting?sortBy={sortBy}&sortDirection={sortDirection}
```

#### 프로필 정보와 함께 조회
```http
GET /api/mybatis/user/{id}/with-profile
```

#### 주소 정보와 함께 조회
```http
GET /api/mybatis/user/{id}/with-addresses
```

#### 사용자 검색
```http
GET /api/mybatis/user/search?keyword={keyword}&username={username}&email={email}&page={page}&size={size}&sortBy={sortBy}&sortDirection={sortDirection}
```

**Query Parameters:**
- `keyword` (String, Optional): 통합 검색 키워드 (이름, 이메일에서 검색)
- `username` (String, Optional): 사용자명으로 검색
- `email` (String, Optional): 이메일로 검색
- 페이징 파라미터들

#### 사용자 생성
```http
POST /api/mybatis/user
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "newuser123",
  "email": "newuser@example.com",
  "profile": {
    "nickname": "새로운 사용자",
    "gender": "F",
    "profileImage": {
      "url": "https://example.com/new-profile.jpg",
      "altText": "새 프로필 이미지"
    }
  },
  "addresses": [
    {
      "zipcode": "54321",
      "detail": "부산시 해운대구 센텀로 456",
      "isDefault": true
    }
  ]
}
```

#### 사용자 수정
```http
PUT /api/mybatis/user/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "updateduser123",
  "email": "updated@example.com"
}
```

#### 사용자 삭제
```http
DELETE /api/mybatis/user/{id}
```

### 🎯 JPA User API (`/api/jpa/user`)

JPA User API는 MyBatis와 동일한 엔드포인트를 제공하며, 추가로 다음 엔드포인트를 제공합니다:

#### 모든 상세 정보와 함께 조회 (JPA 전용)
```http
GET /api/jpa/user/{id}/with-all-details
```

**응답 예시:**
```json
{
  "id": 1,
  "username": "hong123",
  "email": "hong@example.com",
  "profile": {
    "id": 1,
    "nickname": "홍길동",
    "gender": "M",
    "profileImage": {
      "id": 1,
      "url": "https://example.com/profile.jpg",
      "altText": "프로필 이미지"
    }
  },
  "addresses": [
    {
      "id": 1,
      "zipcode": "12345",
      "detail": "서울시 강남구 테헤란로 123",
      "isDefault": true
    }
  ],
  "createdAt": "2024-01-01T09:00:00",
  "updatedAt": "2024-01-15T14:30:00"
}
```

---

## 📋 Order API

### 기본 정보
- **도메인**: 주문 관리
- **엔티티**: Order, OrderItem
- **주요 기능**: 주문 CRUD, 주문 상태 관리, 주문 통계

### 🎯 MyBatis Order API (`/api/mybatis/order`)

#### 전체 주문 조회 (간단 버전)
```http
GET /api/mybatis/order
```

**응답 예시:**
```json
[
  {
    "id": 1,
    "orderDate": "2024-01-15T10:30:00",
    "status": "CONFIRMED",
    "userId": 1,
    "username": "hong123",
    "totalAmount": 1500000,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

#### 전체 주문 조회 (상세 버전)
```http
GET /api/mybatis/order/detailed
```

**응답 예시:**
```json
[
  {
    "id": 1,
    "orderDate": "2024-01-15T10:30:00",
    "status": "CONFIRMED",
    "user": {
      "id": 1,
      "username": "hong123",
      "email": "hong@example.com"
    },
    "orderItems": [
      {
        "id": 1,
        "productId": 1,
        "productName": "노트북 컴퓨터",
        "productPrice": 1500000,
        "quantity": 1,
        "orderPrice": 1500000,
        "itemTotalPrice": 1500000
      }
    ],
    "totalAmount": 1500000,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

#### ID로 주문 조회
```http
GET /api/mybatis/order/{id}
```

#### 사용자별 주문 조회
```http
GET /api/mybatis/order/user/{userId}
```

**Path Parameters:**
- `userId` (Long): 사용자 ID

#### 주문 상태별 조회
```http
GET /api/mybatis/order/status/{status}
```

**Path Parameters:**
- `status` (OrderStatus): 주문 상태
    - `PENDING`: 대기중
    - `CONFIRMED`: 확인됨
    - `SHIPPED`: 배송중
    - `DELIVERED`: 배송완료
    - `CANCELLED`: 취소됨

#### 사용자 + 상태별 주문 조회
```http
GET /api/mybatis/order/user/{userId}/status/{status}
```

#### 날짜 범위별 주문 조회
```http
GET /api/mybatis/order/date-range?startDate={startDate}&endDate={endDate}
```

**Query Parameters:**
- `startDate` (LocalDateTime): 시작 날짜 (ISO 8601 형식: 2024-01-01T00:00:00)
- `endDate` (LocalDateTime): 종료 날짜 (ISO 8601 형식: 2024-12-31T23:59:59)

#### 페이징 조회
```http
GET /api/mybatis/order/paging?page={page}&size={size}&sortBy={sortBy}&sortDirection={sortDirection}
```

#### 정렬 조회
```http
GET /api/mybatis/order/sorting?sortBy={sortBy}&sortDirection={sortDirection}
```

#### 주문 상품 정보와 함께 조회
```http
GET /api/mybatis/order/{id}/with-order-items
```

#### 최근 주문 조회
```http
GET /api/mybatis/order/recent?limit={limit}
```

**Query Parameters:**
- `limit` (Integer): 조회할 주문 수 (기본값: 10)

#### 사용자별 총 주문 금액
```http
GET /api/mybatis/order/user/{userId}/total-amount
```

**응답 예시:**
```json
5500000
```

#### 주문 검색
```http
GET /api/mybatis/order/search?userId={userId}&status={status}&startDate={startDate}&endDate={endDate}&page={page}&size={size}&sortBy={sortBy}&sortDirection={sortDirection}
```

**Query Parameters:**
- `userId` (Long, Optional): 사용자 ID
- `status` (OrderStatus, Optional): 주문 상태
- `startDate` (LocalDateTime, Optional): 시작 날짜
- `endDate` (LocalDateTime, Optional): 종료 날짜
- 페이징 파라미터들

#### 주문 생성
```http
POST /api/mybatis/order
Content-Type: application/json
```

**Request Body:**
```json
{
  "userId": 1,
  "orderDate": "2024-01-15T10:30:00",
  "status": "PENDING",
  "orderItems": [
    {
      "productId": 1,
      "quantity": 2,
      "orderPrice": 50000
    },
    {
      "productId": 2,
      "quantity": 1,
      "orderPrice": 30000
    }
  ]
}
```

#### 주문 수정
```http
PUT /api/mybatis/order/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "status": "CONFIRMED",
  "orderDate": "2024-01-15T10:30:00"
}
```

#### 주문 상태 변경
```http
PATCH /api/mybatis/order/{id}/status?status={status}
```

**Query Parameters:**
- `status` (OrderStatus): 변경할 주문 상태

#### 주문 삭제
```http
DELETE /api/mybatis/order/{id}
```

### 🎯 JPA Order API (`/api/jpa/order`)

JPA Order API는 MyBatis와 동일한 엔드포인트를 제공하며, 추가로 다음 엔드포인트들을 제공합니다:

#### 사용자 정보와 함께 조회 (JPA 전용)
```http
GET /api/jpa/order/{id}/with-user
```

#### 주문 상품 정보와 함께 조회 (JPA 전용)
```http
GET /api/jpa/order/{id}/with-order-items
```

#### 모든 상세 정보와 함께 조회 (JPA 전용)
```http
GET /api/jpa/order/{id}/with-all-details
```

**응답 예시:**
```json
{
  "id": 1,
  "orderDate": "2024-01-15T10:30:00",
  "status": "CONFIRMED",
  "user": {
    "id": 1,
    "username": "hong123",
    "email": "hong@example.com"
  },
  "orderItems": [
    {
      "id": 1,
      "productId": 1,
      "productName": "노트북 컴퓨터",
      "productPrice": 1500000,
      "quantity": 1,
      "orderPrice": 1500000,
      "itemTotalPrice": 1500000
    }
  ],
  "totalAmount": 1500000,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:00:00"
}
```

#### 주문 상태 변경 (JPA - OrderResponseDto 반환)
```http
PATCH /api/jpa/order/{id}/status?status={status}
```

**응답:** MyBatis는 `204 No Content`를 반환하지만, JPA는 수정된 주문 정보를 반환합니다.

---

## 📊 성능 비교 가이드

### 1. 기본 CRUD 성능 비교

#### 조회 성능 테스트
```bash
# MyBatis 전체 조회
curl "http://localhost:8080/api/mybatis/product" | jq '.[0:5]'

# JPA 전체 조회  
curl "http://localhost:8080/api/jpa/product" | jq '.[0:5]'

# 로그에서 실행시간 확인
# [MyBatis] Product findAll - 실행시간: 25ms, 결과 수: 1000
# [JPA] Product findAll - 실행시간: 32ms, 결과 수: 1000
```

#### 단건 조회 성능 테스트
```bash
# MyBatis 단건 조회
curl "http://localhost:8080/api/mybatis/product/1"

# JPA 단건 조회
curl "http://localhost:8080/api/jpa/product/1"
```

### 2. 인덱스 효과 비교

```bash
# 일반 테이블에서 검색
curl "http://localhost:8080/api/mybatis/product/search?keyword=노트북"

# 인덱스가 적용된 테이블에서 검색
curl "http://localhost:8080/api/mybatis/product-index/search?keyword=노트북"

# 실행시간 비교 확인
```

### 3. 복잡한 조인 쿼리 성능 비교

```bash
# MyBatis 조인 쿼리
curl "http://localhost:8080/api/mybatis/product/1/with-images"

# JPA 페치 조인
curl "http://localhost:8080/api/jpa/product/1/with-all-details"
```

### 4. 페이징 성능 비교

```bash
# MyBatis 페이징 (커스텀 PageDto)
curl "http://localhost:8080/api/mybatis/product/paging?page=100&size=20" | jq '.totalElements'

# JPA 페이징 (Spring Data Page)
curl "http://localhost:8080/api/jpa/product/paging?page=100&size=20" | jq '.totalElements'
```

### 5. 검색 쿼리 성능 비교

```bash
# MyBatis 복합 검색
curl "http://localhost:8080/api/mybatis/product/search?keyword=컴퓨터&minPrice=100000&maxPrice=2000000&page=0&size=50"

# JPA 복합 검색
curl "http://localhost:8080/api/jpa/product/search?keyword=컴퓨터&minPrice=100000&maxPrice=2000000&page=0&size=50"
```

## 🔍 로그 분석

### MyBatis 로그 패턴
```
[MyBatis] Product findAll - 실행시간: 25ms, 결과 수: 1000
==>  Preparing: SELECT * FROM product WHERE price BETWEEN ? AND ?
==> Parameters: 100000(Integer), 500000(Integer)
<==      Total: 156
```

### JPA 로그 패턴
```
[JPA] Product findAll - 실행시간: 32ms, 결과 수: 1000
Hibernate: 
    select
        p1_0.id,
        p1_0.name,
        p1_0.price,
        p1_0.brand_id,
        p1_0.category_id 
    from
        product p1_0 
    where
        p1_0.price between ? and ?
```

## 📝 주의사항

1. **DateTime 형식**: ISO 8601 형식을 사용합니다 (`2024-01-15T10:30:00`)
2. **페이징 시작**: 페이지 번호는 0부터 시작합니다
3. **정렬 방향**: "asc" 또는 "desc"만 허용됩니다
4. **주문 상태**: OrderStatus enum 값을 정확히 사용해야 합니다
5. **성능 측정**: 각 API 호출 시 서버 로그에서 실행시간을 확인할 수 있습니다

## 🚀 성능 최적화 팁

1. **인덱스 활용**: ProductIndex API를 사용하여 인덱스 효과를 확인하세요
2. **페이징 사용**: 대용량 데이터 조회 시 반드시 페이징을 사용하세요
3. **필요한 데이터만 조회**: 간단한 조회가 필요한 경우 기본 엔드포인트를 사용하세요
4. **연관 데이터 조회**: N+1 문제를 피하기 위해 전용 조인 엔드포인트를 활용하세요
5. **검색 최적화**: 정확한 검색 조건을 사용하여 불필요한 데이터 조회를 피하세요