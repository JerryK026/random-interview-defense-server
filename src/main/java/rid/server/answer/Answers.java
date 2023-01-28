package rid.server.answer;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import rid.server.quiz.QuizType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
        for (int i = 0; i < size; i++) {
            String currentContents = contents.get(i);
            int idx = getQuizIndex(currentContents);
            answers.put(idx, new Answer(idx, quizType, currentContents));
        }
    }

    private List<String> loadAnswers(String path) {
        Resource[] resources = readResourcesFromPath(path);

        List<String> answerStrings = new ArrayList<>();

        for (Resource resource : resources) {
            String contents = bufferedReader2String(
                    resource2BufferedReader(resource)
            );

            answerStrings.add(contents);
        }

        return answerStrings;
    }

    private String bufferedReader2String(BufferedReader br) {
        return br.lines().collect(Collectors.joining("\n"));
    }

    private static BufferedReader resource2BufferedReader(Resource resource) {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Resource에서 BufferedReader로 파싱하는 도중 에러가 발생했습니다", e);
        }
        return br;
    }

    private Resource[] readResourcesFromPath(String path) {
        Resource[] resources;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources(path + "*");
        } catch (IOException e) {
            throw new RuntimeException("패턴에 일치하는 답변을 찾는 도중 에러가 발생했습니다", e);
        }
        return resources;
    }

    private int getQuizIndex(String contents) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; Character.isDigit(contents.charAt(i)); i++) {
            sb.append(contents.charAt(i));
        }

        return Integer.parseInt(sb.toString());
    }
}
