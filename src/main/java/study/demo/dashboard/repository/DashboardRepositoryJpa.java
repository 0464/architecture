package study.demo.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.demo.domain.Dashboard;

@Repository
public interface DashboardRepositoryJpa extends JpaRepository<Dashboard, Long>, DashboardRepository {

}
