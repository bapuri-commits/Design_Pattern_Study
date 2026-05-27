# OO Design and Patterns - Lab 자료 (9)

---

# Laboratory 09 | Exercise Patterns

**Lab 9: 구축 패턴(1)**

성명: ________________

학번: ________________

목차
- InLab #1. Builder 패턴의 적용
- InLab #2. Factory Method 패턴의 적용
- InLab #3. Abstract Factory 패턴의 적용

Version 2.0

---

## InLab #1: Builder 패턴

### 1-1.

다음과 같은 User 클래스를 설계하였다. 생략 가능한 파라미터를 고려하여 생성자를 다음과 같이 작성하였다고 하자.

```java
public class User {
    private final String firstName;    //required
    private final String lastName;     //required
    private final int age;             //optional
    private final String phone;        //optional
    private final String address;      //optional

    public User(String firstName, String lastName) {
        this(firstName, lastName, 0);
    }

    public User(String firstName, String lastName, int age) {
        this(firstName, lastName, age, "");
    }

    public User(String firstName, String lastName, int age, String phone) {
        this(firstName, lastName, age, phone, "");
    }

    public User(String firstName, String lastName, int age, String phone, String address)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }
    ...
}
```

**(1) 위와 같은 방식의 생성자 구현의 문제점은 무엇인가?**

> _(작성란)_

---

**(2) Java는 생성자를 정의하지 않으면 디폴트 생성자를 제공한다. 다음과 같이 제공되는 생성자를 이용한다면 문제점은 무엇인가? 두 가지 이상 들어라.**

```java
public class User {
    private String firstName; // required
    private String lastName; // required
    private int age; // optional
    private String phone; // optional
    private String address;  //optional

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
```

> _(작성란)_

---

**(3) 빌더 패턴을 이용하여 First Name, Last Name 만 있더라도 객체가 생성될 수 있도록 User 클래스를 수정하라.**

\<User.java\>

> _(작성란)_

---

\<Main.java\>

> _(작성란)_

---

결과

```
Kunwoo Kim
null
seoul
0
010

XXX YYY
null
pusan
0
011
```

---

## InLab #2: Factory Method 패턴

### 2-1. 다음은 모터를 구동하여 운영하는 엘리베이터를 간략화한 코드이다.

```java
public enum Direction {UP, DOWN}
public enum MotorStatus {MOVING, STOPPING}
public abstract class Motor {
    private MotorStatus motorStatus;

    public Motor() {
        motorStatus = MotorStatus.STOPPED;
    }
    public MotorStatus getMotorStatus() {
        return motorStatus;
    }
    private void setMotorStatus(MotorStatus motorStatus) {
        this.motorStatus = motorStatus;
    }
    private void move(Direction direction) {
        MotorStatus motorStatus = getMotorStatus();
        if (motorStatus == MotorStatus.MOVING)
            return;
        moveMotor(direction);
        setMotorStatus(MotorStatus.MOVING);
    }
}

public class LGMotor extends Motor {
    protected void moveMotor(Direction direction) {
        System.out.println("move LG Motor " + direction);
    }
}
public class HyundaiMotor extends Motor {
    protected void moveMotor(Direction direction) {
        System.out.println("move Hyundai Motor " + direction);
    }
}

public class ElevatorController {
    private int id;
    private int curFloor = 1;
    private Motor motor;

    public ElevatorController(int id, Motor motor) {
        this.id = id;
        this.motor = motor;
    }
    public void gotoFloor(int destination) {
        if (destination = curFloor)
            return;

        Direction direction;

        if (destination > curFloor)
            direction = Direction.UP;
        else
            direction = Direction.DOWN;

        motor.move(direction);

        System.out.print("Elevator [" + id + "] floor:" + curFloor);
        curFloor = destination;
        System.out.println(" ==> " + curFloor + " with " + motor.getClass().getName());

        motor.stop();
    }
}
```

**(1) 다음 클라이언트 프로그램의 실행 결과는 무엇인가? 이런 설계에 어떤 문제점이 있는가?**

```java
public class Client {
    public static void maint(String[] args) {
        Motor lgMotor = new LGMotor();
        ElevatorController controller1 = new ElevatorController(1, lgMotor);
        controller1.gotoFloor(5);
        controller1.gotoFloor(3);

        Motor hyundaiMotor = new HyundaiMotor();
        ElevatorController controller2 = new ElevatorController(1, hyundaiMotor);
        controller2.gotoFloor(4);
        controller2.gotoFloor(6);
    }
}
```

> _(작성란)_

---

**(2) 위 프로그램을 팩토리 패턴을 적용하여 설계를 개선하고 UML 클래스 다이어그램으로 표현하라.**

> _(작성란)_

---

**(3) LGMotor 와 HyundaiMotor 객체를 생성하는 MotorFactory 클래스를 구현하라.**

\<ElevatorController.java\>

> _(작성란)_

---

\<Motor.java\>

> _(작성란)_

---

\<HyundaiMotor.java / LGMotor.java\>

> _(작성란)_

---

\<MotorFactory.java\>

> _(작성란)_

---

\<Client.java\>

> _(작성란)_

---

## InLab #3: Abstract Factory 패턴

### 3-1.

차량(Vehicle) 객체를 생성하는 프로그램을 작성하고 있다. 차량의 유형(예: SUV, 세단, 쿠페, 컨버터블, 해치백, 트럭 등)과 에너지 유형(예: 가스, 하이브리드 및 전기)을 모두 고려해야 한다. 이 문제에서는 간단히 코딩하기 위해 세단과 SUV 의 두 가지 차체 유형으로만 작업하도록 하자.

추상 팩토리 패턴을 이용하여 구현된 Vehicle 객체의 패밀리를 사용하는 다음 메인 프로그램이 작동하도록 모든 요소를 설계하고 코딩하라.

```java
public static void main(String[] args) {
    Vehicle hybridSedan = VehicleFactory.CreateInstance(
            CarBodyType.Sedan,
            EnergyType.Hybrid);
    System.out.println(hybridSedan);
      hybridSedan.fillUp();
      hybridSedan.greetDriver();
      hybridSedan.drive();
    Vehicle gasSUV = VehicleFactory.CreateInstance(
            CarBodyType.SUV,
            EnergyType.Gas);
    System.out.println(gasSUV);
      gasSUV.fillUp();
      gasSUV.greetDriver();
      gasSUV.drive();
    Vehicle electricSedan = VehicleFactory.CreateInstance(
            CarBodyType.Sedan,
            EnergyType.Electric);
    System.out.println(electricSedan);
      electricSedan.fillUp();
      electricSedan.greetDriver();
      electricSedan.drive();
}
```

위 코드가 수행된 후에는 다음과 같은 결과가 인쇄되어야 한다.

```
EnergyType=Hybrid BodyType=Sedan
your hybrid Sedan is fueling or charging...
Greeting from your hybrid Sedan
Driving an hybrid Sedan
EnergyType=Gas BodyType=SUV
your gas SUV is fueling...
Greeting from your gas SUV
Driving a gas SUV
EnergyType=Electric BodyType=Sedan
your electric Sedan is Charging...
Greeting from your electric Sedan
Driving an electric Sedan
```

**(1) 추상 팩토리 패턴이 사용된 설계를 UML 클래스 다이어그램으로 작성하라.**

> _(작성란)_

---

**(2) 위 설계를 코딩하라.**

> _(작성란)_

---

## PostLab #9: 회고 보고서

Activity #9 의 주제는 객체를 구축하는 패턴이었습니다. 빌더, 팩토리 메소드, 추상 팩토리 패턴을 알고 코딩으로 체험하기 위한 목적이 강의와 액티비티를 통하여 얼마나 잘 이해되었는지 또한 무엇을 배웠는지 자신의 언어로 정리해 봅시다.

> _(작성란)_
