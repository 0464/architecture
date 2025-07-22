package study.demo.dashboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import study.demo.domain.User;
import study.infra.redis.QueueService;
import study.infra.security.oauth.OAuth2UserInfo;

@Slf4j
@RestController
@RequestMapping("api/dashboard")
@RequiredArgsConstructor
public class RestDashboardController {

    private final QueueService queueService;

    @GetMapping(value = {"/subscribe"}, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@SessionAttribute(name = "loginUser", required = false) User user) {

        queueService.processQueue(); // scheduler start
        String userId = user.getUuid();

        return queueService.subscribe(userId);
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@SessionAttribute(name = "loginUser", required = false) User user) {

        String userId = user.getUuid();
        queueService.joinQueue(userId);

        return ResponseEntity.ok("✅ 대기열 참가 완료");
    }

    @PostMapping("/exist")
    public ResponseEntity<String> exist() {
        queueService.schedulerStop();

        return ResponseEntity.ok("✅ 대기열 정지");
    }

    @GetMapping("/view")
    public ResponseEntity<String> view(@SessionAttribute(name = "loginUser", required = false) User user) {

        String userId = user.getUuid();
        boolean allowed = queueService.view(userId);

        if (allowed) {
            return ResponseEntity.ok("/chat");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("<html><body><h3>❌ 대기 중이거나 유효하지 않음</h1></body></html>");
        }
    }
}
