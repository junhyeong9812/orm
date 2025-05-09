# Elasticsearch 7.17.10 공식 이미지 사용
FROM docker.elastic.co/elasticsearch/elasticsearch:7.17.10

# 한국어 분석기 플러그인 설치 (nori)
RUN bin/elasticsearch-plugin install analysis-nori

# elasticsearch.yml 설정 파일 복사
COPY elasticsearch.yml /usr/share/elasticsearch/config/elasticsearch.yml

# 포트 설정
EXPOSE 9200 9300

# 헬스체크를 위한 명령
HEALTHCHECK --interval=30s --timeout=30s --retries=3 \
  CMD curl -f http://localhost:9200/_cluster/health || exit 1