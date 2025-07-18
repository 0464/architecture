package study.infra.aop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.demo.domain.AccessLog;

@Repository
public interface AccessLogRepositoryJpa extends JpaRepository<AccessLog, Long> {

}
