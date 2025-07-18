package study.infra.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.infra.aop.AccessLogRepositoryJpa;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AccessLogRepositoryJpa accessLogRepositoryJpa;

    @Bean
    public FilterRegistrationBean<AccessLogFilter> accessLogFilter() {
        FilterRegistrationBean<AccessLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AccessLogFilter(accessLogRepositoryJpa));
        registrationBean.setAsyncSupported(true);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<EncodingFilter> encodingFilter() {
        FilterRegistrationBean<EncodingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EncodingFilter());
        registrationBean.setAsyncSupported(true);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
}
