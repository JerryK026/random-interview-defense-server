package rid.server;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rid.server.answer.AnswerDispatcher;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseHandler {

    private static final String NO_USER_STATUS_MESSAGE = "저장된 최근 정보가 존재하지 않거나 시간이 초과되어 사라졌습니다";

    private final AnswerDispatcher answerDispatcher;
    private final IPStore ipStore;

    @GetMapping
    public String response(final HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if (hasNotState(ipAddress)) {
            return NO_USER_STATUS_MESSAGE;
        }

        return answerDispatcher.answer(
                ipStore.quiz(ipAddress)
        ).content();
    }

    private boolean hasNotState(String ipAddress) {
        return !ipStore.isContains(ipAddress);
    }
}
