# OO Design and Patterns - Lab 자료 (11)

---

# Laboratory 11 | Exercise Patterns

**Lab 11: 오퍼레이션 패턴(1)**

성명: ________________

학번: ________________

목차
- InLab #1. Template Method 패턴의 적용
- InLab #2. State 패턴의 적용
- InLab #3. Strategy 패턴의 적용

Version 2.0

---

## InLab #1: Template Method 패턴

### 1-1.

커피숍에 다음과 같은 두 가지 음료(커피, 차)에 대한 코드가 있다.

```java
public class Coffee {
    void prepareRecipe () {
        boilWater();
        brewCoffeeGrinds();
        pourInCup();
        addSugarAndMilk();
    }
    public void boilWater() {
        System.out.println ("Boiling Water")
    }
    public void brewCoffeeGrinds() {
        System.out.println ("Dripping coffee though filter")
    }
    public void pourInCup() {
        System.out.println ("Pouring into Cup")
    }
    public void addSugarAndMilk() {
        System.out.println ("Adding Sugar and Milk")
    }
}

public class Tea {
    void prepareRecipe () {
        boilWater();
        steepTeaBag();
        pourInCup();
        addLemon();
    }
    public void boilWater() {
        System.out.println ("Boiling Water")
    }
    public void steepTeaBag() {
        System.out.println ("Steeping the Tea")
    }
    public void pourInCup() {
        System.out.println ("Pouring into Cup")
    }
    public void addLemon() {
        System.out.println ("Adding Lemon")
    }
}
```

**(1) coffee 와 tea 의 prepareRecipe()의 과정 중 boilWater() and pourInCup() 함수는 똑같다. 위 코드를 템플릿 메소드 패턴(공통 클래스를 CaffeineBeberage 로 할 것)을 이용하여 설계한 것을 클래스 다이어그램으로 그려라.**

> _(작성란)_

---

**(2) 위의 설계를 코딩하라. 커피와 티 클래스의 공통된 recipe 는 다음과 같다.**

```
Boil some water
Use hot water to extract Coffee or Tea
Pour coffee in cup
Add the appropriate condiments to the beverage
```

> _(작성란)_

---

## InLab #2: State 패턴

### 2-1.

다음은 출입구에 설치된 자동문(Door1)을 시뮬레이션한 프로그램이다. 코드를 읽고 물음에 답하라.

```java
public class Door_1 extends Observable
{
    public static final int CLOSED = -1;
    public static final int OPENING = -2;
    public static final int OPEN = -3;
    public static final int CLOSING = -4;
    public static final int STAYOPEN = -5;
    private int state = CLOSED;
    //

    public String status()
    {
        switch (state){
            case OPENING :
                return "Opening";
            case OPEN :
                return "Open";
            case CLOSING :
                return "Closing";
            case STAYOPEN :
                return "StayOpen";
            default :
                return "Closed";
        }
    }
    public void click()
    {
        if (state == CLOSED)
        {
            setState(OPENING);
        }
        else if (state == OPENING || state == STAYOPEN)
        {
            setState(CLOSING);
        }
        else if (state == OPEN)
        {
            setState(STAYOPEN);
        }
        else if (state == CLOSING)
        {
            setState(OPENING);
        }
    }

    private void setState(int state)
    {
        this.state = state;
        setChanged();
        notifyObservers();
    }

    public void complete()
    {
        if (state == OPENING)
        {
            setState(OPEN);
        }
        else if (state == CLOSING)
        {
            setState(CLOSED);
        }
    }
    public void timeout()
    {
        setState(CLOSING);
    }
}
```

**(1) 위 프로그램에서 Door1 의 동작을 UML 상태 다이어그램으로 그려라.**

> _(작성란)_

---

**(2) 위 프로그램의 동작은 같고 상태 패턴을 이용한 Door2 클래스를 설계하고 코딩하라. (먼저 클래스 다이어그램을 그린 후 코드를 작성할 것)**

> _(작성란)_

---

## InLab #3: Strategy 패턴

### 3-1.

다음은 컴퓨터 게임 제작을 위한 클래스 요소들이다. 게임의 King, Queen 등 각 캐릭터들은 칼, 창, 활 등 여러 종류의 무기를 사용한다. 각 무기들의 사용 동작은 다 다르게 제작되어야 한다.

```
+------------------------+    +-------------------------+    +-----------------------------+
|       Character        |    |     KnifeBehavior       |    |    BowAndArrowBehavior      |
+------------------------+    +-------------------------+    +-----------------------------+
| WeaponBehavior weapon; |    | useWeapon()             |    | useWeapon()                 |
|                        |    | //implements cutting    |    | //implements fight with     |
| fight();               |    | // with a knife         |    | // bow and arrows           |
+------------------------+    +-------------------------+    +-----------------------------+

+------------------------+    +-------------------------+    +-----------------------------+
|      AxeBehavior       |    |    <<interface>>        |    |           Queen             |
+------------------------+    |    WeaponBehavior       |    +-----------------------------+
| useWeapon()            |    +-------------------------+    | fight()                     |
| //implements fight     |    | useWeapon()             |    +-----------------------------+
| // with an axe         |    +-------------------------+
+------------------------+

+------------+    +------------+    +------------+    +-----------------------------+
|    King    |    |   Knight   |    |   Bishop   |    |        SpearBehavior        |
+------------+    +------------+    +------------+    +-----------------------------+
| fight()    |    | fight()    |    | fight()    |    | useWeapon()                 |
+------------+    +------------+    +------------+    | //implements fight          |
                                                      | // with a spear             |
                                                      +-----------------------------+
```

**(1) 위 요소들을 이용하여 각 캐릭터들이 다양한 무기를 지정하여 사용할 수 있도록 Strategy 패턴을 이용하여 설계하라. 클래스 다이어그램으로 그릴 것.**

> _(작성란)_

---

## PostLab #11: 회고 보고서

Lab #11 의 주제는 오퍼레이션에 대한 패턴이었습니다. 템플릿 메소드, 상태, 스트래티지 패턴을 알고 코딩으로 체험하기 위한 목적이 강의와 액티비티를 통하여 얼마나 잘 이해되었는지 또한 무엇을 배웠는지 자신의 언어로 정리해 봅시다.

> _(작성란)_
