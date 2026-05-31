---
type: deep_study
pattern: Adapter
category: 인터페이스/구조 패턴 (Structural)
course: 객체지향설계와패턴
week: 6 (인터페이스 패턴)
created: 2026-05-31
related: ["[[facade_pattern]]", "[[factory_method_pattern]]"]
---

# Adapter 패턴

## 한 줄 정의
> 인터페이스가 안 맞는 두 코드를 **둘 다 못 고치는 상황**에서,
> 중간에 변환기를 끼워 이어붙이는 패턴. **목적 = 변환** (A 모양 → B 모양).

## 출발점: 문제

내 시스템은 `Duck`만 받고 부른다. 코드 곳곳이 이 타입으로 고정:

```java
interface Duck { void quack(); void fly(); }

class DuckPond {
    void add(Duck duck) { ... }      // Duck만 받음 (파라미터 타입 고정)
    void makeAllQuack() { d.quack(); } // 들어온 게 Duck이라 믿고 quack() 호출
}
```

그런데 외부 라이브러리의 칠면조를 끼우고 싶다. 인터페이스가 다름:

```java
interface Turkey { void gobble(); void fly(); }  // quack 아님 = gobble
```

### 두 개의 벽
1. **타입이 안 맞아 컴파일 불가**: `pond.add(turkey)` → Turkey는 Duck이 아니라 ❌
2. **메서드 이름이 안 맞음**: 시스템은 `quack()`을 부르는데 칠면조는 `gobble()`만 안다

### 제약 (문제를 어렵게 만드는 핵심)
- **시스템 코드 못 고침**: `add(Duck)`, `quack()`이 50군데에 산재. 다 고치는 건 팩토리 때 피하려던 그 짓. 게다가 거위(Goose)가 또 오면 또 고쳐야 함.
- **칠면조 코드 못 고침**: 외부 라이브러리라 소스가 없거나, 건드리면 업데이트 때 날아감.

## 해결: 어댑터를 중간에 끼운다

한쪽 인터페이스를 `implements`(타입 맞춤) + 반대쪽 객체를 필드로 품고 위임(이름 맞춤):

```java
class TurkeyAdapter implements Duck {   // ← Duck을 구현 → 타입상 진짜 Duck (벽1 해결)
    private Turkey turkey;              // ← Turkey를 필드로 품음 (래핑)

    public TurkeyAdapter(Turkey turkey) { this.turkey = turkey; }

    public void quack() { turkey.gobble(); }  // quack 요청을 gobble로 위임 (벽2 해결)
    public void fly()   { turkey.fly(); }
}
```

```java
Duck adapter = new TurkeyAdapter(new WildTurkey());
pond.add(adapter);   // ✅ adapter는 Duck이니까 통과. 시스템은 칠면조인 줄 모른 채 quack 호출
```

→ **시스템도 칠면조도 한 줄 안 고침.** 중간에 변환기만 새로 추가. 거위가 와도 `GooseAdapter`만 추가 (OCP, cf. [[factory_method_pattern]]).

> 비유: **여행용 전원 어댑터.** 콘센트(Duck)도 기기 플러그(Turkey)도 못 바꾸니, 한쪽은 콘센트 모양(implements Duck) 안쪽은 유럽 플러그를 무는(필드로 Turkey) 변환기를 끼운다.

## 합성 vs 상속 (객체 어댑터 vs 클래스 어댑터)

어댑터를 만드는 두 방법:
- **클래스 어댑터(상속)**: `class TurkeyAdapter extends WildTurkey implements Duck`
- **객체 어댑터(합성)**: 위 코드처럼 Turkey를 **필드로 품음** ← 표준

**합성이 이기는 이유 2가지:**
1. **런타임 교체**: 필드라서 감싸는 대상을 실행 중에 갈아끼울 수 있음. 상속은 `extends WildTurkey`로 **컴파일 시점에 고정**되어 못 바꿈.
2. **단일 상속 제약 회피**: 자바는 `extends` 하나뿐. 상속 어댑터는 그 자리를 써버려서 **오직 한 클래스만** 변환 가능. 필드는 제약 없음 → 여러 종류를 한 어댑터로 받을 수 있음.

→ "상속보다 합성을 선호하라"가 Adapter에서도 그대로 작동. 실무·교재 표준은 **객체 어댑터(합성)**.

## 정리
- **문제**: 인터페이스 다른 두 코드를 둘 다 못 고치는데 이어붙여야 함
- **해결**: 중간 어댑터 — 한쪽을 `implements`(타입) + 반대쪽을 필드로 품고 위임(이름)
- **선택**: 객체 어댑터(합성) — 런타임 교체 + 단일 상속 회피

## 연결
- [[facade_pattern]] — 짝꿍이지만 **목적 정반대**. Adapter=변환(A→B, 개수 그대로) / Facade=단순화(여러 개→하나)
- [[factory_method_pattern]] — 같은 **위임(has-a + 일 넘기기)** 뼈대. 구조 패턴 전반이 이 구조.
