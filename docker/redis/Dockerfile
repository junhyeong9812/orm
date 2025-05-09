# Redis 7.0 공식 이미지 사용
FROM redis:7.0

# 커스텀 설정 파일 복사
COPY redis.conf /usr/local/etc/redis/redis.conf

# 포트 설정
EXPOSE 6379

# Redis 서버 실행 (커스텀 설정 파일 사용)
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]

# 헬스체크를 위한 명령
HEALTHCHECK --interval=10s --timeout=5s --retries=3 CMD redis-cli ping || exit 1