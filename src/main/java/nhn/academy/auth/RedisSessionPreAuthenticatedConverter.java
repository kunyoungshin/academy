package nhn.academy.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Service;

@Service
public class RedisSessionPreAuthenticatedConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("SESSIONID".equals(cookie.getName())) {
                String sessionId = cookie.getValue();
                return new RedisSessionPreAuthenticatedToken(sessionId, "N/A");
            }
        }
        return null; // 토큰 없으면 인증 시도 안 함
    }
}
