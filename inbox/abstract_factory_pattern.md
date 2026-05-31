---
type: deep_study
pattern: Abstract Factory
category: 생성 패턴 (Creational)
course: 객체지향설계와패턴
created: 2026-05-31
related: ["[[factory_method_pattern]]", "[[builder_pattern]]"]
---

# Abstract Factory 패턴

## 한 줄 정의
> 관련 있는 것들끼리만, **한 집안(family)으로 일관되게** 생성하는 패턴.
> "family of related objects"를 일관되게 만든다.

## 출발점: Factory Method에서 한 걸음

Factory Method는 만들 게 **하나(모터)**였다. 그런데 부품이 여러 개라면?

상황: 엘리베이터를 만드는데 **모터** + **버튼 패널**이 둘 다 필요하다. 그리고 제약:
- LG 엘리베이터 → LG 모터 + LG 버튼패널
- Hyundai 엘리베이터 → Hyundai 모터 + Hyundai 버튼패널
- **LG 모터에 Hyundai 버튼패널을 섞으면 안 된다** (서로 안 맞음)

→ "모터 하나만 만드는 팩토리"가 아니라 **"한 세트를 통째로 만드는 팩토리"**가 필요.

## 해결: 만드는 메서드가 여러 개인 팩토리

```java
interface ElevatorFactory {
    Motor createMotor();          // 모터도 만들고
    ButtonPanel createButton();   // 버튼패널도 만들고
}

class LGFactory implements ElevatorFactory {
    public Motor createMotor()        { return new LGMotor(); }
    public ButtonPanel createButton() { return new LGButton(); }
}
class HyundaiFactory implements ElevatorFactory {
    public Motor createMotor()        { return new HyundaiMotor(); }
    public ButtonPanel createButton() { return new HyundaiButton(); }
}
```

## 핵심: 집안 일관성이 자동으로 지켜진다

`LGFactory` 하나를 손에 쥐는 순간, 거기서 나오는 건 모터든 버튼이든 **전부 LG짜리**다.
섞고 싶어도 섞을 수 없다 — 한 팩토리 안에 같은 집안 부품만 들어있으니까.

```java
void assemble(ElevatorFactory factory) {
    Motor m = factory.createMotor();
    ButtonPanel b = factory.createButton();
    // m과 b는 무조건 같은 집안. 신경 쓸 필요도 없음
}
```

→ Abstract Factory가 푸는 진짜 문제: 단순히 "여러 개 만들기"가 아니라
**"서로 어울려야 하는 한 묶음(family)을 일관되게 만들기."**

## Factory Method와의 관계 (경쟁 아님, 포함)

- `LGFactory.createMotor()` 같은 **메서드 하나하나가 그 자체로 Factory Method**다.
- 즉 **Abstract Factory = 여러 개의 Factory Method를 묶어놓은 것.**
- 둘은 경쟁이 아니라 Abstract Factory가 Factory Method를 **품고 있는** 관계.

| | Factory Method | Abstract Factory |
|---|---|---|
| 만드는 종류 | 1종류 | 여러 종류 |
| 관심사 | 그 하나를 어떤 구체클래스로 만들지 결정을 서브클래스에 위임 | 관련 있는 것들끼리만 한 집안으로 일관되게 생성 |

## 구성품 생성 방식은 자유
각 `createXxx()` 안에서 부품을 만드는 방식은 자유다. 부품 생성이 단순하면 `new`로 한 줄 처리(Simple Factory처럼)면 되고, 부품 자체가 복잡하면 그 안에서 또 다른 팩토리를 호출하는 **중첩 구조**도 가능하다. 필요할 때만.

## 실무 큰 그림
- **DI 프레임워크(Spring IoC 컨테이너) = 초거대 Abstract Factory.** `@Autowired`로 객체를 받으면 프레임워크가 "어떤 구체 클래스를, 어떤 부품들과 함께" 한 집안으로 조립해 건넨다. → [[factory_method_pattern]]에서 "결정을 가장 바깥으로 모으기"를 통째로 자동화한 것.

## 연결
- [[factory_method_pattern]] — Factory Method 여러 개가 묶이면 Abstract Factory
- [[builder_pattern]] — 같은 생성 패턴 묶음. 단, Builder는 "한 객체를 단계적으로 조립"하는 쪽
