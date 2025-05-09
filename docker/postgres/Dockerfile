# PostgreSQL 15 공식 이미지 사용
FROM postgres:15.3

# 로케일 설정 (한글 지원)
RUN localedef -i ko_KR -c -f UTF-8 -A /usr/share/locale/locale.alias ko_KR.UTF-8

# 환경 변수 설정
ENV LANG ko_KR.utf8

# 초기화 스크립트 복사
COPY init.sql /docker-entrypoint-initdb.d/

# 포트 설정
EXPOSE 5432

# 헬스체크를 위한 명령
HEALTHCHECK --interval=10s --timeout=5s --retries=3 CMD pg_isready -U user -d ormdb