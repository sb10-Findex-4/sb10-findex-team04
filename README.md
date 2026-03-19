# {sb10-findex-team04}
팀 협업 문서 링크  
https://www.notion.so/Codeit-Spring-10-4-f6765902e5ef8343bfb6818f0144f915

## 팀원 구성
문정환  
박린  
이다솔  
전승주  
황민재  

---

## 프로젝트 소개
- 금융 지수 데이터를 한눈에제공하는 대시보드 서비스의 백엔드 시스템 구축
- 프로젝트 기간: 2026.03.11~2026.03.20

---

## 기술 스택
- Backend: Spring Boot, Spring Data JPA
- Database: PostgreSQL
- 공통 Tool: Git&Gitub, Discord

---

## 팀원별 구현 기능 상세
문정환  
박린  
이다솔  
전승주  
황민재  

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
