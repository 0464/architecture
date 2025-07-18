package study.infra.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 로그인 성공 후에 전에 방문한 페이지가 있다면 <code>prevPage</code>로 이동합니다.
 */

@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String prevPage = (String) session.getAttribute("prevPage");
            if (prevPage != null) {
                session.removeAttribute("prevPage");
                getRedirectStrategy().sendRedirect(request, response, prevPage);
                return;
            }
        }

        // 별도 로직 없으면 부모가 SavedRequest 확인 후 리다이렉트 수행
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
