package nhn.academy.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFilter;

public class RedisSessionPreAuthenticatedFilter extends AuthenticationFilter {

    public RedisSessionPreAuthenticatedFilter(AuthenticationManager authenticationManager, RedisSessionPreAuthenticatedConverter redisSessionConverter) {
        super(authenticationManager, redisSessionConverter);
    }


}
