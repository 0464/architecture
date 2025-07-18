package study.infra.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

/**
 * <code>UTF-8 Encoding</code>을 적용합니다.
 */

@Slf4j
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 요청과 응답에 UTF-8 인코딩 적용
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //log.debug("UTF-8 Encoding...");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}

}
