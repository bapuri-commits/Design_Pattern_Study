# OO Design and Patterns - Lab 자료 (6)

---

# Laboratory 06 | Exercise Patterns

**Lab 6: 인터페이스 패턴(2)**

성명: ________________

학번: ________________

목차
- InLab #1. Composite 패턴의 적용
- InLab #2. Bridge 패턴의 적용

Version 2.0

---

## InLab #1: Composite 패턴

### 1-1. 컴포지트 패턴에서 Leaf 객체와 Composite 객체가 공유하고 있는 Node 가 인터페이스가 아닌 추상 클래스로 표현하는 이유는 무엇인가?

UML 클래스 다이어그램:

```
              Node
           {abstract}
+--------------------------------+
| getName(): String {abstract}   |  0..*
| add(Node) {abstract}           |◄─────┐
| remove(String) {abstract}      |      │
| getChild(int): Node {abstract} |      │
+--------------------------------+      │
          △                            │
          │                            │
   ┌──────┴──────┐                     │
   │             │                     │
 File        Directory                 │
+--------+  +------------------+       │
|getName |  |getName(): String |       │
|(): String  |add(Node)        |◄──────┘ 1
+--------+  |remove(String)   |
            |getChild(int):Node|
            |getSize(): int    |
            +------------------+
```

> _(작성란)_

---

### 1-2. 다음에 제시된 코드 Triangle, Circle 클래스, Drawing 클래스의 구현을 보고 적용된 패턴을 파악하여 클래스 다이어그램을 그리고 각 요소를 설명하라.

**\<main.java\>**

```java
package com.journaldev.design.test;
import com.journaldev.design.composite.Circle;
import com.journaldev.design.composite.Drawing;
import com.journaldev.design.composite.Shape;
import com.journaldev.design.composite.Triangle;

public class TestPattern {
    public static void main(String[] args) {
        Shape tri = new Triangle();
        Shape tri1 = new Triangle();
        Shape cir = new Circle();

        Drawing drawing = new Drawing();
        drawing.add(tri1);
        drawing.add(tri1);
        drawing.add(cir);

        drawing.draw("Red");

        drawing.clear();

        drawing.add(tri);
        drawing.add(cir);
        drawing.draw("Green");
    }
}
```

**\<Drawing.java\>**

```java
package com.journaldev.design.composite;

import java.util.ArrayList;
import java.util.List;

public class Drawing implements Shape{

    //collection of Shapes
    private List<Shape> shapes = new ArrayList<Shape>();

    @Override
    public void draw(String fillColor) {
        for(Shape sh : shapes)
        {
            sh.draw(fillColor);
        }
    }

    //adding shape to drawing
    public void add(Shape s){
        this.shapes.add(s);
    }

    //removing shape from drawing
    public void remove(Shape s){
        shapes.remove(s);
    }

    //removing all the shapes
    public void clear(){
        System.out.println("Clearing all the shapes from drawing");
        this.shapes.clear();
    }
}
```

**\<Shape.java\>**

```java
package com.journaldev.design.composite;

public interface Shape {

    public void draw(String fillColor);
}
```

**\<Triangle.java\>**

```java
package com.journaldev.design.composite;

public class Triangle implements Shape {

    @Override
    public void draw(String fillColor) {
        System.out.println("Drawing Triangle with color "+fillColor);
    }

}
```

**\<Circle.java\>**

```java
package com.journaldev.design.composite;

public class Circle implements Shape {

    @Override
    public void draw(String fillColor) {
        System.out.println("Drawing Circle with color "+fillColor);
    }

}
```

> _(작성란)_

---

### 1-3. OS 의 파일 관리 시스템을 설계한다고 하자. 파일과 디렉토리를 합하여 엔트리로 다루는 프로그램을 설계하고 코딩하라. 디렉토리 엔트리를 생성하고, 이름을 받고(getName), 크기를 알아내고(getSize), 내용물의 목록을 인쇄하는(printList) 기능을 포함시켜야 한다.

**\<UML 클래스 다이어그램\>**

> _(작성란)_

---

### 1-4. Main 프로그램은 아래와 같다. 나머지 필요한 프로그램을 작성하라.

**\<main.java\>**

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Making root entries...");
        Directory rootdir = new Directory("root");
        Directory bindir = new Directory("bin");
        Directory tmpdir = new Directory("tmp");
        Directory usrdir = new Directory("usr");
        rootdir.add(bindir);
        rootdir.add(tmpdir);
        rootdir.add(usrdir);
        bindir.add(new File("vi", 10000));
        bindir.add(new File("latex", 20000));
        rootdir.printList();
        System.out.println();

        System.out.println("Making user entries...");
        Directory emchoi = new Directory("emchoi");
        Directory gildong = new Directory("gildong");
        Directory dongguk = new Directory("dongguk");
        usrdir.add(youngjin);
        usrdir.add(gildong);
        usrdir.add(dojun);
        emchoi.add(new File("diary.html", 100));
        emchoi.add(new File("Composite.java", 200));
        gildong.add(new File("memo.tex", 300));
        dongguk.add(new File("game.doc", 400));
        dongguk.add(new File("junk.mail", 500));
        rootdir.printList();
    }
}
```

> _(작성란)_

---

## InLab #2: Bridge 패턴

### 2-1. 다음과 같은 요구 사항으로 화면에 출력하는 프로그램을 브리지 패턴을 이용하여 만들어라.

- 홀수값 만을 저장하는 EvenStack 과 OddStack 두 가지가 필요하다.
- 구현 방법은 Array 와 Linked 두 가지 방법을 사용한다.
- 스택에는 push, pop, top, IsEmpty, IsFull, printStack 메소드가 있어야 한다.

**(1) 브리지 패턴을 이용하여 설계하라(클래스 다이어그램으로 나타낼 것).**

> _(작성란)_

---

**(2) 브리지 패턴의 각 구성요소를 코딩해 보라.**

> _(작성란)_

---

## PostLab #6: 회고 보고서

Lab #6 의 주제는 인터페이스 패턴(2)이었습니다. 컴포지트, 브리지 패턴의 차이를 알고 코딩으로 체험하기 위한 목적이 강의와 액티비티를 통하여 얼마나 잘 이해되었는지 또한 무엇을 배웠는지 자신의 언어로 정리해 봅시다.

> _(작성란)_
