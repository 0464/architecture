package study.demo.dashboard.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.demo.dashboard.repository.DashboardRepository;
import study.demo.dashboard.repository.DashboardRepositoryJpa;
import study.demo.dashboard.repository.DashboardRepositoryMybatis;

@Component
@RequiredArgsConstructor
public class DashboardRepositoryFactory {

    private final DashboardRepositoryJpa jpaRepository;
    private final DashboardRepositoryMybatis mybatisRepository;

    public DashboardRepository jpa() {
        return jpaRepository;
    }

    public DashboardRepository mybatis() {
        return mybatisRepository;
    }

}
