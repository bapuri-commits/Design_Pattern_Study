---
type: deep_study
pattern: Visitor
category: behavioral / operation
lab: Lab12
related:
  - "[[double_dispatch]]"
  - "[[open_closed_principle]]"
  - "[[decorator_pattern]]"
  - "[[strategy_pattern]]"
created: 2026-05-29
---

# Visitor 패턴 — 비대칭의 패턴

## 한 줄 요약

> **"Element 계층은 안정적이고, 연산(Operation)은 자주 추가될 때"** 가성비가 나오는 패턴.
> 그 외의 상황에선 함정이 더 많다.

---

## 1. 골격

```
ItemElement (interface)
  └── accept(visitor)        ← 더블 디스패치의 1단

ShoppingCartVisitor (interface)
  ├── visit(Book)
  ├── visit(Fruit)            ← 더블 디스패치의 2단 (오버로딩)
  └── ...

ConcreteVisitor (e.g. ShoppingCartVisitorImpl, GrocerVisitor)
  └── 각 visit(...)의 실제 구현
```

핵심은 두 번 디스패치되는 흐름:

```java
item.accept(visitor)          // (1) item의 런타임 타입으로 디스패치 (오버라이딩)
  → visitor.visit(this)       // (2) this의 컴파일타임 타입으로 디스패치 (오버로딩)
```

- (1)은 런타임. `ItemElement` 참조 변수가 실제로 `Book`인지 `Fruit`인지에 따라 다른 `accept`가 호출됨.
- (2)는 컴파일타임. `this`의 타입이 컴파일 시점에 이미 `Book` 또는 `Fruit`로 확정되어 있어서, 그에 맞는 오버로드가 선택됨.

**오버로딩(컴파일타임) + 오버라이딩(런타임)의 조합**으로,
자바의 단일 디스패치 한계를 우회해 "두 타입 모두에 의존하는 호출"을 만들어낸다.

---

## 2. 패턴의 비대칭 — 직접 측정한 비용

Lab12 작업으로 새 Element 두 번, 새 Visitor 한 번 추가하면서 측정한 실측치:

| 작업 | 신설 | 수정 강제 | 총 손댄 파일 |
|---|---|---|---|
| Meat 추가 (Element) | 1 | 2 | **3** |
| Grain + Vegetable 추가 (Element ×2) | 2 | 2 | **4** |
| GrocerVisitor 추가 (Visitor) | 1 | 0 | **1** |

수정 강제된 파일은 **Visitor 인터페이스 + 모든 ConcreteVisitor**.
지금은 ConcreteVisitor가 둘뿐이라 비용이 작아 보이지만:

- ConcreteVisitor가 10개 있는 시스템에서 새 Element 추가 → **11개 파일 수정 강제**
- 같은 시스템에서 새 Visitor 추가 → **0개 수정**, 새 파일 하나

이 비대칭이 GoF가 "**Visitor는 Element 계층이 안정적일 때만 써라**"라고 못박은 진짜 이유.
글로 읽으면 흘러가지만 손으로 직접 빨간줄을 보고 나면 머리가 아니라 손에 새겨진다.

### 적용 판단 기준

| 시스템 특성 | Visitor 가성비 |
|---|---|
| Element 종류 거의 고정, 연산만 늘어남 (AST, 컴파일러 IR) | 천국 |
| 둘 다 자주 변함 | 지옥 |
| Element가 자주 늘어남 (비즈니스 도메인) | 함정 |

---

## 3. 갈림길 — Element를 변경하고 싶을 때

질문: "Visitor로 Element를 수정할 수도 있지 않나?"

답: 할 수는 있다. 다만 **케이스를 갈라야** 한다.

### 케이스 A. 단순 상태 갱신 (모든 Element에 동일한 변경)
예: 모든 Fruit 가격 10% 인상.
→ **Visitor 안 씀.** Fruit에 `applyDiscount(rate)` 메서드 추가가 정답.
Element 종류별로 다르게 처리할 일이 없으니 더블 디스패치의 가치가 0.

### 케이스 B. Element 종류별로 다른 변환 (구조 변환 포함)
예: AST의 상수 폴딩 — `BinaryOp(*, IntLit(3), IntLit(4))` → `IntLit(12)`.
노드 종류별로 변환 로직이 완전히 다르고, 이런 변환이 수십 개 늘어남 (상수 폴딩, 데드 코드 제거, 인라이닝, ...).
→ **Visitor가 빛나는 자리.**

### 캡슐화는 이미 양보된 상태

> "근데 setter 추가하면 캡슐화가 너무 깨지는 거 아냐?"

오해. Visitor 패턴 자체가 이미 캡슐화 양보 위에 서 있다.
`visit(Book)`에서 `book.getPrice()`로 내부를 끄집어내 외부에서 계산하는 것 자체가
"Book이 자기 계산을 직접 안 하고 외부에 노출시킨" 행위.

GoF Consequences에서도 단점으로 명시:
> "Visitor는 ConcreteElement의 인터페이스가 충분히 강력해야 동작한다.
> 즉 Element가 자기 내부를 충분히 노출해야 하므로 캡슐화가 깨질 수 있다."

setter 추가는 그 양보를 **더 깊게** 할 뿐이지, 새로운 위반은 아니다.
판단 기준은 "캡슐화"가 아니라 "Visitor가 진짜 필요한 시나리오인가."

---

## 4. 함정 — "이미 가공된 결과를 재사용하지 마라"

GrocerVisitor를 만들면서 자연스럽게 떠오르는 발상:

```java
// 안티 패턴
public class GrocerVisitor implements ShoppingCartVisitor {
    private ShoppingCartVisitorImpl retail = new ShoppingCartVisitorImpl();

    @Override public int visit(Book book)   { return 0; }
    @Override public int visit(Fruit fruit) { return retail.visit(fruit) * 7 / 10; }
    @Override public int visit(Meat meat)   { return retail.visit(meat) * 7 / 10; }
    // ...
}
```

"retail Visitor의 결과를 받아서 70% 곱하자" — Decorator 발상에 가까운, 객체지향적으로 정당해 보이는 사고.
**그러나 이 시나리오에선 망가진다.** 세 가지 이유:

### (1) 시맨틱이 어긋남
retail `visit(Meat)`은 Premium 등급이면 2배 할증을 적용한 가격을 반환.
Grocer 시나리오는 "도매가는 등급 무관"인데, retail 결과를 받는 순간 **Premium 할증이 그대로 살아남음.**
- Normal Meat: cost × 0.7 → 의도대로
- Premium Meat: cost × 2 × 0.7 = cost × 1.4 → **의도와 정반대**

Grain의 대량할인도 마찬가지로 따라옴.

### (2) 부수효과까지 끌려옴
retail `visit`은 `System.out.println("Book ISBN : ...")`까지 하는데,
Grocer가 그걸 호출하는 순간 **retail의 println이 그대로 찍힘.** 끌 수도 없음.

> 함수형의 "순수 함수는 합성하기 좋다, 부수효과 있는 함수는 위험하다"의 정확한 사례.
> 자바/OO에서도 동일한 결의 교훈.

### (3) 의도와 구현이 어긋남
다음 개발자는 "Grocer는 retail × 0.7이구나" 라고 읽는데,
실제로는 "retail의 모든 분기(할증/할인)를 거친 결과 × 0.7."
그리고 누가 retail의 할증을 3배로 바꾸면 **Grocer도 자기 모르게 영향받음.**
"Grocer가 retail 정책에 종속"되는 결합이 생긴다.

### 정답
각 ConcreteVisitor는 **Element의 raw 데이터(getter)에서 직접 계산.** 서로 모름.

```java
@Override
public int visit(Fruit fruit) {
    return fruit.getPricePerKg() * fruit.getWeight() * 7 / 10;
}
```

Visitor 두 개는 같은 Element 위에서 서로 다른 **독립적 해석자**다.
이게 패턴 본래 의도.

### 부가 정리: "역계산"이 의미를 가지려면

도매상이 소매가 × 0.7로 도매가를 정한다는 발상은
**소매가가 "도매가 + 마진" 구조일 때만** 말이 된다.
그러나 retail Impl은 "단가 × 무게 × 자기 정책"으로 짠 가격이지,
"도매가에 마진 얹은" 구조가 아니다.
그러니까 역으로 빼는 게 의미 자체가 없음.

이 판단을 어떻게 했는지 기억해 두자 — **"기존 결과를 재사용해도 되나"의 진짜 기준은
"기존 결과가 새 결과의 자연스러운 베이스인가"**다.

---

## 5. Java 문법 메모 (C++ 대조)

작업 중 짚힌 자바 문법 두 가지:

### 메서드 오버로딩
이름 같고 시그니처 다른 메서드 정의 — C++과 동일.
시그니처 = 이름 + 파라미터 타입 목록 + 개수.
**반환 타입은 시그니처에 안 들어감**, 파라미터 이름도 무관.

C++과의 차이: **자바는 디폴트 인자가 없음.** C++의 `void f(int x = 0)`은
자바에선 `f()` / `f(int)` 두 메서드로 오버로딩해서 풀어야 함.

### 오버로딩 vs 오버라이딩 (Visitor 패턴 이해의 열쇠)
| | 오버로딩 | 오버라이딩 |
|---|---|---|
| 정의 | 같은 이름, 다른 시그니처 | 상속받아 같은 시그니처로 재정의 |
| 디스패치 | **컴파일타임** | **런타임** |
| 예 | `visit(Book)` vs `visit(Fruit)` | `Book.accept()` vs `Fruit.accept()` |

Visitor 패턴이 더블 디스패치를 만들어내는 게 정확히 이 두 메커니즘의 조합.

### enum
"정해진 선택지 몇 개" 표현하는 정식 도구. 문자열 상수보다 안전.

```java
public enum Grade { NORMAL, PREMIUM }
if (meat.getGrade() == Grade.NORMAL) { ... }  // == 비교 안전
```

오타를 컴파일러가 잡아주고, 어떤 값이 있는지 한 파일에서 보임.
자바 enum은 C++ enum보다 강력 (메서드 가질 수 있는 사실상의 클래스).

### boolean getter 네이밍 컨벤션
`getIsOrganic()`이 아니라 `isOrganic()`. 자바 표준 컨벤션.

---

## 6. 학습 흐름 회고 (Lab12 실제 작업)

1. zip 받음 → 표준 Book/Fruit Visitor 예제 + 폴더명에 과제 지시
   "Meat, Fruit와 Grocer를 추가해서 방문하기(visit)"
2. **Meat 추가 (Element)**: 인터페이스 + Impl 손대는 비용 체감
3. Grain, Vegetable 추가 (Element ×2): 같은 비용 한 번 더
4. **GrocerVisitor 추가 (Visitor)**: 새 파일 하나, 기존 코드 무손
5. 두 Visitor를 같은 카트에 돌려서 비교 출력 → Retail 455, Grocer 193

도중에 짚은 갈림길들:
- (a) Element에 setter 추가해서 Grocer를 풀려고 한 발상 → 시나리오 해석의 문제. 시나리오 B(가격표 자체 변경)면 setter가 맞지만 그건 Visitor가 아니라 그냥 setter. 시나리오 A(다른 해석자)면 Visitor가 정답이고 Element는 안 건드림.
- (b) GrocerVisitor가 Impl 결과 ×0.7 → 위 §4의 함정. 안티 패턴.

**오늘의 진짜 산물은 두 가지:**
- 비대칭의 무게를 손으로 느낀 것 (책으로 읽는 것과 질적으로 다름)
- "Visitor를 어떻게 쓸까"가 아니라 "Visitor를 써야 하는가"부터 묻는 습관

---

## 7. 다른 패턴과의 관계

- [[double_dispatch]] — Visitor의 작동 메커니즘 그 자체. 이 개념을 독립 노트로 떼어 둘 가치 있음.
- [[decorator_pattern]] — "기존 동작을 감싸 변형"하는 정식 패턴. §4의 안티 패턴이 어설프게 Decorator를 흉내 낸 셈.
- [[strategy_pattern]] — "정책을 갈아끼움"의 일반화. Element 한 종류에 대해 여러 정책을 갈아끼우는 거라면 Strategy가 맞음.
- [[open_closed_principle]] — Visitor의 비대칭은 정확히 OCP의 비대칭. 연산 축으로는 열려있고(추가에 닫힌 코드 수정 불필요), Element 축으로는 닫혀있지 않음(추가 시 기존 코드 수정 필요).
