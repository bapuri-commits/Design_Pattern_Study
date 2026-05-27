# OO Design and Patterns - Lab 자료 (10)

---

# Laboratory 10 | Exercise Patterns

**Lab 10: 구축 패턴(2)**

성명: ________________

학번: ________________

목차
- InLab #1. Prototype 패턴의 적용
- InLab #2. Memento 패턴의 적용

Version 2.0

---

## InLab #1: Prototype 패턴

### 1-1.

프로토타입 디자인 패턴은 기존 객체를 복제하여 새로운 객체를 생성할 수 있는 생성 패턴이다. 객체를 만드는 데 비용이 많이 들고 기존 객체를 쉽게 수정하여 새 객체를 만들 수 있는 경우에 유용한 패턴이다.

**(1) 프로토타입 패턴을 이루는 세 가지 컴포넌트는 무엇이며 각각을 역할을 설명하라.**

> _(작성란)_

---

**(2) 다음은 프로토타입 패턴을 사용한 코드이다. 어떤 부분에 문제가 있는가? 문제를 해결하기 위하여 코드를 수정하라.**

```java
class ConcretePrototype {
    private String name;

    public ConcretePrototype(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
class PrototypeClient {
    public static void main(String[] args) {
        ConcretePrototype prototype = new ConcretePrototype("Original Object");
        ConcretePrototype clone = prototype; // directly assign original object to clone

        clone.setName("Clone Object");

        System.out.println("Original Object: " + prototype.getName()); // Clone Object
        System.out.println("Clone Object: " + clone.getName()); // Clone Object
    }
}
```

> _(작성란)_

---

## InLab #2: Memento 패턴

### 2-1.

객체의 내부 상태를 기록해야 하는 경우가 있다. 예를 들면 게임이나 그래픽 편집에서 현재의 상태가 어디엔가 남겨져 있어 게임이 종료되거나 편집 상태가 없어지는 것을 막기 위한 것이다. 또한 사용자가 임시 작업을 취소하거나 오류를 복구할 수 있는 체크포인트 및 "실행 취소" 메커니즘을 구현할 때 필요하다.

객체를 이전 상태로 복원할 수 있도록 상태 정보를 어딘 가에 저장해야 한다. 그러나 객체는 일반적으로 상태의 일부 또는 전부를 캡슐화 하므로 다른 객체에 액세스할 수 없고 외부에 저장할 수 없다. 이 상태를 노출하면 캡슐화를 위반하여 애플리케이션의 안정성과 확장성을 손상시킬 수 있기 때문이다.

메멘토 패턴을 x, y 좌표 값을 가지고 있는 originator 객체에 적용하기 위한 프로그램을 설계하라.

```
+-------------+          +-------------+          +-------------+
|  Originator |--------->|   Memento   |<---------|  CareTaker  |
| setMemento  |          | getState()  |          |             |
| createMemento|          +-------------+          +-------------+
| state       |
+-------------+
```

**(1) 다음 요구사항을 반영하여 각 요소를 코딩하라.**

- 메멘토에 저장해야 하는 Originator 클래스는 두 개의 double 유형의 필드 x 및 y 를 포함하고 CareTaker 의 참조도 사용한다. CareTaker 는 Originator 객체의 상태를 나타내는 memento 객체를 저장하고 검색하는 데 사용된다.
- 생성자에서 createSavepoint 메서드를 사용하여 객체의 초기 상태를 저장한다. 이 메서드는 memento 객체를 만들고 CareTaker 에게 객체를 관리하도록 요청한다.
- 실행 취소 작업을 구현하기 위해 마지막으로 저장된 memento 의 키 이름을 저장하는 데 사용되는 lastUndoSavepoint 변수를 사용하라.
- Originator 는 세 가지 유형의 실행 취소 작업을 제공하여야 한다. 매개변수가 없는 실행취소 방법은 마지막으로 저장된 상태를 복원하고 저장 좌표 이름을 매개변수로 사용하는 실행취소는 특정 저장포인트 이름으로 저장된 상태를 복원한다. undoAll 메서드는 CareTaker 에게 모든 저장점을 지우고 초기 상태(객체가 생성된 시점의 상태)로 설정하도록 요청한다.
- CareTaker 는 요청한 memento 객체를 저장하고 제공하는 데 사용된다. memento 객체를 저장하는 데 사용되는 saveMemento 메소드, 요청 memento 객체를 반환하는 데 사용되는 getMemento 및 모든 저장점을 지우는 데 사용되는 clearSavepoints 메소드가 포함되어야 하며 저장된 모든 memento 객체를 삭제한다.

\<Originator.java\>

> _(작성란)_

---

\<Memento.java\>

> _(작성란)_

---

\<CareTaker.java\>

> _(작성란)_

---

**(2) 메멘토 패턴을 적용한 결과를 다음 두 가지 관점에서 평가하라.**

- 캡슐화의 관점

> _(작성란)_

- 객체 상태 보관의 효율

> _(작성란)_

- Originator 와 CareTaker 에 제공하는 인터페이스 관점

> _(작성란)_

---

## PostLab #10: 회고 보고서

Lab #10 의 주제는 객체를 구축하는 패턴이었습니다. 프로토타입, 메멘토 패턴을 알고 코딩으로 체험하기 위한 목적이 강의와 액티비티를 통하여 얼마나 잘 이해되었는지 또한 무엇을 배웠는지 자신의 언어로 정리해 봅시다.

> _(작성란)_
