// MongoDB 초기화 스크립트
db = db.getSiblingDB('ormdb');

// 관리자 사용자 생성
db.createUser({
  user: 'admin',
  pwd: 'password',
  roles: [
    { role: 'dbOwner', db: 'ormdb' },
    { role: 'readWrite', db: 'ormdb' }
  ]
});

// 일반 사용자 생성
db.createUser({
  user: 'user',
  pwd: 'password',
  roles: [
    { role: 'readWrite', db: 'ormdb' }
  ]
});

// 컬렉션 생성
db.createCollection('users');
db.createCollection('orders');
db.createCollection('products');

// 컬렉션에 인덱스 추가
db.users.createIndex({ "email": 1 }, { unique: true });
db.orders.createIndex({ "orderDate": 1 });
db.products.createIndex({ "name": 1 });

// 예제 데이터 추가 (필요한 경우)
db.products.insertMany([
  { name: "Example Product 1", price: 10000, description: "테스트 상품 1" },
  { name: "Example Product 2", price: 20000, description: "테스트 상품 2" }
]);

print('MongoDB 초기화 완료');