package study.infra.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.infra.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/dashboard**", "/chat**", "/admin**"); // 보호하고 싶은 경로
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로
                .allowedOrigins("http://localhost:3000")  // Vue dev 서버
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);  // 쿠키 전달 허용 시
    }
}
