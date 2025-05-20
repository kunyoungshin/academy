package nhn.academy.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import nhn.academy.service.LoginFailureCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LoginFailureCounter loginFailureCounter;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 로그인 성공 시 추가 작업을 수행하는 로직
        String sessionId = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
        sessionCookie.setHttpOnly(true); // 보안 설정
        sessionCookie.setMaxAge(60 * 60); // 쿠키 유효시간 (1시간)
        sessionCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        response.addCookie(sessionCookie);
        redisTemplate.opsForValue().set(sessionId, authentication.getName());
        loginFailureCounter.reset(authentication.getName());
        // 원래 요청한 URL로 리다이렉트
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
