# {sb10-findex-team04}
팀 협업 문서 링크  
https://www.notion.so/Codeit-Spring-10-4-f6765902e5ef8343bfb6818f0144f915

## 팀원 구성
문정환 ([mjohn26](https://github.com/mjohn26))   
박린 ([boolynn17](https://github.com/boolynn17))  
이다솔 ([LeeDyol](https://github.com/LeeDyol))   
전승주 ([sungju3115](https://github.com/sungju3115))   
황민재 ([rorm0819](https://github.com/rorm0819))   

---

## 프로젝트 소개
- 금융 지수 데이터를 한 눈에 제공하는 대시보드 서비스의 백엔드 시스템 구축
- 프로젝트 기간: 2026.03.11~2026.03.20

---

## 기술 스택
- Backend: Spring Boot, Spring Data JPA, springdoc-openapi, MapStruct, QueryDSL  
- Database: PostgreSQL
- 배포 Tool: Railway
- 공통 Tool: Git&Github, Discord

---

## 팀원별 구현 기능 상세
### 문정환  
- 자동 연동 설정 관리
### 박린  
- 연동 작업 관리 + 대시 보드
### 이다솔  
- Opne API 연동 준비 + 연동 작업 관리
### 전승주  
- 지수 정보 관리 + 이슈 대응 + DB 설정
### 황민재  
- 지수 데이터 관리

---

## 파일 구조
```
.
├── .env
├── build.gradle
└── src
    └── main
        ├── java/com/sprint/mission/findex
        │   ├── autosyncconfig
        │   │   ├── controller
        │   │   ├── dto
        │   │   ├── entity
        │   │   ├── scheduler
        │   │   └── service
        │   ├── base
        │   ├── client
        │   │   ├── dto
        │   │   └── FindexOpenApiClient.java
        │   ├── config
        │   ├── exception
        │   │   └── response
        │   ├── indexdata
        │   │   ├── controller
        │   │   ├── dto
        │   │   ├── entity
        │   │   ├── mapper
        │   │   ├── repository
        │   │   └── service
        │   ├── indexinfo
        │   │   ├── controller
        │   │   ├── dto
        │   │   ├── entity
        │   │   ├── mapper
        │   │   ├── repository
        │   │   └── service
        │   ├── syncjob
        │   │   ├── controller
        │   │   ├── dto
        │   │   ├── entity
        │   │   ├── mapper
        │   │   ├── repository
        │   │   └── service
        │   └── FindexApplication.java
        └── resources
            ├── application-dev.yaml
            ├── application-prod.yaml
            ├── application.yaml
            └── static
                ├── assets
                ├── favico.ico
                └── index.html
```

---

## 구현 홈페이지
https://sb10-findex-team04-copy-production.up.railway.app/

---

## 프로젝트 회고록
- 전승주 : https://jsjcode.tistory.com/53 
