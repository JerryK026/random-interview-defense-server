package rid.server.answer;

import org.springframework.core.io.DefaultResourceLoader;
import rid.server.quiz.QuizType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class Answers {

    private Map<Integer, Answer> answers = new HashMap<>();

    Answers(QuizType quizType) {
        init(quizType);
    }

    /**
     *     Answer가 아닌 String으로 넘겨서 Handler에서 Answer에 대해 알지 못하도록 의존성 제거
     *     vs
     *     Quiz에서는 Quiz로 넘겨 IPStore 저장에 사용하므로 일관성 지키기
     *     => 일관성을 지키는 것이 더 객체지향적이라고 판단
      */
    public Answer answer(int id) {
        return answers.get(id);
    }

    private void init(QuizType quizType) {
        List<String> contents = loadAnswers(quizType.getAnswerPath());

        int size = contents.size();
        for (int idx = 1; idx <= size; idx++) {
            answers.put(idx, new Answer(idx, quizType, contents.get(idx - 1)));
        }
    }

    private List<String> loadAnswers(String path) {
        File[] files;

        try {
            files = new DefaultResourceLoader().getResource(path).getFile().listFiles();
        } catch (IOException e) {
            // 에러 메시지를 상수화하면 오히려 관리 포인트 및 복잡도가 늘어나기 때문에 예외에 메시지 직접 명시
            // 상수화하지 않아도 어차피 상수풀에 올라감
            throw new RuntimeException("Answer 파일 메모리에 적재 중 문제 발생", e);
        }

        List<String> answerStrings = new ArrayList<>();

        for (File file : files) {
            try {
                answerStrings.add(
                        new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8)
                );
            } catch (IOException e) {
                throw new RuntimeException("Answer 파일들 Byte[]로 변환 중 문제 발생", e);
            }
        }

        return answerStrings;
    }
}
