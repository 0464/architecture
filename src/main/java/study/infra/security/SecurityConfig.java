package study.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import study.infra.security.oauth.CustomOAuthUserService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuthUserService customOAuthUserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login**", "/css/**", "/font/**", "/js/**", "/icon/**", "/api/**").permitAll() // public 경로
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .requestCache(cache -> cache.requestCache(requestCache()))
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestRepository(authorizationRequestRepository())
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuthUserService))
                        .loginPage("/login")
                        .successHandler(new LoginSuccessHandler()) // 로그인 성공 후 원래 이동할 하려던 페이지
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 후 이동할 경로
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID") // 쿠키 제거
                );

        return http.build();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public RequestCache requestCache() {
        return new HttpSessionRequestCache() {
            private final List<String> BLOCKED_PATHS = List.of(
                    "/.well-known", "/favicon.ico"
            );

            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                String uri = request.getRequestURI();
                boolean isBlocked = BLOCKED_PATHS.stream().anyMatch(uri::startsWith);
                if (!isBlocked) {
                    super.saveRequest(request, response); // 유효한 요청만 저장
                }
            }
        };
    }
}
