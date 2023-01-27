package rid.server;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import rid.server.quiz.Quiz;
import rid.server.quiz.QuizDispatcher;
import rid.server.quiz.QuizType;

import javax.servlet.http.HttpServletRequest;

// User status를 가지긴 하나 Restful하진 않음. 그냥 HTTP
// 어차피 TEXT만 밀어넣어주면 됨
@RestController()
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestHandler {

    final QuizDispatcher quizDispatcher;
    final IPStore ipStore;

    // TODO: IPStore에 저장하는 stateful한 요청이니 POST가 되어야 하나?
    @GetMapping
    public String request(final HttpServletRequest request) {
        Quiz quiz = quizDispatcher.randomQuiz();
        storeClientState(request.getRemoteAddr(), quiz);
        return quiz.content();
    }

    @GetMapping("/backend")
    public String backend(final HttpServletRequest request) {
        Quiz quiz = quizDispatcher.quiz(QuizType.BACKEND);
        storeClientState(request.getRemoteAddr(), quiz);
        return quiz.content();
    }

    @GetMapping("/frontend")
    public String frontend(final HttpServletRequest request) {
        Quiz quiz = quizDispatcher.quiz(QuizType.FRONTEND);
        storeClientState(request.getRemoteAddr(), quiz);
        return quiz.content();
    }

    @GetMapping("/tip")
    public String tip() {
        return quizDispatcher.quiz(QuizType.TIPS).content();
    }

    private void storeClientState(String ipAddress, Quiz quiz) {
        ipStore.put(ipAddress, quiz);
    }
}
