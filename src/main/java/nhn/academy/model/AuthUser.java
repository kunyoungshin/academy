package nhn.academy.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;

public class AuthUser implements UserDetails {
    private String encodedPassword;
    private String username;
    private Role role;
    public AuthUser(MemberEntity memberEntity) {
        this.username = memberEntity.getName();
        this.encodedPassword = memberEntity.getPassword();
        this.role = memberEntity.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.encodedPassword;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
