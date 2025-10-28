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
    private final MemberService memberService;

    public RedisSessionPreAuthenticatedProvider(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username  = (String) authentication.getPrincipal();
        Member member = memberService.getMember(username);
        AuthUser authUser = new AuthUser(member);
        Authentication auth = new PreAuthenticatedAuthenticationToken(authUser, null, authUser.getAuthorities());
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RedisSessionPreAuthenticatedToken.class.isAssignableFrom(authentication);
    }
}