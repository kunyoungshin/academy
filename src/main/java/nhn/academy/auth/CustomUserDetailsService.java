package nhn.academy.auth;

import nhn.academy.model.AuthUser;
import nhn.academy.model.MemberEntity;
import nhn.academy.service.MemberService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberService memberService;
    private PasswordEncoder passwordEncoder;
    public CustomUserDetailsService(PasswordEncoder passwordEncoder,
                                    MemberService memberService) {
        this.passwordEncoder = passwordEncoder;
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberService.getMemberEntity(username);
        return new AuthUser(memberEntity);

    }
}
