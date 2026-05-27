# OO Design and Patterns - Lab 자료 (7)

---

# Laboratory 07 | Exercise Patterns

**Lab 7: 책임 패턴(1)**

성명: ________________

학번: ________________

목차
- InLab #1. Singleton 패턴의 적용
- InLab #2. Observer 패턴의 적용
- InLab #3. Mediator 패턴의 적용

Version 2.0

---

## InLab #1: Singleton 패턴

### 1-1. 다음 클래스 중에 싱글톤을 적용하여야 할 것으로 보이는 클래스는 무엇인가? 그 이유는?

```
OurBiggestRocket        TopSalesAssociate

java.lang.Math                      java.lang.System
-Math()                             +out:PrintStream
+pow(a:double,b:double):double           ↓
                                    PrintStream

PrintSpooler            PrinterManager
```

> _(작성란)_

---

### 1-2. 다음 프로그램은 카드 게임을 위하여 작성한 것이다. 클래스 중에 싱글톤으로 만들어야 적절한 것은 무엇인가? 그 클래스를 싱글톤 패턴을 적용하여 수정해 보라.

```java
import java.util.*;

enum Suit {
  SPADES,
  HEARTS,
  CLUBS,
  DIAMONDS
}

class Card {
  public Card(Suit s, int n) {
    suit = s;
    if((n < 2) || (n > 14)) {
      throw new IllegalArgumentException( );
    }
    number = n;
  }

  public void print( ) {
    switch(number) {
      case 11:
        System.out.print("Jack");
        break;
      case 12:
        System.out.print("Queen");
        break;
      case 13:
        System.out.print("King");
        break;
      case 14:
        System.out.print("Ace");
        break;
      default:
        System.out.print(number);
        break;
    }
    System.out.print(" of ");
    switch(suit) {
      case SPADES:
        System.out.println("spades.");
        break;
      case HEARTS:
        System.out.println("hearts.");
        break;
      case CLUBS:
        System.out.println("clubs.");
        break;
      case DIAMONDS:
        System.out.println("diamonds.");
        break;
    }
  }

  private Suit suit;
  private int number;
}

class Deck {
  public Deck( ) {
    cards = new ArrayList<Card>( );

    // build the deck
    Suit[] suits = {Suit.SPADES, Suit.HEARTS, Suit.CLUBS, Suit.DIAMONDS};
    for(Suit suit: suits) {
      for(int i = 2; i <= 14; i++) {
        cards.add(new Card(suit, i));
      }
    }

    // shuffle it!
    Collections.shuffle(cards, new Random( ));
  }

  public void print( ) {
    for(Card card: cards) {
      card.print( );
    }
  }
```

> _(작성란)_

---

## InLab #2: 옵서버 패턴

### 2-1. 숫자를 생성하는 Subject 와 이를 관찰하고 그 값을 표시하는 Observer 사이의 관계를 옵서버 패턴으로 설계하여 클래스 다이어그램으로 그려라.

[힌트] 설계에 필요한 클래스는 다음과 같다.

1. Observer – 관찰자를 나타내는 인터페이스
2. NumberGenerator – 숫자를 생성하는 객체를 나타내는 추상 클래스
3. RandomNumberGenerator – 랜덤하게 수를 생성하는 클래스
4. DigitalObserver – 만들어진 수를 아라비아 숫자로 표시하는 클래스
5. GraphObserver – 막대 그래프로 수를 표시하는 클래스

메인 프로그램과 실행 결과는 다음과 같이 되어야 한다.

```java
public class Main {
    public static void main(String[] args) {
        NumberGenerator generator = new RandomNumberGenerator();
        Observer observer1 = new DigitObserver();
        Observer observer2 = new GraphObserver();
        generator.addObserver(observer1);
        generator.addObserver(observer2);
        generator.execute();
    }
}
```

\<실행 결과의 예\>

```
DigitalObserver: 24
GraphObserver: ************************
DigitalObserver: 23
GraphObserver: ***********************
DigitalObserver: 39
GraphObserver: ***************************************
DigitalObserver: 48
GraphObserver: ************************************************
DigitalObserver: 8
GraphObserver: ********
```

**\<클래스 다이어그램\>**

> _(작성란)_

---

### 2-2. 위 설계를 기반으로 각 클래스를 코딩하라.

> _(작성란)_

---

### 2-3. 다음은 팬, 전원 공급 장치 및 버튼으로 구성된 간단한 냉각 시스템을 구축한다고 상상해 보자. 버튼을 누르면 팬이 켜지거나 꺼진다. 팬을 켜기 전에 전원을 켜야 한다. 마찬가지로 팬이 꺼진 직후에 전원을 꺼야 한다. 모든 것이 잘 작동하는 것 같다. 그러나 Button, Fan 및 PowerSupplier 클래스가 너무 밀접하게 결합되어 있다. Button 은 Fan 에서 직접 작동하고 Fan 은 Button 및 PowerSupplier 와 상호 작용하기 때문이다.

이렇게 하면 다른 모듈에서 Button 클래스를 재사용하는 것은 어려울 것이다. 또한 시스템에 두 번째 전원 공급 장치를 추가해야 하는 경우 Fan 클래스의 논리를 수정해야 한다. 중재자 패턴을 구현하여 클래스 간의 종속성을 줄이고 코드를 더 재사용할 수 있도록 수정해 보라.

```java
public class Button {
    private Fan fan;

    // constructor, getters and setters

    public void press(){
        if(fan.isOn()){
            fan.turnOff();
        } else {
            fan.turnOn();
        }
    }
}
public class Fan {
    private Button button;
    private PowerSupplier powerSupplier;
    private boolean isOn = false;

    // constructor, getters and setters

    public void turnOn() {
        powerSupplier.turnOn();
        isOn = true;
    }

    public void turnOff() {
        isOn = false;
        powerSupplier.turnOff();
    }
}
public class PowerSupplier {
    public void turnOn() {
        // implementation
    }
    public void turnOff() {
        // implementation
    }
}
```

> _(작성란)_

---

## InLab #3: 중재자 패턴

### 3-1. 고객 프로필을 생성하고 편집하기 위한 대화 상자가 있다고 가정해 보자. 텍스트 필드, 체크박스, 버튼 등과 같은 다양한 양식의 컨트롤로 구성된다. 양식 안의 어떤 요소는 다른 요소와 상호 작용할 수 있다. 예를 들어, '나는 개인 블로그가 있습니다' 확인란을 선택하면 블로그의 주소를 입력할 수 있는 숨겨진 텍스트 필드가 표시될 수 있다. 또 다른 예는 데이터를 저장하기 전에 모든 필드의 값을 검증해야 하는 제출 버튼이다.

그림 3-1. 중재자 패턴이 적용되지 않은 설계 (Profile Dialog / LogIn Dialog)

- Profile Dialog: Button ↔ Dialog, Button, Tabs, Checkbox ↔ TextField
- LogIn Dialog: Button ↔ Dialog, TextField, Checkbox ↔ TextField

이런 설계를 채택한 경우 각 요소들이 다른 애플리케이션에서 재사용되려면 그림 3-2 와 같은 로직이 필요하다. 이런 로직을 모든 양식 요소의 코드 내부에 직접 구현하면 또 다른 앱의 양식에서 재사용하기가 훨씬 더 어려워진다. 예를 들어 해당 확인란 클래스는 블로그 주소의 텍스트 필드와 연결되어 있으므로 다른 양식 내에서 사용할 수 없다.

그림 3-2 Checkbox 요소의 재사용:

```
Checkbox
- dialog
+ onCheck()

if (dialog.name == "profile_form")
  // ...
if (dialog.name == "login_form")
  // ...
```

**(1) 위 설계를 중재자 패턴을 이용하여 각 요소들이 더 쉽게 재사용되도록 설계하는 방법을 설명하라.**

> _(작성란)_

---

**(2) 버튼, 체크박스, 텍스트 레이블 등 다양한 UI 클래스 간의 상호 종속성을 제거한 중재자 패턴이 적용된 설계를 그림으로 그려라.(단 각 요소들의 커뮤니케이션을 중재하는 요소는 Dialog 로 한다)**

> _(작성란)_

---

## PostLab #7: 회고 보고서

Lab #7 의 주제는 책임 패턴이었습니다. 싱글톤, 옵서버, 중재자 패턴의 차이를 알고 코딩으로 체험하기 위한 목적이 강의와 액티비티를 통하여 얼마나 잘 이해되었는지 또한 무엇을 배웠는지 자신의 언어로 정리해 봅시다.

> _(작성란)_
