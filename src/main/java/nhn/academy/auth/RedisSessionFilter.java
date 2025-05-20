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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RedisSessionFilter extends OncePerRequestFilter {


    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // 쿠키에서 세션 ID 가져오기

        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        if (sessionId != null) {
            // Redis에서 인증 정보 가져오기
            Object o = redisTemplate.opsForValue().get(sessionId);
            String username = (String) o;
            if (username != null) {
                try {
                    Member member = memberService.getMember(username);
                    AuthUser authUser = new AuthUser(member);
                    Authentication auth = new PreAuthenticatedAuthenticationToken(authUser, null, authUser.getAuthorities());
                    auth.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }catch (Exception e) {

                }

            }
        }


        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
