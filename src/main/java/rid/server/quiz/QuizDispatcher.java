package rid.server.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rid.server.RandomGenerator;

import java.util.HashMap;
import java.util.Map;


@Component
public class QuizDispatcher {

    @Autowired
    private RandomGenerator random;

    private final Map<QuizType, Quizzes> quizzesMap = new HashMap<>();

    QuizDispatcher() {
        loadQuizzes();
    }

    public Quiz quiz(QuizType quizType) {
        return quizzesMap.get(quizType).quiz(random);
    }

    public Quiz randomQuiz() {
        return quiz(
                QuizType.of(
                        random.getNumber(quizzesMap.size())
                )
        );
    }

    private void loadQuizzes() {
        for (QuizType type : QuizType.values()) {
            quizzesMap.put(type, new Quizzes(type));
        }
    }
}
