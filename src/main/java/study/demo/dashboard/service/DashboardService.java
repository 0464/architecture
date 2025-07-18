package study.demo.dashboard.service;

import study.common.util.CommonMap;

import java.util.List;

public interface DashboardService {

    /**
     * 대시보드 첫 화면 데이터 1개를 불러오세요.
     * @param paramMap -
     */
    CommonMap getDashboardData(CommonMap paramMap);

    /**
     * 대시보드 첫 화면 데이터 전체를 불러오세요.
     * @param
     */
    List<CommonMap> getDashboardDataList();
}
