package rid.server.quiz;

import lombok.AllArgsConstructor;

/**
 * VO 영역이라 Getter를 다 연다
 */
@AllArgsConstructor
public class Quiz {
    private final int id;

    private final QuizType type;
    private final String content;

    /**
     * 객체지향 원칙에 따라 factory 성격의 메서드는 getter가 아닌 명사형으로 명명
     */
    public String content() {
        return content;
    }

    public QuizType quizType() {
        return type;
    }

    public int id() {
        return id;
    }
}


