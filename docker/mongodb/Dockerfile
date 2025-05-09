# MongoDB 6.0 공식 이미지 사용
FROM mongo:6.0

# 초기화 스크립트 복사
COPY init-mongo.js /docker-entrypoint-initdb.d/

# 포트 설정
EXPOSE 27017

# 헬스체크를 위한 명령
HEALTHCHECK --interval=10s --timeout=10s --retries=5 \
  CMD mongosh --eval 'db.adminCommand("ping")' || exit 1