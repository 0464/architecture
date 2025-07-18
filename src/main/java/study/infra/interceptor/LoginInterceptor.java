package study.infra.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;
import java.util.Set;

/**
 * <code>/login</code>에 접근 시 전에 방문한 페이지를 {@link HttpSession}에 저장합니다.
 * <p>로그인 세션을 체크하고 비로그인상태면 로그인창으로 리다이렉트 합니다.
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private static final Set<String> EXCLUDED_PREFIXES = Set.of(
            "/login", "/oauth2", "/.well-known", "/favicon.ico"
    );

    private static final Set<String> EXCLUDED_SUFFIXES = Set.of(
            ".js", ".css", ".png", ".jpg"
    );

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String fullUrl = uri + (query == null ? "" : "?" + query);
        boolean isExcluded = EXCLUDED_PREFIXES.stream().anyMatch(uri::startsWith)
                            || EXCLUDED_SUFFIXES.stream().anyMatch(uri::endsWith);
        if (!isExcluded) {
            request.getSession().setAttribute("prevPage", fullUrl);
        }
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
