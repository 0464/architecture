package study.demo.login.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping(value = {"/login"})
    public ModelAndView login() {

        ModelAndView mv = new ModelAndView("login");
        mv.addObject("login", null);
        return mv;
    }
}
