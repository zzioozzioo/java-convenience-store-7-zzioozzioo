# java-convenience-store-precourse

# 🍱 편의점 🍱

***

## ⭐️ 핵심 기능

### ✅ 입력 기능

- [x] 상품 목록, 행사 목록 파일 읽어들이기
- [x] 구매할 상품과 수량 입력받기
- [x] 프로모션 적용 대상 증정품 추가 여부 입력받기
- [x] 프로모션 적용 대상이지만 재고 부족 시 정가 결제 여부 입력받기
- [x] 멤버십 할인 적용 여부 입력받기
- [x] 추가 구매 여부 입력받기

### ✅ 재고 관리 기능

- [x] 고객이 상품 구매 시 결제된 수량만큼 재고 차감
- [x] 최신 재고 상태 유지
- [x] 결제 가능 여부 확인

### ✅ 할인 기능

- 프로모션 할인(기본/MD추천상품/반짝할인)
    - [x] 오늘 날짜가 프로모션 기간 내에 포함된 경우에 적용
    - [x] N개 구매 시 1개 무료 증정 형태로 진행
    - [x] 동일 상품에 하나의 프로모션만 적용
    - [x] 프로모션 재고 내에서만 혜택 적용
    - [x] 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감
    - [x] 프로모션 재고가 부족할 경우에는 일반 재고를 사용

- 멤버십 할인
    - [x] Y/N로 적용 여부 확인
    - [x] 프로모션 미적용 금액의 30%를 할인
    - [x] 프로모션 적용 후 남은 금액에 대해 적용
    - [x] 최대 한도는 8,000원

### ✅ 계산 기능

- [x] 총 구매액 계산
- [x] 행사(프로모션) 할인 금액 계산
- [x] 멤버십 할인 금액 계산
- [x] 최종 결제 금액 계산

### ✅ 출력 기능

- [x] 환영 인사 출력
- [x] 상품 내역(상품명, 가격, 프로모션 이름, 재고) 출력
- 안내 메시지 출력
    - [x] 프로모션 적용 대상 증정품 혜택 안내 메시지 출력
    - [x] 프로모션 적용 대상이지만 재고 부족 시 정가 결제 여부 확인 안내 메시지 출력
    - [x] 멤버십 할인 적용 여부 확인 안내 메시지 출력
    - [x] 추가 구매 여부 확인 안내 메시지 출력
- 영수증 출력
    - [x] 구매 상품 내역 출력
    - [x] 증정 상품 내역 출력
    - [x] 금액 정보(총 구매액, 행사 할인, 멤버십 할인, 내실 돈) 출력

### ✅ 종료 기능

- [x] 영수증 출력 후 추가 구매 혹은 종료 선택

### ✅ 예외 처리 기능

1) 구매할 상품과 수량 입력 시
    - [x] 값을 입력하지 않은 경우
    - 개별 상품을 구분하는 구분자를 잘못 입력한 경우
        - [x] 구분자를 불필요하게 많이 입력한 경우
        - [x] 잘못된 구분자를 입력한 경우
    - 잘못된 상품명을 입력한 경우
        - [x] 상품명을 입력하지 않은 경우
        - [x] 존재하지 않는 상품명을 입력한 경우
        - [x] 상품명 앞뒤에 공백이 있는 경우(?)
    - 잘못된 수량을 입력한 경우
        - [x] 수량을 입력하지 않은 경우
        - [x] 0을 입력한 경우
        - 재고보다 더 많은 수량을 입력한 경우
            - [x] 재고가 없는 경우는 품절 안내
            - [x] 재고가 부족한 경우는 남은 재고 구매 여부 확인
    - [x] 상품명과 수량을 모두 입력하지 않은 경우
    - [x] 상품명과 수량을 구분하는 구분자를 잘못 입력한 경우


2) 프로모션 적용 대상 증정품 추가 여부 입력 시
    - [x] 값을 입력하지 않은 경우
    - [x] Y/N에 해당하지 않는 경우


3) 프로모션 적용 대상이지만 재고 부족 시 정가 결제 여부 입력 시
    - [x] 값을 입력하지 않은 경우
    - [x] Y/N에 해당하지 않는 경우


4) 멤버십 할인 적용 여부 입력 시
    - [x] 값을 입력하지 않은 경우
    - [x] Y/N에 해당하지 않는 경우


5) 추가 구매 여부 입력 시
    - [x] 값을 입력하지 않은 경우
    - [x] Y/N에 해당하지 않는 경우


6) 잘못된 값 입력 시 "[ERROR]"로 시작하는 에러 메시지 출력 후 그 부분부터 다시 입력받기

***

## 📦 역할별 패키지 분리

***

## ✅ 프로그래밍 요구 사항

### 제출 전 확인 리스트

- [x] JDK-21 사용
- [x] 프로그램 실행의 시작점은 `Application`의 `main()`
- [x] `build.gradle` 변경 불가, 제공된 라이브러리만 사용
- [x] [Java Style Guide](https://github.com/woowacourse/woowacourse-docs/tree/main/styleguide/java)를 준수하며 프로그래밍
- [x] 프로그램 종료 시`System.exit()`를 호출 X
- [x] 프로그램 구현 완료 시 `ApplicationTest`의 모든 테스트가 성공
- [x] 프로그래밍 요구 사항에서 달리 명시하지 않는 한 파일, 패키지 이름을 수정하거나 이동 X
- [x] indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현(2까지만 허용)
- [x] 3항 연산자 사용 X
- [x] 함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 만들기
- [x] JUnit 5와 AssertJ를 이용하여 정리한 기능 목록이 정상적으로 작동하는지 테스트 코드로 확인
- [x] 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현
- [x] else 예약어 사용 X
- [x] Java Enum을 적용하여 프로그램을 구현
- [x] 구현한 기능에 대한 단위 테스트를 작성(단, UI(System.out, System.in, Scanner) 로직은 제외)
- [x] 입출력을 담당하는 클래스를 별도로 구현

### 라이브러리 요구 사항

- [x] camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현
- [x] 사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 readLine()을 활용
- [x] 현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 now()를 활용