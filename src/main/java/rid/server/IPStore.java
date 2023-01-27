package rid.server;

import org.springframework.stereotype.Component;
import rid.server.quiz.Quiz;

import java.util.HashMap;
import java.util.Map;

/**
 * todo: IP 블랙박스 차단 기능 추가
 * LRU 리스트에 넣어놓고 악의적 요청으로 보이는 IP의 경우 차단
 */
@Component
public class IPStore {
    private Map<String, Quiz> quizMap = new HashMap<>();

    public void put(String ipAddress, Quiz quiz) {
        quizMap.put(ipAddress, quiz);
    }

    public boolean isContains(String ip) {
        return quizMap.containsKey(ip);
    }

    public Quiz quiz(String ipAddress) {
        return quizMap.get(ipAddress);
    }
}
