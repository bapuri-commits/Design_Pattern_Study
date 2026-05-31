---
type: deep_study
pattern: Factory Method
category: 생성 패턴 (Creational)
course: 객체지향설계와패턴
created: 2026-05-31
related: ["[[abstract_factory_pattern]]", "[[builder_pattern]]"]
---

# Factory Method 패턴

## 한 줄 정의
> **무엇을** 만드는지는 호출자가 몰라도, **만드는 방법(인터페이스)**은 쥐고 있게 하는 패턴.
> 어떤 구체 클래스를 만들지의 **결정을 서브클래스에 위임**한다.

## 출발점: 문제

객체를 만드는 가장 평범한 방법은 `new`다.

```java
Motor motor = new LGMotor();
```

이게 코드 여기저기 **산재**하면, LG → Hyundai로 바꿀 때:
- **50군데를 다 고쳐야** 한다 (한 곳이라도 빠뜨리면 LG/Hyundai가 섞여 돌아가는 버그 — 컴파일러도 못 잡음)
- **OCP 위반**: 모터 교체는 "변경"인데, 그걸 하려고 기존 클라이언트 코드를 전부 뜯어고쳐야 한다 → "변경에 닫혀 있어야 한다"가 무너짐

## 1단계 해결: Simple Factory (단순 팩토리)

생성 코드를 한 곳에 모은다.

```java
class MotorFactory {
    public Motor createMotor() {
        return new LGMotor();   // 바뀌면 여기만 수정
    }
}
```

50군데 → 1군데로 줄었다. **변경(교체) 시나리오에는 이걸로 충분하다.**
(이때 팩토리가 자기 코드를 고치는 건 위반이 아니라 자기 책임을 다하는 것.)

### Simple Factory의 한계
종류가 **추가**되는 시나리오에서 무너진다:

```java
public Motor createMotor(String type) {
    if (type.equals("LG")) return new LGMotor();
    else if (type.equals("Hyundai")) return new HyundaiMotor();
    else if (type.equals("Samsung")) return new SamsungMotor();  // 추가될 때마다 if 증식
    // ...
}
```

모터가 추가될 때마다 이 메서드를 **열어서 고쳐야** 하고, 멀쩡하던 기존 분기를 깨뜨릴 위험이 생긴다. OCP 위반이 다시 등장.

## 2단계 해결: Factory Method

팩토리를 인터페이스/추상클래스로 두고, **서브클래스마다** 만들 종류를 다르게 한다.

```java
abstract class MotorFactory {
    public abstract Motor createMotor();   // "무엇을 만들지"는 자식에게 위임
}

class LGMotorFactory extends MotorFactory {
    public Motor createMotor() { return new LGMotor(); }
}
class HyundaiMotorFactory extends MotorFactory {
    public Motor createMotor() { return new HyundaiMotor(); }
}
```

이제 Samsung 모터가 추가되면 → `SamsungMotorFactory` **새 클래스를 추가**할 뿐,
기존 코드는 **한 줄도 안 건드린다.** → "확장엔 열려, 변경엔 닫혀" 달성.

### Simple Factory vs Factory Method 핵심 차이
**"어떤 구체 클래스를 만들지 결정하는 위치"**가 다르다.
- Simple Factory: 한 메서드 **안에서** if-else로 결정 → 고치려면 메서드를 연다
- Factory Method: **어느 서브클래스를 쓰느냐**로 결정 → 새 서브클래스 추가로 확장

## 결정은 어디로 가나 — 가장 바깥으로

클라이언트는 자기가 LG를 쓰는지 모르게 한다 (팩토리를 주입받음):

```java
class ElevatorController {
    private Motor motor;
    public ElevatorController(MotorFactory factory) {   // 어떤 팩토리인지 밖에서 받음
        this.motor = factory.createMotor();
    }
}

// 진짜 결정은 프로그램 가장 바깥(main) 한 곳:
MotorFactory factory = new LGMotorFactory();
ElevatorController c = new ElevatorController(factory);
```

→ "50군데에 퍼져 있던 결정이 1곳으로 모인" 것. **돌아온 게 아니라 도착한 것.**

## 팩토리 vs 단순 주입 (언제 팩토리가 값을 하나)

만들 게 단순하고 하나뿐이면 그냥 생성자 주입(`new LGMotor()` 직접 주입)이 낫다 — 팩토리는 오버엔지니어링.
팩토리가 의미를 갖는 경우:
1. **생성 과정이 한 줄이 아닐 때** (설정 읽기·검증·초기화·등록 등 절차를 숨김)
2. **"지금"이 아니라 "나중에, 여러 번" 만들어야 할 때** — 미리 만든 객체 하나로는 안 되고 "만드는 방법"을 들고 있어야 함

> 비유: 단순 주입 = **다 만든 빵을 건넴** / 팩토리 = **빵 만드는 레시피를 건넴**
> 손님 올 때마다 갓 구운 빵이 필요하면 레시피를 줘야 한다.

## 팩토리의 본질 (중요한 교정)

❌ 팩토리 = 같은 객체를 **여러 개** 만드는 것
✅ 팩토리 = **객체를 얻는 방법을 호출자에게서 숨기는 것 (캡슐화)**

`factory.createMotor()` 안에서 무슨 일이 벌어지는지 호출자는 모르고, 알 필요도 없다:
- 매번 새로 만들기 (`new`)
- 이미 만든 거 돌려주기 (캐싱)
- 풀에서 빌려주기 (오브젝트 풀)
- 하나만 만들어 계속 그것만 (Singleton)

→ "여러 개"는 그중 한 경우일 뿐. **이 정책을 나중에 바꿔도 호출자 코드는 한 글자도 안 변한다.**

## 심화 / 실무 / 시스템 레벨

- **(실무) Simple Factory가 더 흔하다.** 종류 추가가 드문 닫힌 집합이면 서브클래스 5개 만드는 게 과하다. 패턴은 "추가가 빈번하다"는 비용을 정당화할 때만 값을 한다. GoF 정석은 외우되 실무는 비용-편익으로 판단.
- **(구분 주의) 정적 팩토리 메서드(Effective Java)는 GoF Factory Method와 다른 것.** `Boolean.valueOf()`, `List.of()`, `Optional.of()` 같은 정적 메서드. 장점: ① 이름이 있다(`of`, `from`, `valueOf`로 의도 표현) ② 매번 새 객체를 안 만들어도 된다(`Integer.valueOf()`는 -128~127 캐싱).
- **(시스템) `new`가 하는 일**: 클래스 로딩 → 힙 메모리 할당 → 필드 기본값 초기화 → 생성자 실행. `new`는 항상 새로 밟지만, 팩토리로 감싸면 그 사이에 캐싱/풀링을 끼워 GC 압력을 줄일 수 있다.
- **(연계) Singleton, Object Pool, Flyweight는 전부 "생성을 팩토리로 감싸 객체 재사용을 끼워넣은" 변주.**
- **(실무 큰 그림) DI 프레임워크 = 거대한 Abstract Factory.** Spring IoC 컨테이너가 "가장 바깥의 조립"을 자동화한 것. → [[abstract_factory_pattern]]

## 관련 원칙
- **OCP** (개방-폐쇄): 확장에 열리고 변경에 닫힌다 → 종류 추가를 새 클래스로
- **DIP** (의존성 역전): 호출자가 구체클래스(`LGMotor`)가 아니라 추상(`Motor`)에 의존하게 됨. 구체 클래스 이름이 호출자 코드에서 사라진다.
  - cf. 교수님 실무 경험담: "보험사 앱 DB context 직접 생성 = DIP 위반" → 팩토리로 숨기면 해결
- **위임(delegation)**: 컨트롤러가 모터 생성 책임을 팩토리에 위임 → 팩토리를 갈아끼워 행동을 바꿈

## 연결
- [[abstract_factory_pattern]] — Factory Method가 여러 개 묶이면 Abstract Factory가 된다
- [[builder_pattern]] — 같은 "생성을 감싸 검증/규칙을 끼워넣는다" 원리의 다른 적용
