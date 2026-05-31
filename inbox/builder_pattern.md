---
type: deep_study
pattern: Builder
category: 생성 패턴 (Creational)
course: 객체지향설계와패턴
created: 2026-05-31
related: ["[[factory_method_pattern]]", "[[abstract_factory_pattern]]"]
exam_signal: true
---

# Builder 패턴

## 한 줄 정의
> 선택 필드가 많은(또는 조립 규칙/순서가 중요한) 객체를,
> **위치가 아니라 이름 붙은 메서드로** 하나씩 설정해 조립하고,
> **`build()` 시점에 검증 후 생성**하는 패턴.

## 출발점: 생성자만으로는 안 되는 이유

Lab 9의 User: 필수 2개(firstName, lastName) + 선택 3개(age, phone, address).
생성자로만 처리하면 → **점층적 생성자(telescoping constructor)** 폭발:

```java
User(String firstName, String lastName)
User(String firstName, String lastName, int age)
User(String firstName, String lastName, int age, String phone)
User(String firstName, String lastName, int age, String phone, String address)
```

진짜 문제는 개수가 아니라 **"값을 자리(위치)로 구분"**하는 데서 오는 꼬임:

**1. 같은 타입 인자가 자리만 바뀌어도 컴파일러가 못 잡는다.**
```java
new User("세윤", "김", 25, "서울", "010-1234-5678");
//                        ↑phone자리  ↑address자리 — 뒤바뀜! 컴파일 OK, 런타임에 한참 뒤 발견
```
**2. 안 쓰는 선택 필드도 억지로 채워야 한다.**
`new User("세윤","김",25,"","서울")` — 저 빈 문자열이 "값 없음"인지 "실수로 빠뜨림"인지 알 수 없음.

> 또 다른 대안 JavaBeans(빈 생성자 + setter 떡칠)도 결함이 둘:
> ① 객체가 **반쯤 만들어진 상태**로 세상에 존재(setter 다 부르기 전까지 불완전) — Lab 9 (2)번 코드가 이것
> ② `final`을 못 쓴다(가변이어야 하므로 불변 객체 불가)

## 해결: 이름표 붙여 설정 + build()

```java
User u = new User.Builder("세윤", "김")   // 필수는 여기서
                 .age(25)                 // 선택은 이름표 붙여서
                 .address("서울")          // phone은 안 부름 = 안 넣음
                 .build();                // 다 모았으면 진짜 객체 생성
```

- 값을 **위치가 아니라 메서드 이름**으로 지정 → 순서 꼬임 불가능
- 안 쓰는 필드는 그냥 안 부르면 끝 (null 억지로 안 채움)
- 메서드가 타입을 강제 → `.age("서울")`은 **컴파일 에러** (위치 방식과 정반대)

## 작동 원리 1: 메서드 체이닝 = `return this`

```java
public Builder age(int age) {
    this.age = age;
    return this;        // ← 나를 돌려줘서 또 점 찍을 수 있게
}
```
`this`를 리턴하니 `.age(25)`의 결과가 다시 Builder → `.address(...)`를 이어붙일 수 있다.
Builder가 읽기 좋은 이유의 절반이 이 `return this`.

## 작동 원리 2: build() = 검증의 길목

```java
public User build() {
    if (firstName == null || lastName == null)   // 필수값 검사
        throw new IllegalStateException("이름은 필수입니다");
    if (age < 0)
        throw new IllegalArgumentException("나이가 음수일 수 없습니다");
    return new User(this);                        // 다 통과해야 진짜 생성
}
```

왜 하필 `build()`인가:
- 그 전(`.age()`, `.address()`)은 아직 **미완성** 상태라 검증 불가
- `build()`는 "조립이 다 끝났다"는 선언 → **모든 값이 모인 상태에서 한꺼번에 검증** 가능한 유일한 길목
- 통과 못 하면 객체가 **아예 안 만들어짐** → 손에 들어온 User는 "항상 유효한 객체" 보장. 반쯤 망가진 객체가 새어나갈 수 없음.

> 이것이 [[factory_method_pattern]]과 **같은 원리**다: 팩토리가 "생성을 메서드 뒤로 숨겨" 캐싱/검증을 끼워넣었듯, Builder는 "생성을 `build()` 뒤로 숨겨" 검증을 끼워넣는다. **생성을 감싸면 그 안에 규칙을 넣을 수 있다** — 생성 패턴 전체를 관통하는 한 줄.

## 작동 원리 3: 불변 객체(immutable) 친화

User 필드가 전부 `final`이면 setter를 못 쓴다(`final`은 생성 시 한 번만). 생성자로 다 넣자니 telescoping 지옥.
Builder가 딜레마 해결: **값은 Builder에 자유롭게 쌓아두고(가변), `build()` 순간 final 필드에 한 방에 박아(불변) 객체 생성.**
→ 조립할 땐 말랑말랑, 완성품은 단단하게.

## 심화 / 실무 / 시스템 레벨

- **(실무) 표준 라이브러리에 깔린 Builder들**: `StringBuilder`(`sb.append("a").append("b")` — `return this` 그대로), `Stream.builder()`, `HttpRequest.newBuilder().uri(...).header(...).build()`.
- **(⚠️ 시험 시그널) GoF 원조 Builder ≠ Effective Java식 Builder.**
  - 우리가 주로 배운 건 *Effective Java*식(필드 채우기 + 가독성/안전성).
  - **GoF 원전 Builder의 의도는 "같은 조립 과정으로 서로 다른 표현(representation)을 만든다."** 예: 문서를 "제목→본문→마무리" 같은 순서로 조립하되, 결과를 HTML/PDF/Markdown 등으로 뽑음.
  - 구조: 조립 절차를 지휘하는 **Director** + 각 형식을 실제로 만드는 여러 **ConcreteBuilder**(HtmlBuilder, PdfBuilder...). Director는 순서만 알고, 무슨 형식인지는 어떤 Builder가 꽂혔느냐가 결정 → **위임 구조** (cf. [[factory_method_pattern]]의 위임).
  - **교수님이 GoF 워크북 기준으로 출제하면 Director 버전을 정답으로 볼 가능성** → exam_signals 참조.
- **(시스템) Builder와 가비지(GC)**: 진짜 객체를 만들려고 Builder 객체를 하나 더 만든다 → 매번 임시 객체가 생겼다 버려짐. 수백만 번 생성하는 고성능 코드(게임 루프, 대용량 처리)에선 Builder 쓰레기가 GC에 부담. 대부분 무시 가능하나 극한 성능 상황에선 피하기도 함. (→ [[factory_method_pattern]]의 "캐싱으로 GC 압력 줄이기"와 정반대편 트레이드오프)

## 정리
- **문제**: 선택 필드 많은 객체를 생성자로 → telescoping 폭발 + 같은 타입 순서 꼬임 + 안 쓰는 필드 억지 채우기
- **해결**: 이름 붙은 메서드로 설정 · `return this` 체이닝 · `build()`에서 검증 후 생성
- **이득**: 가독성 / 순서 무관 / 컴파일 타임 타입 안전 / 항상 유효한 객체 / 불변 객체 친화

## 연결
- [[factory_method_pattern]] — "생성을 감싸 규칙을 끼워넣는다"는 같은 원리
- [[abstract_factory_pattern]] — 같은 생성 패턴 묶음 (Abstract Factory는 "한 집안을 일관 생성", Builder는 "한 객체를 단계적 조립")
