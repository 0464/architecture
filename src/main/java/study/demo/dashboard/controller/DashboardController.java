package study.demo.dashboard.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import study.common.util.CommonMap;
import study.demo.dashboard.usecase.DashboardUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardUseCase dashboardDataSelectUseCase;

    @GetMapping(value = {""})
    public ModelAndView index() {
        CommonMap requestMap = new CommonMap();
        requestMap.put("key", 1);

        ModelAndView mv = new ModelAndView("dashboard");
        mv.addObject("dashboard", dashboardDataSelectUseCase.getDashboardData(requestMap));
        return mv;
    }
}
