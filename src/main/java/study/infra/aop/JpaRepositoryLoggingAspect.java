package study.infra.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP {@link Aspect}를 이용하여 JPA SQL문 로깅 부가기능 구현
 */

@Slf4j
@Aspect
@Component
public class JpaRepositoryLoggingAspect {

    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
    public Object logRepositoryQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        String queryId = joinPoint.getSignature().toShortString();
        log.info("[JPA QueryId] {}", queryId);
        Object result = joinPoint.proceed();

        long queryTime = System.currentTimeMillis() - start;
        log.info("<==  QueryTime: {} ms", queryTime);

        return result;
    }
}
