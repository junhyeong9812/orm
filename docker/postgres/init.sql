-- PostgreSQL 초기화 스크립트

-- 한글 인코딩 확인
SHOW server_encoding;
SHOW client_encoding;

-- 확장 모듈 설치 (필요한 경우)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 스키마 생성
CREATE SCHEMA IF NOT EXISTS app;

-- 권한 설정
GRANT ALL ON SCHEMA app TO user;
GRANT ALL ON ALL TABLES IN SCHEMA app TO user;
GRANT ALL ON ALL SEQUENCES IN SCHEMA app TO user;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA app TO user;

-- 필요한 추가 테이블, 인덱스 등을 여기에 추가할 수 있습니다.