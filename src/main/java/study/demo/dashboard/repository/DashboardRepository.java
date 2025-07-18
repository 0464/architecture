package study.demo.dashboard.repository;

import org.apache.ibatis.annotations.Mapper;
import study.demo.domain.Dashboard;
import java.util.List;

@Mapper
public interface DashboardRepository {

    /**
     * 대시보드 테이블에서 데이터 1개를 검색하세요.
     * @param key -
     */
    Dashboard findByKey(int key);

    /**
     * 대시보드 테이블에서 데이터 전체를 검색하세요.
     *
     * @param
     * -
     */
    List<Dashboard> findAll();


}
