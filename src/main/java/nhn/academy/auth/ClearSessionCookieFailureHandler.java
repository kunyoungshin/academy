package nhn.academy.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

public class ClearSessionCookieFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public ClearSessionCookieFailureHandler() { super("/auth/login?error"); }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
//        Cookie kill = new Cookie("SESSIONID", "");
//        kill.setMaxAge(0);
//        kill.setPath("/");
//        kill.setHttpOnly(true);
//        kill.setSecure(request.isSecure());
//        response.addCookie(kill);
        // 중요한 부분: 예외 던지지 않음, 리다이렉트 없음
    }
}