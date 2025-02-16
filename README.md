## README

### 설명
할 일 목록을 효율적으로 관리할 수 있도록 하는 todo 서비스 입니다.
- 회원가입/로그인/로그아웃
- 할 일 목록 조회/추가/수정/삭제
- 우선순위 필터링

### 실행 방법
```
# 1. mysql, nginx(react) 실행
$ docker compose up --build -d

# 2. Spring 백엔드 빌드
$ cd backend
$ ./gradlew clean build

# 3. Spring 실행
$ java -jar build/libs/mytodo-0.0.1-SNAPSHOT.jar --spring.profiles.active=release

# 4. 브라우저 http://localhost:5173 접속
```


### API 명세
https://qsunki.github.io/todo/
<br>또는 실행 후 브라우저에서 다음 주소로 접속<br>
http://localhost:8080/docs/index.html

### Backend 라이브러리
- **spring data jdbc**: 간단한 CRUD 작업을 위한 ORM. JPA보다 가벼우며 도메인 중심 설계에 적합
- **mybatis**: 복잡한 쿼리 작성이 필요한 경우를 위해 사용. SQL을 직접 작성하여 세밀한 쿼리 제어 가능
- **testcontainer**: 실제 데이터베이스를 사용한 통합 테스트 환경 구성. 테스트의 신뢰성 향상
- **rest docs(api docs)**: API 문서 자동화. 테스트 코드를 기반으로 항상 최신 상태의 문서 유지
- **spring security**: 인증/인가 처리


### Frontend 컴포넌트
- **Form**: 로그인, 회원가입 폼 구현
- **Input**: 텍스트 입력 필드 (할 일 제목, 내용 등)
- **Button**: 로그인, 회원가입, 할 일 추가/수정/삭제 등 액션 버튼
- **List**: 할 일 목록 표시
- **Card**: 할 일 아이템 컨테이너
- **Modal**: 할 일 수정/삭제 확인 팝업
- **message**: 작업 완료/실패 시 알림 메시지
- **Space**: 컴포넌트 간 간격 조절
