# Ecommerce MSA Backend

### 도커 실행 설정
`~/docker-compose.yaml` 

### 빌드 명령어
`docker-compose up --build` 

### 설계 
1. 모든 서비스는 API-GATEWAY를 통해서 호출된다.
2. 설정 정보는 한곳에서 관리되어야한다.
3. 모든 서비스 정보(서비스명등..)는 한곳에서 관리되어야 한다.
4. 마이크로 서비스는 호출 추적이 되어야 한다.
5. 마이크로 서비스는 호출 정보가 시각화 되어야 한다.
6. 마이크로 서비스끼리의 호출 방식은 Rest 방식 또는 Feign Client로 호출한다.
7. 마이크로 서비스에서 동기화 문제를 메세지 시스템을 사용하여 해결한다.


## 기타 
사용 언어 : java11
사용 기술 : Spring boot, Spring Cloud gateway, Spring Cloud discovery, Spring Cloud Config, Spring Cloud Sleuth, MariaDB, kafka, zipkin 