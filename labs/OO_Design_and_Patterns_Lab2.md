# OO Design and Patterns - Lab 자료 (2)

---

## InLab #4: 마이크로웨이브 오븐 제어 시스템

### 프로젝트 개요 (p.29)

마이크로웨이브 오븐은 마이크로웨이브로 음식의 분자를 진동시켜 음식을 가열한다.

사용자는 키패드를 사용하여 오븐이 얼마 동안, 어떤 파워 수준으로 음식을 가열할 것인지 알린다. 즉 키패드는 0부터 9까지의 숫자키와 start, stop/clear, power 키가 있다.

누링을 하려면 문을 열고 음식을 넣은 다음 숫자키를 누를 때마다 하나의 자릿수가 왼쪽으로 시프트되어 입력되는데 4자리까지의 입력이 가능하다. start 버튼을 누르면 마이크로웨이브 오븐에 라이트가 켜지면서 타이머에 저장된 시간만큼 가동된다. 타이머가 멈추면 오븐을 중지시키고 종을 울린다.

마이크로웨이브 오븐이 가동 중에 문이 열려도 마이크로웨이브 오븐이 멈춘다. 타이머는 멈추고 라이트는 꺼진다. 다시 문을 닫은 후 start 버튼을 누르면 남은 시간만큼 마이크로웨이브 오븐이 작동된다. 문이 열렸을 때 숫자키를 눌러 시간을 세팅할 수 있다. 그러나 start 버튼이나 stop 버튼은 활성되지 않는다.

세팅된 시간을 취소하려면 stop/clear 버튼을 누르면 0으로 세팅된다.

마이크로파 발생기는 플라이스트론을 제어하여 작동한다.

---

## Exercise #1: CRC 카드를 이용한 클래스 찾기 (p.30)

**목적:** 마이크로웨이브 오븐 시스템의 클래스 찾기와 CRC 카드를 이용한 클래스 찾기 연습

### 수행 방법

1. 위에 설명한 마이크로웨이브 오븐 도메인에 대하여 클래스 후보가 될만한 것들을 찾아 적는다.
2. 클래스의 이름을 영어로 붙이고 클래스의 책임과 협력 클래스들을 찾아본다.
3. 아래 워크시트의 CRC 카드에 하나의 클래스를 기재한다.

### CRC 카드 작성 결과

#### Class: Oven

| Responsibilities | Collaborations |
|-----------------|----------------|
| Know powerLevel | (없음) |
| Set powerLevel | |
| Start heating | |
| Stop heating | |

#### Class: Timer

| Responsibilities | Collaborations |
|-----------------|----------------|
| Know remainingTime | Oven |
| Set time | |
| Count down | |
| Stop | |
| Notify completion | |
| Get time | |

#### Class: Door

| Responsibilities | Collaborations |
|-----------------|----------------|
| Know doorState | Oven |
| Notify open | Timer |
| Notify close | |

#### Class: Keypad

| Responsibilities | Collaborations |
|-----------------|----------------|
| Know inputBuffer (기본값 0000) | Timer |
| Enter digit (시프트 입력) | Oven |
| Press start | |
| Press stop/clear | |
| Press power | |

---

## Exercise #2: 클래스 다이어그램 작성 (p.33)

**목적:** 앞에서 찾은 마이크로웨이브 오븐 시스템의 클래스들로 다이어그램을 작성하는 연습

### 수행 방법

1. Exercise #1에서 찾은 클래스를 이용하여 클래스들로 그 다이어그램을 기입한다.
2. 클래스 사이의 관계를 보고 이를 관계를 담당하는 다이어그램에 표시한다.
3. 다이어그램에 나타낸 관계가 빠진 것이 없는지 체크하면서 따라가면서 점점 관계가 빨진 것이다.

### UML 표기법 (예시: 주문 시스템 참고)

| 표기 | 의미 |
|------|------|
| 화살표 없음 (양방향 선) | Association — info can flow in both directions |
| 단방향 화살표 | Association — 한쪽에서만 참조 |
| 빈 다이아몬드 (`◇—`) | Aggregation — contains 관계 |
| 속이 찬 다이아몬드 (`◆—`) | Composition — 강한 소유 관계 |
| 삼각형 화살표 | Generalization (상속) |
| 숫자 (`1`, `0..*`, `1..*`) | Multiplicity |

### 마이크로웨이브 오븐 클래스 다이어그램

```
          ┌─────────────────────────┐
          │         Timer           │
          ├─────────────────────────┤
          │ remainingTime : int = 0 │
          ├─────────────────────────┤
          │ setTime()               │
          │ countDown()             │
          │ stop()                  │
          │ notifyCompletion()      │
          │ getTime()               │
          └────────────┬────────────┘
          1 ↑        1 ↑          1 ↑
            │          │            │
          (Keypad)  (Timer→Oven)  (Door)
            │          │            │
┌───────────┴──┐  ┌────┴──────────┐  ┌──┴────────────┐
│    Keypad    │  │     Oven      │  │     Door      │
├──────────────┤→1├───────────────┤1←├───────────────┤
│inputBuffer:  │  │powerLevel:int │  │doorState:bool │
│  int = 0     │  ├───────────────┤  ├───────────────┤
├──────────────┤  │setPowerLevel()│  │notifyOpen()   │
│enterDigit()  │  │startHeating() │  │notifyClose()  │
│pressStart()  │  │stopHeating()  │  └───────────────┘
│pressStopClear│  └───────────────┘
│pressPower()  │
└──────────────┘
```

**관계 요약 (모두 단방향 association, multiplicity 1:1)**

| 주체 | → | 대상 | 근거 |
|------|---|------|------|
| Keypad | → | Timer | pressStart(), setTime() 호출 |
| Keypad | → | Oven | setPowerLevel() 호출 |
| Door | → | Timer | notifyOpen/Close → stop() 호출 |
| Door | → | Oven | notifyOpen/Close → stopHeating/startHeating() 호출 |
| Timer | → | Oven | startHeating(), stopHeating() 호출 |

---

## Exercise #3: 시퀀스 다이어그램 작성 (p.35)

**목적:** 각 사용 사례에 대한 내부 객체들의 협력 관계를 시퀀스 다이어그램으로 작성하는 연습

### 수행 방법

1. Exercise #3에서 기술한 사용 사례들이 어떤 메시지들이 어떤 객체들에게 보내지는지 순서를 시퀀스 다이어그램으로 그린다.
2. 메소드 와 행을 했는 클래스 안에 메소드가 정의되어 있지 않았다면 클래스 다이어그램에 추가한다.

### UML 표기법

| 표기 | 의미 |
|------|------|
| 수직 점선 | Lifeline — 객체의 생존선 |
| 좁은 직사각형 (lifeline 위) | Activation — 메소드 실행 구간 |
| 실선 화살표 | Message — 메소드 호출 |
| 점선 화살표 | Return — 반환값 |
| 자기 자신으로 향하는 화살표 | Self-call — 내부 메소드 호출 |
| 점선 테두리 박스 | 특정 구간 표시 (인터럽트, 루프 등) |

### 시나리오 1: 정상 작동

```
Door        Keypad       Timer        Oven
 |             |            |            |
 |—notifyOpen()————————————————————————→|  1. 문 열림
 |     [음식 넣음 - 사용자 액션]           |
 |—notifyClose()———————————————————————→|  2. 문 닫힘
 |             |            |            |
 |             |—setPowerLevel()————————→|  3. 파워 설정
 |             |—setTime()——→|            |  4. 시간 설정
 |             |—start()————→|            |  5. 시작
 |             |            |←countDown() |     (self-call)
 |             |            |—startHeating()→|  6. 가열 시작
 |             |            |            |
 |             |    [가열 중 . . .]       |
 |             |            |            |
 |             |            |—notifyCompletion()→|  7. 시간 종료
 |             |            |            |←stopHeating()
 |—notifyOpen()————————————————————————→|  8. 음식 꺼냄
```

### 시나리오 2: 문 열림 인터럽트

```
Door        Keypad       Timer        Oven
 |             |            |            |
 |—notifyOpen()————————————————————————→|  1. 문 열림
 |     [음식 넣음 - 사용자 액션]           |
 |—notifyClose()———————————————————————→|  2. 문 닫힘
 |             |            |            |
 |             |—setPowerLevel()————————→|  3. 파워 설정
 |             |—setTime()——→|            |  4. 시간 설정
 |             |—start()————→|            |  5. 시작
 |             |            |←countDown() |     (self-call)
 |             |            |—startHeating()→|  6. 가열 시작
 |             |            |            |
 | ╔══ 인터럽트: 문 열림 ════════════════╗ |
 |—notifyOpen()————————————————————————→|  7a. stopHeating()
 |—notifyOpen()————→|            |        |  7b. stop()
 |             |            |            |
 |—notifyClose()———————————————————————→|  8a. 문 닫힘 알림
 |—notifyClose()———→|            |        |  8b. Timer 알림
 | ╚═════════════════════════════════════╝ |
 |             |            |            |
 |    [사용자: start 버튼 재입력]          |
 |             |—start()————→|            |  9a. 재시작
 |             |            |←countDown() |      (self-call)
 |             |            |—startHeating()→|  9b. 가열 재시작
 |             |            |            |
 |             |            |—notifyCompletion()→|  10. 시간 종료
 |             |            |            |←stopHeating()
 |—notifyOpen()————————————————————————→|  11. 음식 꺼냄
```
