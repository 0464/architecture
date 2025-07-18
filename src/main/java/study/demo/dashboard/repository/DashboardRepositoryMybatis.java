package study.demo.dashboard.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.demo.domain.Dashboard;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashboardRepositoryMybatis implements DashboardRepository {

    private final DashboardRepository dashboardRepository;

    @Override
    public Dashboard findByKey(int key) { return dashboardRepository.findByKey(key); }

    @Override
    public List<Dashboard> findAll() { return dashboardRepository.findAll(); }

}
