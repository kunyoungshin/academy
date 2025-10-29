package nhn.academy.service;

import nhn.academy.model.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    MemberService memberService;


    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username.equals("admin")){
            return new User("admin", passwordEncoder.encode("admin"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }else if(username.equals("member")){
            return new User("member", passwordEncoder.encode("member"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER")));
        }

        throw new UsernameNotFoundException("User not found");



    }
}