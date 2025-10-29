package nhn.academy.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class AuthUser implements UserDetails {

    private MemberEntity memberEntity;
    public AuthUser(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_"+memberEntity.getRole().name()));
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getName();
    }

}