# CHANGELOG — lab12 코드 변경 이력

> 노트(`오퍼레이션_패턴_학습정리.md`)의 "완전 코드"를 출발점으로 lab12 프로젝트에 옮긴 뒤,
> 코드 리뷰를 거치면서 이루어진 변경과 그 이유를 정리한 문서.

---

## 0. 출발점 — 초기 이식 상태

| 패키지 | 파일 수 | 출처 |
|---|---|---|
| `command/` | 8 | 노트 §3 "Lab12 완전 코드" 그대로 |
| `decorator2/` | 5 | 노트 §3 "Lab12 완전 코드" 그대로 (Shape 계열) |
| `iterator/` | 3 | 노트 §3 "Lab12 완전 코드" 그대로 |

**이식 시 가한 유일한 변경**: 각 파일 첫 줄에 `package <폴더명>;` 선언 추가.
본문은 한 글자도 바꾸지 않음. (이는 노트의 코드 자체가 학습용 정답이라는 전제)

> `decorator/` 폴더(Coffee 계열)는 Lab 8에서 가져온 별도 예제로, 본 문서의 변경 대상이 아님.

---

## 1. 코드 변경 사항

### 1-1. `iterator/BookShelfIterator.java` — `next()` 경계 체크 추가

**변경 전 (노트 그대로)**

```java
@Override public Book next() {
    Book book = bookShelf.getBookAt(index);
    index++;
    return book;
}
```

**변경 후**

```java
@Override public Book next() {
    if (!hasNext()) throw new NoSuchElementException();
    Book book = bookShelf.getBookAt(index);
    index++;
    return book;
}
```

`import java.util.NoSuchElementException;` 도 함께 추가.

**변경 이유 — `java.util.Iterator` 계약 위반**

- `java.util.Iterator`의 `next()`는 자바독에 **"더 이상 요소가 없을 때 `NoSuchElementException`을 던져야 한다"** 라고 명시되어 있다. `implements Iterator<Book>`을 선언한 시점에 이 계약을 받아들인 것.
- 변경 전 코드는 끝까지 가면 `ArrayIndexOutOfBoundsException`이 터진다 — **다른 예외**. 호출 측이 `NoSuchElementException`을 잡으려고 try-catch를 쓰면 잡히지 않는다.
- `for-each` 루프 안에서는 `hasNext()`가 선행되므로 실전에서는 안 터지지만, `it.next()`를 직접 부르는 코드(이번에 추가된 `iterator/Main.java`가 정확히 그 경우)에서 차이가 드러난다.
- **원칙**: 표준 인터페이스를 구현했다면 그 계약까지 지키는 것이 패턴 적용의 일부다. 노트엔 빠져 있지만 코드에서는 보강하는 게 맞다.

---

### 1-2. Demo / Main 클래스 신규 추가 (3개)

| 파일 | 출처 | 역할 |
|---|---|---|
| `command/Demo.java` | Lab 1-2의 `Demo` 코드 | **GoF Command 패턴의 5번째 역할 = Client** |
| `decorator2/Main.java` | Lab 2-1의 `DecoratorPatternDemo` 코드 | 단순 실행 호스트 |
| `iterator/Main.java` | Lab 3-2의 `Main` 코드 | 단순 실행 호스트 |

**왜 Command만 `Demo`이고 나머지는 `Main`인가**

- Command 패턴은 다섯 가지 역할(Command · ConcreteCommand · Receiver · Invoker · **Client**)을 가지며, **Client는 단순한 main 함수가 아니라 패턴 안에서 고유한 책임**(Receiver 생성 + ConcreteCommand로 짝짓기 + Invoker에 꽂기)을 진다. 이 "조립의 책임"을 가진 자가 곧 Client.
- 그래서 `Demo`라는 이름이 **GoF 5번째 역할명의 자리**다. 단순 호스트가 아닌 패턴 구성 요소.
- Decorator와 Iterator의 main은 그런 패턴 역할이 없다. 그냥 실행을 돌리는 호스트일 뿐 → 일반적인 `Main` 이름이 적절.
- Lab 문서도 3-2에서는 실제 `Main`이라고 명명하고 있어 의도 일치.

**노트(Q&A) 인용 — Command의 무지(無知) 관계**

| 역할 | 안다 | 위치 |
|---|---|---|
| **Client** | 전부 안다 | `Demo` |
| **Invoker** | ICommand 하나만 안다 | `HomeAutomationRemote` |
| **Receiver** | 자기 일만 안다. 자기가 Command 안에 있는지조차 모름 | `Light`, `Fan` |

`docs/1-1_command.puml`의 3대 노트가 이 무지 관계를 시각적으로 보여준다.

---

### 1-3. `command/Demo.java` — Lab 원문 오타 정상화

**Lab 1-2 원문**

```java
HomeAutomationRemote remote = new homeAutomationRemote();   // 소문자 h
```

**작성된 코드**

```java
HomeAutomationRemote remote = new HomeAutomationRemote();   // 정상화
```

**이유**: Lab 원문은 컴파일 자체가 안 되는 명백한 타이포. 의도적 명명이 아닌 단순 오타로 판단해 정상화. 의미적 변경은 없음.

---

## 2. 다이어그램 변경 사항

### 2-1. `docs/1-1_command.puml` — Client(Demo) 추가

**최초 작성 시 누락**: Demo(Client) 클래스를 다이어그램에서 빠뜨려, Command 패턴의 5가지 역할 중 4개만 표현됨.

**수정 후 추가된 요소**

- `Demo <<Client>>` 클래스 박스
- Demo → 모든 의존 대상(Light · Fan · 4×ConcreteCommand · HomeAutomationRemote)으로 **점선 의존(`..>`) 7가닥**, 라벨은 `<<create>>` 또는 `<<create & use>>`
- 무지 관계를 시각적으로 강조하는 **노트 3개** (Demo 옆 / Invoker 옆 / Receiver 옆)

**이유**: Client는 "리모컨 버튼에 기능을 프로그래밍하는 자"로서 **조립의 책임을 가진 패턴 역할**. 다이어그램에서 빼면 "ConcreteCommand와 Receiver를 짝지어 Invoker에 꽂는 일이 누구의 책임인가"가 공백으로 남는다. 다이어그램은 코드가 아니라 **설계 의도의 시각화**이므로, 패턴 역할이 빠지면 안 된다.

> Decorator와 Iterator 다이어그램은 호스트 클래스(`Main`)를 포함하지 않음 — 그것은 패턴 역할이 아니라 단순 실행자이므로 다이어그램에 그릴 가치가 없음. Command와의 비대칭은 의도된 것.

---

## 3. 의도적으로 보존한 것 — "노트와 일치" 우선

다음 두 항목은 코드 리뷰에서 **명백히 개선 여지가 있다고 지적**되었으나, "노트 깎을 때 같이 정리할 거리"로 분류하여 **노트와 코드의 일관성**을 우선해 보존함.

### 3-1. `RedShapeDecorator.draw()` — `decoratedShape.draw()` 직접 호출

**현재 (노트 그대로)**

```java
@Override public void draw() {
    decoratedShape.draw();          // 부모의 protected 필드를 직접 사용
    setRedBorder(decoratedShape);
}
```

**개선 방향 (보류)**

```java
@Override public void draw() {
    super.draw();                   // 부모의 위임 로직 활용
    setRedBorder(decoratedShape);
}
```

**지적 요지**: 추상 `ShapeDecorator`를 만든 이유 중 하나는 "공통 위임 로직을 한 군데에 모은다"이다. 자식이 부모와 똑같은 일을 직접 다시 하면, 추상 클래스를 만든 가치 절반이 사라진다. 예를 들어 부모의 위임 전후로 공통 로그를 추가하고 싶을 때 `super.draw()`를 쓰던 자식은 자동 혜택, 직접 호출하던 자식은 못 받음.

**보존 사유**: 현재 결과 동작은 동일. 노트 자체가 직접 호출 형태이므로, **노트를 깎을 때 둘을 함께 동기화**하는 편이 일관성 유지에 유리하다.

### 3-2. `RedShapeDecorator.setRedBorder(Shape s)` — 죽은 인자

**현재**

```java
private void setRedBorder(Shape s) { System.out.println("Border Color: Red"); }
```

**개선 방향 (보류)**

```java
private void setRedBorder() { System.out.println("Border Color: Red"); }
```

**지적 요지**: 인자 `s`를 받지만 사용하지 않음. 사람이 읽을 때 "원래 뭘 하려던 거지?"라는 의문이 생긴다. Lab 의도가 단순 출력이므로 인자를 빼는 것이 맞다.

**보존 사유**: 3-1과 동일.

> 만약 도형 종류에 따라 분기하려고 `instanceof Circle` 같은 코드를 넣기 시작하면 노트 §4.7의 안티패턴 "타입 식별 깨짐"으로 이어짐. 그래서 인자를 빼는 것이 정답이며, 분기 추가가 아님.

---

## 4. 의식하고 두는 것 — 변경 안 함, 다만 알아두기

### 4-1. `BookShelfIterator`가 외부 클래스인 점

- 현재 `BookShelfIterator`는 `BookShelf` 바깥에 있고, `bookShelf.getBookAt(index)`로 책에 접근한다. 즉 `getBookAt(int)`라는 **인덱스 접근 가능성을 전제**하고 있다.
- 만약 `BookShelf` 내부가 `Book[]` → `LinkedList<Book>`로 바뀌면, `getBookAt(int)`는 O(1) → O(n)이 되고 전체 순회가 O(n²)이 된다.
- **정통 해법**: `BookShelfIterator`를 `BookShelf`의 inner class로 만들면 `books` 배열에 직접 접근 가능 + 캡슐화 강화. 자바 표준 컬렉션(`ArrayList`, `HashMap` 등)은 모두 자기 Iterator를 inner class로 둔다 (`private class Itr implements Iterator<E>` 형태).
- **현재 형태 유지 이유**: Lab의 단순함과 가독성. 다만 "Iterator가 Aggregate의 인덱스 접근 권한을 가지고 있다"는 비대칭만 인식하고 가는 것으로 충분.

---

## 5. 노트 ↔ 코드 동기화 상태 한눈에

| 항목 | 코드 상태 | 노트 상태 | 동기화 |
|---|---|---|---|
| `RedShapeDecorator.draw()` 위임 방식 | `decoratedShape.draw()` (직접) | 동일 | ✅ |
| `RedShapeDecorator.setRedBorder` 인자 | `(Shape s)` 미사용 | 동일 | ✅ |
| `BookShelfIterator.next()` 경계 체크 | **있음 (NoSuchElementException)** | 없음 | ⚠️ 코드가 앞섬 |
| Demo / Main 호스트 클래스 | 3개 모두 추가 | 노트엔 없음 (Lab 문서엔 있음) | — |
| `command.Demo` 생성자 호출 | `new HomeAutomationRemote()` (정상화) | 노트엔 없음 (Lab 원문은 오타) | — |

**다음 노트 정리 시 처리 권장 항목** (위 표의 ⚠️ + §3의 보존 항목):

1. `BookShelfIterator.next()`에 `NoSuchElementException` 추가 — 노트와 코드 동기화
2. `RedShapeDecorator.draw()`를 `super.draw()` 호출로 변경 + 노트 동기화
3. `RedShapeDecorator.setRedBorder()`의 죽은 인자 제거 + 노트 동기화

---

## 부록. docs 파일 목록

| 파일 | 내용 |
|---|---|
| `1-1_command.puml` | Command 패턴 클래스 다이어그램 (5개 역할 모두 포함) |
| `2-1_decorator.puml` | Decorator 패턴 클래스 다이어그램 |
| `3-1_iterator.puml` | Iterator 패턴 클래스 다이어그램 |
| `CHANGELOG.md` | (이 문서) |
