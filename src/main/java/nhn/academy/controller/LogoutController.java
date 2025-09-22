package nhn.academy.controller;

import jakarta.servlet.http.HttpSession;
import nhn.academy.model.Member;
import nhn.academy.model.MemberLoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping
    public ModelAndView processLogout(HttpSession session)  {
        session.invalidate();
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }
}
