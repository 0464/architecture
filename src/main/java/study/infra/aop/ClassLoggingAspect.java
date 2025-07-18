package study.infra.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * AOP {@link Aspect}를 이용하여 메서드 호출 로깅 부가기능 구현
 */

@Slf4j
@Aspect
@Component
public class ClassLoggingAspect {

    // demo 패키지만 로깅
    @Pointcut
    ("execution(* study.demo..*.*(..)) || execution(* study.infra..*.*(..))")

    public void loggingPackages() {}

    @Before("loggingPackages()")
    public void logClass(JoinPoint joinPoint) {

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("{}.{}()", className, methodName);
    }
}
