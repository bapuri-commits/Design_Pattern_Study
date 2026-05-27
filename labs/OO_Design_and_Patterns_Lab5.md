# OO Design and Patterns - Lab 자료 (5)

---

# Laboratory 05 | Exercise Patterns

**Lab 5: Interface Patterns(1)**

성명: ________________

학번: ________________

목차
- InLab #1. Adapter 패턴의 적용
- InLab #2. Façade 패턴의 적용

Version 2.0

---

## InLab #1: Adapter 패턴

### 1-1. 상속을 이용한 Adapter 패턴의 구현

큰 현수막을 인쇄하는 것과 같은 Banner 클래스가 있다. 클래스 안에는 주어진 문자열을 괄호로 묶어 인쇄하는 showWithParen 메소드와 문자열 양옆에 *를 붙여서 인쇄하는 showWithAster 메소드가 준비되어 있다.

```java
public class Banner {
    private String string;

    public Banner(String string) {
        this.string = string;
    }

    public void showWithParen() {
        System.out.println("(" + string + ")");
    }

    public void showWithAster() {
        System.out.println("*" + string + "*");
    }
}
```

주어진 Banner 를 이용하는 프로그램에서는 다른 인터페이스 형식으로 부르고 싶다. 즉 Print 인터페이스를 사용하되 문자열을 괄호로 묶어서 약하게 표시하는 메소드 printWeak 와 문자열을 *로 강조하여 표시하는 메소드 printStrong 으로 호출하고 싶다.

**(1)** 상속을 사용하여 Adapter 클래스를 설계하되 그 이름을 PrintBanner 클래스로 하라. 또한 Banner 클래스와 Print 인터페이스와의 관계를 UML 클래스 다이어그램으로 표시하라.

> _(작성란)_

---

**(2)** 다음과 같은 main 프로그램이 PrintBanner 어댑터를 이용하여 Hello 문자열을 인쇄하려고 한다. PrintBanner 어댑터를 Java 언어로 구현하라.

```java
public class Main {
    public static void main(String[] args) {
        Print p = new PrintBanner("Hello");
        p.printWeak();
        p.printStrong();
    }
}
```

> _(작성란)_

---

**(3)** PrintBanner 어댑터를 위임을 이용하여 구현한다면 어떻게 바뀌어야 하는지 클래스 다이어그램으로 설계하고 코딩하라.

> _(작성란)_

---

### 1-2. 다음 코드에는 사용하기 복잡한 JList 를 쉽게 쓰기 위한 의도가 숨어 있다. 의도가 무엇이며 여기에 사용된 패턴과 구성요소들을 파악하여 설명하라.

```java
public List(int rows) {
    public void add(String item) ;
    public void clear() ;
    public void remove(int position) ;
    public String[] getSelectedItems() ;
}
public interface awtList {
    public void add(String s);
    public void remove(String s);
    public String[] getSelectedItems();
    public void clear();
}
public class JawtList extends JScrollPane
implements ListSelectionListener, awtList {
    private JList listWindow;
    private JListData listContents;
    public JawtList(int rows) {
        listContents = new JListData();
        listWindow = new JList(listContents);
        listWindow.setPrototypeCellValue("Abcdefg Hijkmnop");
        getViewport().add(listWindow);
    }
//------------------------------------------
    public void add(String s) {
        listContents.addElement(s);
    }
//------------------------------------------
    public void remove(String s) {
        listContents.removeElement(s);
    }
//------------------
    public void clear() {
    listContents.clear();
    }

main() {
    kidList = new JawtList(20);
    //===
    private void loadList(Vector v) {
        kidList.clear();
        Iterator iter = v.iterator();
        while(iter.hasNext()){
            Swimmer sw = (Swimmer) iter.next();
            kidList.add(sw.getName());
        }
    }
}
```

> _(작성란)_

---

## InLab #2: Facade 패턴

### 2-1. 다음은 이메일 주소 파일에서 이름을 골라내는 클래스(Database), HTML 파일을 작성하는 클래스(HtmlWriter)이다. 이를 이용하여 다음과 같은 포맷의 웹 페이지를 브라우저에 보이게 하는 PageMaker 퍼써드 클래스를 코딩하라.

```
Hong Gil Dong's web page

Welcome to Hong Gil Dong's web page!

Noce to meet you!

Hong Gil Dong
```

**\<HtmlWriter.java\>**

```java
package pagemaker;

import java.io.Writer;
import java.io.IOException;

public class HtmlWriter {
    private Writer writer;

    public HtmlWriter(Writer writer) {
        this.writer = writer;
    }

    // 타이틀 출력
    public void title(String title) throws IOException {
        writer.write("<!DOCTYPE html>");
        writer.write("<html>");
        writer.write("<head>");
        writer.write("<title>" + title + "</title>");
        writer.write("</head>");
        writer.write("<body>");
        writer.write("\n");
        writer.write("<h1>" + title + "</h1>");
        writer.write("\n");
    }

    // 단락 출력
    public void paragraph(String msg) throws IOException {
        writer.write("<p>" + msg + "</p>");
        writer.write("\n");
    }

    // 링크 출력
    public void link(String href, String caption) throws IOException {
        paragraph("<a href=\"" + href + "\">" + caption + "</a>");
    }

    // 이메일 주소 출력
    public void mailto(String mailaddr, String username) throws IOException {
        link("mailto:" + mailaddr, username);
    }

    // HTML 닫기
    public void close() throws IOException {
        writer.write("</body>");
        writer.write("</html>");
        writer.write("\n");
        writer.close();
    }
}
```

**\<Database 클래스\>**

```java
package pagemaker;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Database {
    private Database() {
    }

    // 이름 데이터 파일에서 원하는 값을 찾아 온다
    public static Properties getProperties(String dbname) throws IOException {
        String filename = dbname + ".txt";
        Properties prop = new Properties();
        prop.load(new FileReader(filename));
        return prop;
    }
}
```

**\<maildata.txt\>**

```
gdhong@examples.com = Hong Gil Dong
dongguk@examples.com = Kim Dong Guk
dojun@examples.com = dojun
hanguk@examples.com = Lee Han Guk
```

**\<Main.java\>**

```java
import pagemaker.PageMaker;

public class Main {
    public static void main(String[] args) {
        PageMaker.makeWelcomePage("hyuki@example.com", "welcome.html");
    }
}
```

**\<PageMaker.java\>**

> _(작성란)_

---

## PostLab #5 회고 보고서

Lab #5 의 주제는 인터페이스 패턴이었습니다. 인터페이스와 추상 클래스의 차이를 알고 인터페이스 패턴에 해당하는 어댑터 패턴과 퍼싸드 패턴을 이해하고 적용하기 위한 목적이 강의와 액티비티를 통하여 얼마나 잘 이해되었는지 또한 무엇을 배웠는지 자신의 언어로 정리해 봅시다.

> _(작성란)_
