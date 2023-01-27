package rid.server.quiz;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum QuizType {
    BACKEND(1, "/quiz/backend.fortune", "/answer/backend/"),
    FRONTEND(2, "/quiz/frontend.fortune", "/answer/frontend/"),
    // TIPS의 answerPath 때문에 도메인에 대한 이해가 불가결해지는 것 같음. 성급한 추상화 느낌
    // 일단 어댑터 패턴 꼴의 모양새를 취한다
    TIPS(3, "/quiz/tip.fortune", null);

    private final int sequence;
    private final String quizPath;
    private final String answerPath;

    QuizType(int sequence, String quizPath, String answerPath) {
        this.sequence = sequence;
        this.quizPath = quizPath;
        this.answerPath = answerPath;
    }

    public static QuizType of(int sequence) {
        return Arrays.stream(values())
                .filter(qt -> qt.sequence == sequence)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지정된 QuizType이 아닙니다"));
    }
}
