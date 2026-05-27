# OO Design and Patterns - Lab 3: SOLID 원칙

---

## 개요

Lab 3는 SOLID 원칙 5가지를 각각 코드 예제로 학습하는 워크북이다.
각 InLab은 **"위반 찾기(x.1)"** + **"수정하기(x.2)"** 쌍으로 구성된다.

| InLab | 원칙 | 핵심 질문 |
|-------|------|-----------|
| InLab #1 | SRP (단일 책임 원칙) | 이 클래스는 한 가지 이유로만 변경되는가? |
| InLab #2 | OCP (개방-폐쇄 원칙) | 확장에는 열려 있고, 수정에는 닫혀 있는가? |
| InLab #3 | LSP (리스코프 치환 원칙) | 자식 클래스가 부모 타입을 완전히 대체할 수 있는가? |
| InLab #4 | ISP (인터페이스 분리 원칙) | 클라이언트가 사용하지 않는 메소드에 의존하지 않는가? |
| InLab #5 | DIP (의존성 역전 원칙) | 고수준 모듈이 저수준 모듈의 구체 클래스에 직접 의존하지 않는가? |

---

## InLab #1: SRP — Class Person

### 1.1 위반 찾기

주어진 코드:

```csharp
public class Person {
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public DateTime DateOfBirth { get; set; }
    public char Gender { get; set; }

    public object Export(string format, string? location = null) {
        switch(format) {
            case "Excel":
                // excel specific rendering and saving to location here
                excel.Save(location);
                break;
            case "JSON":
                // json specific rendering and then returning that data as a result
                return jsonString;
            default:
                // default behaviour here, saving it in a text file
                File.WriteAllText(location.Value, $"{FirstName} {LastName}, born on {DateOfBirth.ToShortDateString()}");
        }
    }
}
```

**위반 원칙:** SRP (Single Responsibility Principle)

**위반 이유:**
- `Person` 클래스가 **데이터 보유**와 **내보내기 로직** 두 가지 책임을 가지고 있다.
- Excel/JSON/텍스트 저장 로직이 한 클래스 안에 혼재한다.
- 새로운 저장 형식이 추가되거나, 저장 방식이 바뀌면 `Person` 클래스를 수정해야 한다.
- 즉, `Person`이 변경되어야 할 이유가 두 가지 이상 존재한다.

### 1.2 수정 방향

`Person`은 데이터만 가지고, 내보내기 책임은 별도 클래스로 분리한다.

```csharp
// Person: 데이터만 책임진다
public class Person {
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public DateTime DateOfBirth { get; set; }
    public char Gender { get; set; }
}

// 내보내기 책임을 별도 클래스로 분리
public class PersonExporter {
    public object Export(Person person, string format, string? location = null) {
        switch(format) {
            case "Excel":
                // excel specific rendering and saving to location here
                excel.Save(location);
                break;
            case "JSON":
                return jsonString;
            default:
                File.WriteAllText(location.Value,
                    $"{person.FirstName} {person.LastName}, born on {person.DateOfBirth.ToShortDateString()}");
        }
    }
}
```

**핵심:** `Person`은 이제 데이터 구조 변경 시에만 수정된다. 저장 로직이 바뀌어도 `Person`은 건드리지 않아도 된다.

---

## InLab #2: OCP — PersonExporter

### 2.1 위반 찾기

주어진 코드:

```csharp
public class Person {
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public DateTime DateOfBirth { get; set; }
    public char Gender { get; set; }
}

public class PersonExporter {
    public object Export(string format, string? location = null) {
        switch(format) {
            case "Excel":
                // excel specific rendering and saving to location here
                excel.Save(location);
                break;
            case "JSON":
                // json specific rendering and then returning that data
                return jsonString;
            default:
                // default behaviour here, saving it in a text file
                File.WriteAllText(location.Value, $"{FirstName} {LastName}, born on {DateOfBirth.ToShortDateString()}");
        }
    }
}
```

**위반 원칙:** OCP (Open-Closed Principle)

**위반 이유:**
- 새로운 형식(예: XML, CSV)을 추가하려면 `PersonExporter`의 `switch` 문을 수정해야 한다.
- 즉, **확장을 위해 기존 코드를 수정**해야 한다 → OCP 위반.
- 이상적으로는 기존 코드를 건드리지 않고 새 형식을 추가할 수 있어야 한다.

### 2.2 수정 방향

인터페이스를 도입하고, 형식별로 구현체를 분리한다.

```csharp
// 추상화: 내보내기 인터페이스
public interface IPersonExporter {
    object Export(Person person, string? location = null);
}

// 각 형식별 구현체 (새 형식 추가 시 기존 코드 수정 불필요)
public class ExcelPersonExporter : IPersonExporter {
    public object Export(Person person, string? location = null) {
        // excel specific rendering and saving to location here
        excel.Save(location);
        return null;
    }
}

public class JsonPersonExporter : IPersonExporter {
    public object Export(Person person, string? location = null) {
        // json specific rendering
        return jsonString;
    }
}

public class TextPersonExporter : IPersonExporter {
    public object Export(Person person, string? location = null) {
        File.WriteAllText(location.Value,
            $"{person.FirstName} {person.LastName}, born on {person.DateOfBirth.ToShortDateString()}");
        return null;
    }
}
```

**핵심:** 새 형식이 필요하면 새 클래스를 추가하기만 하면 된다. 기존 클래스는 수정하지 않는다.

---

## InLab #3: LSP — IPersonFormatter

### 3.1 위반 찾기

주어진 코드:

```csharp
IPersonFormatter {
    Stream Format(Person person);
}

public class XMLPersonFormatter : IPersonFormatter {
    public string Format(Person person) {
        // ignoring location and executing xml formatting logic
        // saving it to a MemoryStream when working in .NET for example
        return xmlStringMemoryStream;
    }
}

public class ExcelPersonFormatter : IPersonFormatter {
    public object Format(Person person) {
        // excel specific rendering and saving to location here
        excel.Save(location);
        // we now don't return null, but a MemoryStream we don't have anything to return...
        return excelWorkBookMemoryStream;
    }
}
```

**위반 원칙:** LSP (Liskov Substitution Principle)

**위반 이유:**
- `IPersonFormatter.Format()`은 `Stream`을 반환하도록 정의되어 있다.
- `XMLPersonFormatter.Format()`은 `string`을 반환한다 → 반환 타입 불일치.
- `ExcelPersonFormatter.Format()`은 `object`를 반환하고 내부에서 저장까지 수행한다 → 부수효과(side effect) 추가.
- `IPersonFormatter` 타입으로 교체했을 때 동작이 달라지므로 LSP 위반.
- **LSP의 핵심:** 자식 클래스는 부모 타입으로 대체되어도 프로그램이 올바르게 동작해야 한다.

### 3.2 수정 방향

모든 구현체가 동일한 반환 타입(`Stream`)을 일관되게 반환하도록 수정한다.

```csharp
public interface IPersonFormatter {
    Stream Format(Person person);
}

public class XMLPersonFormatter : IPersonFormatter {
    public Stream Format(Person person) {
        // xml formatting logic
        var memoryStream = new MemoryStream();
        // ... write xml to stream ...
        return memoryStream;  // Stream 반환
    }
}

public class ExcelPersonFormatter : IPersonFormatter {
    public Stream Format(Person person) {
        // excel formatting logic
        var memoryStream = new MemoryStream();
        // ... write excel workbook to stream ...
        return memoryStream;  // 동일하게 Stream 반환, 저장은 호출자가 담당
    }
}
```

**핵심:** 저장(Save)은 `Format()`의 책임이 아니다. `Format()`은 Stream을 반환하고, 저장은 호출자가 처리한다.

---

## InLab #4: ISP — IPersonRepository

### 4.1 위반 찾기

주어진 코드:

```csharp
public interface IPersonRepository {
    void Save(Person person);
    void Save(IEnumerable<Person> people);
    Person Get(int id);
    IEnumerable<Person> Get();
    IEnumerable<Person> Get(Func<Person, bool> predicate);
}

public class PersonReadOnlyRepository : IPersonRepository {
    public void Save(Person person) {
        throw NotImplementedException();  // 사용하지 않는 메소드
    }
    public void Save(IEnumerable<Person> people) {
        throw NotImplementedException();  // 사용하지 않는 메소드
    }
    public Person Get(int id) {
        throw NotImplementedException();  // 사용하지 않는 메소드
    }
    public IEnumerable<Person> Get() {
        using(var db = new DbContext()) {
            return db.People.ToEnumerable();
        }
    }
    public IEnumerable<Person> Get(Func<Person, bool> predicate) {
        using(var db = new DbContext()) {
            return db.People.Where(p => predicate(p)).ToEnumerable();
        }
    }
}
```

**위반 원칙:** ISP (Interface Segregation Principle)

**위반 이유:**
- `PersonReadOnlyRepository`는 읽기 전용인데, `IPersonRepository`가 강제하는 `Save()` 메소드들을 구현해야 한다.
- 결과적으로 `NotImplementedException`으로 막는 메소드가 생긴다.
- 클라이언트가 사용하지 않는 메소드에 의존하게 된다 → ISP 위반.
- **ISP의 핵심:** 인터페이스는 클라이언트가 실제로 사용하는 메소드만 포함해야 한다.

### 4.2 수정 방향

읽기와 쓰기 인터페이스를 분리한다.

```csharp
// 읽기 전용 인터페이스
public interface IReadablePersonRepository {
    Person Get(int id);
    IEnumerable<Person> Get();
    IEnumerable<Person> Get(Func<Person, bool> predicate);
}

// 쓰기 전용 인터페이스
public interface IWriteablePersonRepository {
    void Save(Person person);
    void Save(IEnumerable<Person> people);
}

// 읽기만 필요한 구현체 → 읽기 인터페이스만 구현
public class PersonReadOnlyRepository : IReadablePersonRepository {
    public Person Get(int id) {
        using(var db = new DbContext()) {
            return db.People.GetById(id);
        }
    }
    public IEnumerable<Person> Get() {
        using(var db = new DbContext()) {
            return db.People.ToEnumerable();
        }
    }
    public IEnumerable<Person> Get(Func<Person, bool> predicate) {
        using(var db = new DbContext()) {
            return db.People.Where(p => predicate(p)).ToEnumerable();
        }
    }
}

// 읽기+쓰기 모두 필요한 구현체 → 두 인터페이스 모두 구현
public class PersonRepository : IReadablePersonRepository, IWriteablePersonRepository {
    // ... 모든 메소드 구현 ...
}
```

**핵심:** 클라이언트는 자신이 필요한 인터페이스만 의존하면 된다. `NotImplementedException`이 사라진다.

---

## InLab #5: DIP — PersonRepository

### 5.1 위반 찾기

주어진 코드:

```csharp
public class PersonRepository : IReadablePersonRepository, IWriteablePersonRepository {
    public void Save(Person person) {
        using(var db = new DbContext()) {  // 매번 직접 생성
            return db.People.Add(person);
        }
    }
    public void Save(IEnumerable<Person> people) {
        using(var db = new DbContext()) {  // 매번 직접 생성
            return db.People.AddRange(people);
        }
    }
    public Person Get(int id) {
        using(var db = new DbContext()) {  // 매번 직접 생성
            return db.People.GetById(id);
        }
    }
    public IEnumerable<Person> Get() {
        using(var db = new DbContext()) {  // 매번 직접 생성
            return db.People.ToEnumerable();
        }
    }
    public IEnumerable<Person> Get(Func<Person, bool> predicate) {
        using(var db = new DbContext()) {  // 매번 직접 생성
            return db.People.Where(p => predicate(p)).ToEnumerable();
        }
    }
}
```

**위반 원칙:** DIP (Dependency Inversion Principle)

**위반 이유:**
- `PersonRepository`가 `DbContext`라는 구체 클래스를 매 메소드에서 직접 `new`로 생성한다.
- 고수준 모듈(`PersonRepository`)이 저수준 모듈(`DbContext`)의 구체 구현에 직접 의존한다.
- `DbContext`를 다른 DB로 교체하거나 테스트 시 가짜 DB를 주입하는 것이 불가능하다.
- **DIP의 핵심:** 고수준 모듈과 저수준 모듈 모두 추상화(인터페이스)에 의존해야 한다.

### 5.2 수정 방향

`DbContext`를 생성자 주입(Constructor Injection)으로 받도록 수정한다.

```csharp
// DbContext 추상화 (인터페이스)
public interface IDbContext {
    IQueryable<Person> People { get; }
    void SaveChanges();
}

// PersonRepository는 추상화에 의존
public class PersonRepository : IReadablePersonRepository, IWriteablePersonRepository {
    private readonly IDbContext _db;  // 추상화에 의존

    // 생성자 주입: 외부에서 구체 구현을 주입받는다
    public PersonRepository(IDbContext db) {
        _db = db;
    }

    public void Save(Person person) {
        _db.People.Add(person);
        _db.SaveChanges();
    }
    public Person Get(int id) {
        return _db.People.GetById(id);
    }
    public IEnumerable<Person> Get() {
        return _db.People.ToEnumerable();
    }
    public IEnumerable<Person> Get(Func<Person, bool> predicate) {
        return _db.People.Where(p => predicate(p)).ToEnumerable();
    }
}
```

**핵심:** 이제 실제 환경에서는 `new PersonRepository(new RealDbContext())`로, 테스트 시에는 `new PersonRepository(new FakeDbContext())`로 주입하면 된다. `PersonRepository` 코드는 전혀 수정하지 않아도 된다.

---

## SOLID 원칙 요약

| 원칙 | 한 줄 정의 | 위반 신호 | 해결 패턴 |
|------|-----------|-----------|-----------|
| **SRP** | 클래스는 하나의 책임만 | 변경 이유가 두 가지 이상 | 책임별 클래스 분리 |
| **OCP** | 확장엔 열려있고 수정엔 닫혀있어야 | 기능 추가 시 기존 코드 수정 | 인터페이스 + 구현체 분리 |
| **LSP** | 자식은 부모를 완전히 대체 가능해야 | 자식이 `throw`하거나 반환타입이 다름 | 계약(contract)을 일관되게 유지 |
| **ISP** | 클라이언트는 필요한 인터페이스만 의존 | `NotImplementedException` 난무 | 인터페이스를 역할별로 분리 |
| **DIP** | 구체 클래스가 아닌 추상화에 의존 | `new 구체클래스()` 직접 생성 | 생성자 주입 (DI) |
