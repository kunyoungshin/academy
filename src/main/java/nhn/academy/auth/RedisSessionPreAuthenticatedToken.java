package nhn.academy.auth;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;


public class RedisSessionPreAuthenticatedToken extends PreAuthenticatedAuthenticationToken {
    public RedisSessionPreAuthenticatedToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
    }
}
