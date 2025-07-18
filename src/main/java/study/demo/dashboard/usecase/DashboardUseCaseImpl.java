package study.demo.dashboard.usecase;

import study.common.util.CommonMap;
import study.demo.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardUseCaseImpl implements DashboardUseCase {

    /**
     * 유즈케이스 구현부는 여러개의 하위서비스를 조합하여 데이터를 반환해야합니다.
     *
     */
    private final DashboardService dashboardService;

    @Override
    @Transactional (readOnly = true)
    public CommonMap getDashboardData(CommonMap requestMap) {

        CommonMap returnMap = new CommonMap();

        returnMap.put("data", dashboardService.getDashboardData(requestMap));
        returnMap.put("datalist", dashboardService.getDashboardDataList());

        return returnMap;
    }
}
