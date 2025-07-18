package study.demo.dashboard.service;

import lombok.RequiredArgsConstructor;
import study.common.util.CommonMap;
import study.common.util.CommonMapConverter;
import study.demo.dashboard.factory.DashboardRepositoryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.demo.domain.Dashboard;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepositoryFactory dashboardRepositoryFactory;
    private final CommonMapConverter commonMapConverter;

    @Override
    @Transactional(readOnly = true)
    public CommonMap getDashboardData(CommonMap paramMap) {

        int key = (int) paramMap.get("key");
        Dashboard dashboard = dashboardRepositoryFactory.jpa().findByKey(key);

        return commonMapConverter.convertToMap(dashboard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommonMap> getDashboardDataList() {

        List<Dashboard> dashboardList = dashboardRepositoryFactory.mybatis().findAll();

        return commonMapConverter.convertToMapList(dashboardList);
    }
}
