# OO Design and Patterns - Lab 자료 (12)

---

# Laboratory 12 | Exercise Patterns

**Lab 12: 오퍼레이션 패턴(2)**

성명: ________________

학번: ________________

목차
- InLab #1. Command 패턴의 적용
- InLab #2. Decorator 패턴의 적용
- InLab #3. Iterator 패턴의 적용

Version 2.0

---

## InLab #1: Command 패턴

커맨드 패턴은 GoF 의 동작 디자인 패턴으로 주로 오퍼레이션에 대한 패턴이다. 커맨드 패턴에서 Command 인터페이스는 특정 명령을 실행하기 위한 메서드 오퍼레이션을 선언한다. 여기서 상속된 구체적인 Command 클래스들이 각 명령에 대한 execute() 메서드를 구현하고 이 명령에 의하여 수행되는 과정에 명령을 받는 Receiver 클래스의 적절한 작업 메서드를 호출한다. Receiver 클래스가 결국 특정 작업을 수행하는 것이다. 클라이언트 클래스는 구체적인 명령을 만들고 구체적인 명령의 Receiver 를 설정하는 역할을 한다. Invoker 클래스에는 Command 에 대한 참조가 포함되어 있고 Command 를 실행하는 메서드가 있다.

```
+----------+         +----------+          +---------------------+
|  Client  |- - - - -|  Invoker |◇--------▶|      Command        |
+----------+         +----------+          +---------------------+
     |                                     | +execute(): void    |
     | <<instantiate>>                     +---------------------+
     |                                                △
     ▼                                                │
+----------+                            +----------------------+    ┌──────────────────┐
| Receiver |◀────── receive ────────────|   ConcreteCommand    |- - │ execute() {      │
+----------+                            +----------------------+    │   receiver.action();│
|+action():void|                         | -state: int          |    │ }                │
+----------+                            | +execute(): void     |    └──────────────────┘
                                        +----------------------+
```

### 1-1.

가정의 다양한 조명/전기 장치를 제어하는 홈 오토메이션 시스템용 원격 제어 장치를 구축해야 한다고 가정한다. 리모컨에는 하나의 버튼이 유사한 장치에서 동일한 작업을 수행할 수 있어야 한다. 즉 TV 켜기/끄기 버튼을 사용하여 다른 방에 있는 다른 TV 세트를 켜고 끌 수 있다. 리모컨은 프로그래밍 가능한 리모컨이며 다양한 조명/팬 등을 켜고 끄는 데 사용된다.

커맨드 패턴을 사용하지 않고 문제를 해결할 수 있는 방법을 살펴보면 리모컨 코드는 다음과 같을 수 있다.

```java
If(buttonName.equals("Light"))
{
    //Logic to turn on that light
}
else If(buttonName.equals("Fan"))
{
    //Logic to turn on that Fan
}
```

위의 솔루션에는 분명히 다음과 같은 눈에 띄는 많은 문제가 있다.

- 새 항목(예: TubeLight)을 추가하려면 if-else 를 더 추가하여 리모컨 코드를 변경해야 한다.
- 버튼을 다른 용도로 변경하려면 코드도 모두 변경해야 한다.
- 집에 물건이 많을수록 코드의 복잡도와 유지보수성이 높아진다.
- 코드가 깨끗하지 않고 밀접하게 결합되어 있으며 인터페이스를 사용하여 융통성 있게 코딩하라는 모범 사례를 따르지 않고 있다.

전등(Light)과 선풍기(Fan)을 홈오토리모컨(HomeAutomationRemote)으로 제어하기 위한 설계를 Command 패턴을 이용하여 클래스 다이어그램으로 작성하라. 설계에 포함되어야 할 구성 요소는 다음과 같다.

- ICommand – 명령 인터페이스
- Light – Receiver 중 하나. 켜기(on) 및 끄기(off)와 같은 조명과 관련된 여러 명령을 받을 수 있다.
- Fan – 또 다른 유형의 Receiver. 켜고(on) 끄기(off)와 같은 팬과 관련된 여러 명령을 받을 수 있다.
- HomeAutomationRemote – 요청을 수행하도록 명령을 요청하는 호출자. 팬 켜기/끄기, 조명 켜기/끄기.
- StartFanCommand, StopFanCommand, TurnOffLightCommand, TurnOnLightCommand 등은 다른 유형의 명령 구현입니다.

> _(작성란)_

---

### 1-2.

다음과 같은 클라이언트 코드가 수행될 수 있도록 위에서 설계한 각 요소들을 코딩하라.

```java
/**
 * Demo class for HomeAutomation
 *
 */
public class Demo    //client
{
    public static void main(String[] args)
    {
        Light livingRoomLight = new Light();     //receiver 1

        Fan livingRoomFan = new Fan();   //receiver 2

        Light bedRoomLight = new Light();    //receiver 3

        Fan bedRoomFan = new Fan();      //receiver 4

        HomeAutomationRemote remote = new
            homeAutomationRemote();   //Invoker

        remote.setCommand(new TurnOnLightCommand( livingRoomLight ));
        remote.buttonPressed();

        remote.setCommand(new TurnOnLightCommand( bedRoomLight ));
        remote.buttonPressed();

        remote.setCommand(new StartFanCommand( livingRoomFan ));
        remote.buttonPressed();

        remote.setCommand(new StopFanCommand( livingRoomFan ));
        remote.buttonPressed();

        remote.setCommand(new StartFanCommand( bedRoomFan ));
        remote.buttonPressed();

        remote.setCommand(new StopFanCommand( bedRoomFan ));
        remote.buttonPressed();
    }
}
```

\<ICommand.java\>

> _(작성란)_

---

\<Light.java\>

> _(작성란)_

---

\<Fan.java\>

> _(작성란)_

---

\<TurnOffLightCommand.java\>

> _(작성란)_

---

\<TurnOnLightCommand.java\>

> _(작성란)_

---

\<StartFanCommand.java\>

> _(작성란)_

---

\<StopFanCommand.java\>

> _(작성란)_

---

\<HomeAutomationRemote.java\>

> _(작성란)_

---

## InLab #2: Decorator 패턴

### 2-1.

데코레이터 패턴은 객체에 추가 책임을 부여한다. 즉 데코레이터는 기능을 확장하기 위하여 서브클래스를 만드는 방법에 대하여 더 유연한 대안을 제공한다.

예를 들어 그래픽 편집기를 만들기 위하여 Circle, Rectangle 같은 Shape 이 필요하다. 즉 Shape 인터페이스와 Shape 인터페이스를 구현하는 구체적인 클래스를 만들 것이다. 여기 Shape 에 Red 색상을 둘레에 입히는 RedShapeDecorator 를 추가하라.

전체적으로 필요한 요소는 Shape 인터페이스와 Shape 객체를 인스턴스 변수로 갖는 추상 데코레이터 클래스 ShapeDecorator 이다. 또한 RedShapeDecorator 는 ShapeDecorator 를 구현하는 구체적인 클래스이다. DecoratorPatternDemo, 데모 클래스는 RedShapeDecorator 를 사용하여 Shape 개체를 장식한다..

**(1) 위 기능을 가능하게 하는 데코레이터 패턴을 이용한 설계를 클래스 다이어그램으로 그려라. 설계는 다음 클라이언트가 작동될 수 있어야 한다.**

```java
DecoratorPatternDemo.java
public class DecoratorPatternDemo {
    public static void main(String[] args) {

        Shape circle = new Circle();

        Shape redCircle = new RedShapeDecorator(new Circle());

        Shape redRectangle = new RedShapeDecorator(new Rectangle());
        System.out.println("Circle with normal border");
        circle.draw();

        System.out.println("\nCircle of red border");
        redCircle.draw();

        System.out.println("\nRectangle of red border");
        redRectangle.draw();
    }
}
```

> _(작성란)_

---

**(2) 다음 각 요소를 코딩하라.**

\<Shape.java\>

> _(작성란)_

---

\<Rectangle.java\>

> _(작성란)_

---

\<Circle.java\>

> _(작성란)_

---

\<ShapeDecorator.java\>

> _(작성란)_

---

\<RedShapeDecorator.java\>

> _(작성란)_

---

## InLab #3: Iterator 패턴

### 3-1.

BookShelf 클래스는 Book 의 집합이다. BookSelf 에 있는 책을 차례대로 접근하여 인쇄하는 메인 프로그램이 수행되도록 Iterator 패턴을 적용하여 설계하고 코딩하라.

**(1) Iterator 패턴이 적용되어 설계되어야 할 예제의 구성요소는 다음과 같다.**

- Book – 책을 나타내는 클래스로 책명(name)이 필요함.
- BookShelf – 책을 모아 놓은 책장을 나타내는 클래스로 책을 추가하고, 어느 위치의 책을 찾고, 전체를 접근하고 몇권인지 알아내는 오퍼레이션이 필요함.
- BookShelfIterator – 책장을 검색하는 데 사용되는 반복자
- Iterator\<E\> - 일반적인 반복자가 가지는 오퍼레이션을 정의한 인터페이스
- Iterable\<E\> - 집합체를 나타내는 인터페이스로 iterator 를 만들 때 필요한 오퍼레이션이 정의됨.

클래스 다이어그램을 그려 설계하라.

> _(작성란)_

---

**(2) 다음 main.java 프로그램이 실행되도록 패턴을 적용하여 설계한 각 구성요소를 코딩하라.**

```java
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        BookShelf bookShelf = new BookShelf(4);
        bookShelf.appendBook(new Book("Around the World in 80 Days"));
        bookShelf.appendBook(new Book("Bible"));
        bookShelf.appendBook(new Book("Cinderella"));
        bookShelf.appendBook(new Book("Daddy-Long-Legs"));

        // 명시적으로 iterator를 사용하는 형태
        Iterator<Book> it = bookShelf.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            System.out.println(book.getName());
        }
        System.out.println();

        // 확장된 for 문장을 사용할 수도 있음
        for (Book book: bookShelf) {
            System.out.println(book.getName());
        }
        System.out.println();
    }
}
```

\<Book.java\>

> _(작성란)_

---

\<BookShelf.java\>

> _(작성란)_

---

\<BookShelfIterator.java\>

> _(작성란)_

---

## PostLab #12: 회고 보고서

Lab #12 의 주제는 오퍼레이션에 관련된 패턴이었습니다. Command, Decorator, Iterator 패턴을 알고 코딩으로 체험하기 위한 목적이 강의와 액티비티를 통하여 얼마나 잘 이해되었는지 또한 무엇을 배웠는지 자신의 언어로 정리해 봅시다.

> _(작성란)_
