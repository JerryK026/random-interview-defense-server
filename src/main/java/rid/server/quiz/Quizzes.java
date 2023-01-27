package rid.server.quiz;

import org.springframework.core.io.ClassPathResource;
import rid.server.RandomGenerator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * FileI/O 해서 메모리 상에 적재하므로 굳이 따지면 Repository라고 할 수도 있음 현재 객체 자체는 추상화해서
 * Quizzes를 통해서만 접근할 수 있음
 */

/**
 * Bean으로 등록하지 않은 이유
 * 1. lazy Quizzes는 QuizDispatcher에 의존해서 lazyBinding함 (애초에 의존 통로가 Dispatcher뿐)
 * 2. 싱글톤으로 QuizType마다 존재하므로 싱글톤이어선 안됨
 * 3. 의존 방향이 단방향, 선형이므로 굳이 Prototype Scope로 등록할 필요도 못느낌
 */
public class Quizzes {

    private Map<Integer, Quiz> quizzes = new HashMap<>();

    Quizzes(QuizType quizType) {
        init(quizType);
    }

    public Quiz quiz(RandomGenerator random) {
        return quizzes.get(
                random.getNumber(quizzes.size())
        );
    }

    private void init(QuizType quizType) {
        String[] contents = loadQuizzes(quizType.getQuizPath());

        /**
         * lambda는 함수형 대전제를 대전제를 지키기 위해 Query일 때만 적용
         * Command일 때는 반복문을 돌린다
         */
        int size = contents.length;
        for (int idx = 1; idx <= size; idx++) {
            quizzes.put(idx, new Quiz(idx, quizType, contents[idx - 1]));
        }
    }

    /**
     * todo : redis로 옮기는 작업 고려
     * DB 커넥션 비용이 아까워서 File I/O를 택했으나 생각해 보면 DB에서 읽어와도 메모리에 올려놓으면 큰 상관이 없고,
     * DB에 올려놨을 때 안정성 / 확장성이 더 뛰어난 것은 사실.
     * 어차피 id로만 검색하기 때문에 key-value 형식의 db 접근이 효율적으로 보이나, 검색 기능 추가를 고려도 해봐야 함
     */
    private String[] loadQuizzes(String path) {
        byte[] bytes;
        try {
            bytes = new ClassPathResource(path).getInputStream().readAllBytes();
        } catch (IOException e) {
           throw new RuntimeException("Quiz 파일들 warm up 도중 문제 발생");
        }

        return new String(bytes, StandardCharsets.UTF_8).split("%");
    }
}
