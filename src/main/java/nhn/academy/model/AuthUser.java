package nhn.academy.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;

public class AuthUser implements UserDetails {

    public AuthUser(MemberEntity memberEntity) {
        //TODO
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //TODO 변경
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        //TODO
        return "";
    }

    @Override
    public String getUsername() {
        //TODO
        return "";
    }

}
