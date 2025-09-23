package nhn.academy.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import nhn.academy.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;


    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoProvider,
                                                       RedisAuthenticationProvider redisProvider) {
        // 우선순위는 필요에 따라 조정
        return new ProviderManager(List.of(redisProvider, daoProvider));
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, new AuthenticationConverter() {
            @Override
            public Authentication convert(HttpServletRequest request) {
                if (request.getCookies() == null) return null;
                for (Cookie cookie : request.getCookies()) {
                    if ("SESSIONID".equals(cookie.getName())) {
                        String sessionId = cookie.getValue();
                        return new PreAuthenticatedAuthenticationToken(sessionId, "N/A");
                    }
                }
                return null; // 토큰 없으면 인증 시도 안 함
            }
        });
        authenticationFilter.setSuccessHandler((req, res, auth) -> {});
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin((formLogin) ->
                formLogin.loginPage("/auth/login")
                        .usernameParameter("id")
                        .passwordParameter("pwd")
                        .loginProcessingUrl("/auth/login/process")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)

        ).exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/403") // 403 오류 발생 시 처리할 페이지 설정
        ).authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/private-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                        .requestMatchers("/public-project/**").permitAll()
                        .requestMatchers("/auth/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/members").permitAll()
                        .anyRequest().authenticated()
        ).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // csrf diable
        http.csrf(AbstractHttpConfigurer::disable);
        // UsernamePasswordAuthenticationFilter가 활성화
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }



}