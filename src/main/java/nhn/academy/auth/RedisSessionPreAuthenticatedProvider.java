package nhn.academy.auth;

import nhn.academy.model.AuthUser;
import nhn.academy.model.Member;
import nhn.academy.service.MemberService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;



@Component
public class RedisSessionPreAuthenticatedProvider implements AuthenticationProvider {
    private final RedisTemplate redisTemplate;
    private final MemberService memberService;

    public RedisSessionPreAuthenticatedProvider(RedisTemplate redisTemplate, MemberService memberService) {
        this.redisTemplate = redisTemplate;
        this.memberService = memberService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String sessionId = (String) authentication.getPrincipal();
        Object o = redisTemplate.opsForValue().get(sessionId);
        String username = (String) o;
        if (username != null) {
            try {
                Member member = memberService.getMember(username);
                AuthUser authUser = new AuthUser(member);
                Authentication auth = new PreAuthenticatedAuthenticationToken(authUser, null, authUser.getAuthorities());
                return auth;
            }catch (Exception e) {

            }

        }
        throw new BadCredentialsException("Invalid session");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RedisSessionPreAuthenticatedToken.class.isAssignableFrom(authentication);
    }
}