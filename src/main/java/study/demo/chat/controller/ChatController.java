package study.demo.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import study.common.util.CommonMap;
import study.demo.dashboard.usecase.DashboardUseCase;
import study.demo.domain.ChatMessage;
import study.demo.domain.User;
import study.infra.redis.QueueService;
import study.infra.security.oauth.OAuth2UserInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatController {

    private final QueueService queueService;
    private final DashboardUseCase dashboardDataSelectUseCase;

    @GetMapping(value = {""})
    public ModelAndView chat(@SessionAttribute(name = "loginUser", required = false) User user) {

        String userId = user.getUuid();
        boolean allowed = queueService.chat(userId);

        if (allowed) {
            ModelAndView mv = new ModelAndView("chat");
            mv.addObject("chat", null);

            return mv;
        } else {

            CommonMap requestMap = new CommonMap();
            requestMap.put("key", 1);
            ModelAndView mv = new ModelAndView("dashboard");
            mv.addObject("dashboard", dashboardDataSelectUseCase.getDashboardData(requestMap));

            return mv;
        }
    }

    @MessageMapping("/chat/send") // /app/chat/send 로 전송된 메시지 처리
    @SendTo("/topic/messages")    // 구독자에게 전송할 경로
    public ChatMessage send(ChatMessage message, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        User user = (User) sessionAttributes.get("loginUser");

        return new ChatMessage(message.getContent(), user.getName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
    }
}
