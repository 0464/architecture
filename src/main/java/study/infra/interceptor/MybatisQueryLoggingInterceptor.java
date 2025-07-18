package study.infra.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

/**
 * <code>Mybatis</code>에서 제공하는 인터셉트로 SQL문 로깅 구현
 *
 */

@Slf4j
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class})
})
public class MybatisQueryLoggingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

        String queryId = ms.getId();
        log.info("[Mybatis QueryId] {}", queryId);
        Object result = invocation.proceed(); // 실제 SQL 실행

        long queryTime = System.currentTimeMillis() - start;
        log.info("<==  QueryTime: {} ms", queryTime);

        return result;
    }

}
