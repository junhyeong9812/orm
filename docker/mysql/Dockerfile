# MySQL 8.0 공식 이미지 사용
FROM mysql:8.0

# 한글 설정을 위한 my.cnf 설정 파일 복사
COPY my.cnf /etc/mysql/conf.d/

# 초기화 데이터베이스 스크립트 복사 (필요한 경우)
COPY init.sql /docker-entrypoint-initdb.d/

# 포트 설정
EXPOSE 3306

# 헬스체크를 위한 명령
HEALTHCHECK --interval=10s --timeout=5s --retries=3 CMD mysqladmin ping -h localhost -u root -p${MYSQL_ROOT_PASSWORD}