package rid.server.answer;

import lombok.AllArgsConstructor;
import rid.server.quiz.QuizType;

@AllArgsConstructor
public class Answer {
    private final int id;
    private final QuizType type;
    private final String content;

    public String content() {
        return content;
    }
}
