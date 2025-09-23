package nhn.academy.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhn.academy.model.AuthUser;
import nhn.academy.model.Member;
import nhn.academy.model.annotation.Auth;
import nhn.academy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RedisSessionFilter extends OncePerRequestFilter {


    private final RedisAuthenticationProvider redisAuthenticationProvider;

    public RedisSessionFilter(RedisAuthenticationProvider redisAuthenticationProvider) {
        this.redisAuthenticationProvider = redisAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String sessionId = getSessionId(request);
        if (sessionId != null) {
            Authentication preAuth =
                    new PreAuthenticatedAuthenticationToken(sessionId, null);
            try {
                Authentication auth = redisAuthenticationProvider.authenticate(preAuth);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
            }
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private static String getSessionId(HttpServletRequest request) {
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }
        return sessionId;
    }
}
