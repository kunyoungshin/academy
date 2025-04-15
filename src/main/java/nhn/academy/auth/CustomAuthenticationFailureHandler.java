package nhn.academy.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import nhn.academy.service.LoginFailureCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private LoginFailureCounter loginFailureCounter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        String username = request.getParameter("id");
        loginFailureCounter.increment(username);
        if (loginFailureCounter.getFailures(username) >= 5) {
            System.out.println("아이디 " + username + " 로그인 시도 5회 초과");
        }
        response.sendRedirect("/auth/login?error=true");
    }
}
