package study.infra.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.filter.OncePerRequestFilter;
import study.infra.aop.AccessLogRepositoryJpa;
import study.demo.domain.AccessLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 접근 로그 {@link AccessLog}를 저장하고 콘솔에 디버깅 {@code logMessage}합니다.
 *
 */

@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class AccessLogFilter extends OncePerRequestFilter {

    private final AccessLogRepositoryJpa accessLogRepositoryJpa;

    // 로그에 포함할 URI 목록
    private final Set<String> INCLUDED_URIS = Set.of(
        "/login",
        "/dashboard"
    );

    // 로그에 미포함할 URI 목록
    private final Set<String> EXCLUDED_PREFIXES = Set.of(
            "/font", "/css", "/.well-known", "/favicon.ico", "/health-check", "/actuator/health"
    );

    private final Set<String> EXCLUDED_SUFFIXES = Set.of(
            ".js", ".css", ".png", ".jpg"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String userAgent = request.getHeader("User-Agent");
        String queryParams = getQueryParams(request).toString();

        String logMessage = String.format("""
            \n🟢 [FILTER DETAILS]
            🔹 IP: %s
            🔹 METHOD: %s
            🔹 URI: %s
            🔹 QUERY-PARAMS: %s
            🔹 USER-AGENT: %s
            ----------------------------------------------------------------------------------------------------""",
                ip, method, uri, queryParams, userAgent);

        // 제외할 URI라면 로깅 안함
        boolean isExcluded = EXCLUDED_PREFIXES.stream().anyMatch(uri::startsWith)
                             || EXCLUDED_SUFFIXES.stream().anyMatch(uri::endsWith);

        if (!isExcluded) {
            log.debug(logMessage);
        }

        // 로그인 및 이용자만 접속로그 DB 저장
        if (INCLUDED_URIS.stream().anyMatch(uri::startsWith)) {
            AccessLog accessLog = new AccessLog(ip, uri, method, userAgent, queryParams);
            saveLog(accessLog);
        }

        // 다음 필터로 넘김
        filterChain.doFilter(request, response);

    }

    @Async
    public void saveLog(AccessLog accessLog) {
        accessLogRepositoryJpa.save(accessLog);
    }

    private Map<String, String> getQueryParams(HttpServletRequest request) {
        Map<String, String> queryParams = new HashMap<>();
        request.getParameterMap()
                .forEach((key, value) -> queryParams.put(key, String.join(",", value)));
        return queryParams;
    }
}
