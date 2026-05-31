---
type: deep_study
pattern: Facade
category: 인터페이스/구조 패턴 (Structural)
course: 객체지향설계와패턴
week: 6 (인터페이스 패턴)
created: 2026-05-31
related: ["[[adapter_pattern]]", "[[factory_method_pattern]]"]
---

# Facade 패턴

## 한 줄 정의
> 복잡한 서브시스템 여러 개 앞에 **단순한 창구 하나**를 세우는 패턴.
> **목적 = 단순화** (복잡함을 가리고 쉬운 길 하나로). 여러 개 → 하나.

## 출발점: 문제

홈시어터로 영화 한 편 보려면 매번 순서 맞춰 8줄을 쳐야 함:

```java
projector.on();
projector.setInput(dvdPlayer);
screen.down();
lights.dim(10);
amplifier.on();
amplifier.setVolume(5);
dvdPlayer.on();
dvdPlayer.play("인터스텔라");
```

끄려면 또 역순으로 8줄. → 묶어버리고 싶다, 함수 하나로.

## 해결: 부품들을 품은 창구 클래스

```java
class HomeTheaterFacade {
    private Projector projector;
    private Screen screen;
    private Lights lights;
    private Amplifier amplifier;
    private DvdPlayer dvdPlayer;
    // 생성자에서 부품들 다 받아둠

    public void watchMovie(String movie) {   // 8줄을 이 안에 묶음
        projector.on();
        projector.setInput(dvdPlayer);
        screen.down();
        lights.dim(10);
        amplifier.on();
        amplifier.setVolume(5);
        dvdPlayer.on();
        dvdPlayer.play(movie);
    }
    public void endMovie() {   // 끄는 역순도 묶음
        dvdPlayer.off(); amplifier.off(); lights.on();
        screen.up(); projector.off();
    }
}
```

```java
theater.watchMovie("인터스텔라");   // 8줄이 1줄로
theater.endMovie();
```

> Facade = 프랑스어 "정면/외관." 복잡한 내부(배선·배관)는 가리고 사람 드나드는 **정문 하나**만 보여줌.

여기서도 **has-a + 위임**: Facade가 부품들을 필드로 품고, `watchMovie()` 요청을 부품들에게 나눠 시킴. (cf. [[factory_method_pattern]], [[adapter_pattern]] — 같은 뼈대)

## Adapter와의 대조 (헷갈리는 짝, 목적 정반대)

구조는 둘 다 "품고 위임"이라 비슷. 하지만 **왜 하느냐**가 정반대:

| | Adapter | Facade |
|---|---|---|
| 목적 | **변환** (A 모양 → B 모양) | **단순화** (복잡함 가리기) |
| 개수 | 안 줄어 (칠면조1 → 어댑터1) | 확 줄어 (호출 8개 → 1개) |
| 한 줄 | "모양을 바꾼다" | "복잡함을 가린다" |

> **Adapter는 모양을 바꾸고(A→B, 개수 그대로), Facade는 복잡함을 가린다(여러 개→하나).**

## 주의 1: "단순화"지 "조율"이 아니다 → Mediator와 구분

`watchMovie()`가 부품을 순서대로 부르니 "흐름을 조율하는 것처럼" 보이지만, 그건 단순화하다 따라온 **부수 효과**지 목적이 아님. Facade의 목적은 어디까지나 **"호출하는 쪽을 편하게, 내부 복잡도 숨기기."**

- "여러 객체의 복잡한 상호작용·흐름 지휘" 자체가 목적인 패턴은 **Mediator(중재자)** — cf. Lab 8 냉각 시스템(버튼-팬-전원).
- Mediator: 객체 간 통신을 중앙에서 지휘하는 게 **목적 그 자체**.
- Facade: 그냥 복잡한 거 가리는 **정문**. (둘 다 흐름을 건드려도 의도가 다름)

## 주의 2: Facade는 아무것도 막지 않는다

Facade를 만들어도 원래 부품들(projector, dvdPlayer...)은 여전히 살아있고 직접 접근 가능. Facade는 **"쉬운 길을 하나 더 열어준" 것**이지 "어려운 길을 막은" 게 아님. 급하면 `projector.on()` 직접 호출 OK.
↔ Adapter는 타입을 안 맞추면 컴파일이 안 되니 "안 거치면 안 되는" 성격. 이것도 결을 가르는 포인트.

## 정리
- **문제**: 복잡한 서브시스템을 쓰려면 매번 여러 호출을 순서 맞춰 해야 함
- **해결**: 부품들을 품은 Facade 클래스가 단순한 메서드 하나로 묶어 제공
- **본질**: 단순화(가리기)지 조율이 아님 / 기존 직접 접근을 막지 않음

## 연결
- [[adapter_pattern]] — 짝꿍, 목적 정반대(변환 vs 단순화)
- [[factory_method_pattern]] — 같은 위임(has-a + 일 넘기기) 뼈대
