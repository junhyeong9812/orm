-- MySQL 초기화 스크립트

-- 한글 인코딩 확인
SHOW VARIABLES LIKE 'character_set%';
SHOW VARIABLES LIKE 'collation%';

-- 데이터베이스가 없으면 생성
CREATE DATABASE IF NOT EXISTS ormdb
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 사용자 권한 설정
GRANT ALL PRIVILEGES ON ormdb.* TO 'user'@'%';
FLUSH PRIVILEGES;

-- 데이터베이스 선택
USE ormdb;

-- 필요한 추가 테이블, 인덱스 등을 여기에 추가할 수 있습니다.