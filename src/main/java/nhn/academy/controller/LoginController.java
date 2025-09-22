package nhn.academy.controller;

import jakarta.servlet.http.HttpSession;
import nhn.academy.model.Member;
import nhn.academy.model.MemberLoginRequest;
import nhn.academy.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final MemberService memberService;
    public static final String LOGIN_USER = "LOGIN_USER";
    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping
    public ModelAndView processLogin(@ModelAttribute MemberLoginRequest loginRequest, HttpSession session)  {
        Member memberResponse = memberService.login(loginRequest);
        ModelAndView mav = new ModelAndView("home");
        HttpSession newSession = session;   // 실전은 request.getSession(true) 새 세션 발급이 적절
        newSession.setAttribute(LOGIN_USER, memberResponse);
        return mav;
    }


}

