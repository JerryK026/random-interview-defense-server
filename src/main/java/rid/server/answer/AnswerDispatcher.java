package rid.server.answer;

import org.springframework.stereotype.Component;
import rid.server.quiz.Quiz;
import rid.server.quiz.QuizType;

import java.util.HashMap;
import java.util.Map;

@Component
public class AnswerDispatcher {

    private final Map<QuizType, Answers> answersMap = new HashMap<>();

    AnswerDispatcher() {
        loadAnswers();
    }

    public Answer answer(Quiz quiz) {
        return answersMap.get(quiz.quizType())
                .answer(quiz.id());
    }

    private void loadAnswers() {
        for (QuizType type : QuizType.values()) {
            /**
             * Todo: null precondition validation은 객체지향적이지 않으므로 리팩터링 대상임
             */
            if (type.getAnswerPath() == null) return;

            answersMap.put(type, new Answers(type));
        }
    }
}
