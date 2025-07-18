package study.demo.dashboard.usecase;

import study.common.util.CommonMap;

public interface DashboardUseCase {

    /**
     * 대시보드 첫 화면 데이터를 불러오세요.
     * @param requestMap -
     */
    CommonMap getDashboardData(CommonMap requestMap);
}
