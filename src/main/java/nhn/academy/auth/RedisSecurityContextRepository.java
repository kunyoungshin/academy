package nhn.academy.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * SecurityContext 를 Redis 에 저장하는 SecurityContextRepository.
 * HttpSession 대신 직접 발급한 쿠키(ACADEMY-SESSION) 의 UUID 를 키로 사용 -> 스케일아웃 가능.
 */
@Component
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private static final String COOKIE_NAME = "ACADEMY-SESSION";
    private static final String REDIS_KEY_PREFIX = "security:context:";
    private static final Duration EXPIRE = Duration.ofMinutes(30);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSecurityContextRepository(RedisConnectionFactory connectionFactory) {
        // TODO #1: RedisTemplate 생성 후 connectionFactory 연결
        //  - keySerializer 는 StringRedisSerializer
        //  - valueSerializer 는 JdkSerializationRedisSerializer (AuthUser 는 Serializable 구현 필요)
        //  - afterPropertiesSet() 호출
        this.redisTemplate = null;
    }

    // 매 요청마다 SecurityContextHolderFilter 가 호출 -> Redis 에서 SecurityContext 복원
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder holder) {
        String sessionId = readCookie(holder.getRequest());
        if (sessionId == null) {
            return SecurityContextHolder.createEmptyContext();
        }

        // TODO #2: Redis 에서 (REDIS_KEY_PREFIX + sessionId) 키로 값을 조회해
        //  SecurityContext 면 그대로 리턴, 아니면 빈 컨텍스트 리턴
        return SecurityContextHolder.createEmptyContext();
    }

    // 로그인 성공 시 successfulAuthentication() 에서 호출 -> 쿠키 발급 + Redis 저장
    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = readCookie(request);
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            writeCookie(response, sessionId);
        }

        // TODO #3: redisTemplate 으로 (REDIS_KEY_PREFIX + sessionId) 키에 context 저장 (TTL: EXPIRE)
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String sessionId = readCookie(request);
        if (sessionId == null) {
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX + sessionId));
    }

    private String readCookie(HttpServletRequest request) {
        // TODO #4: request 의 쿠키 배열에서 이름이 COOKIE_NAME 인 쿠키의 value 를 리턴
        //  (없으면 null 리턴)
        return null;
    }

    private void writeCookie(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(COOKIE_NAME, sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
