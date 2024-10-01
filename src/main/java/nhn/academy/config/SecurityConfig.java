package nhn.academy.config;

import nhn.academy.auth.CustomAuthenticationFailureHandler;
import nhn.academy.auth.CustomAuthenticationSuccessHandler;
import nhn.academy.auth.CustomUserDetailsService;
import nhn.academy.service.LoginAttemptService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        LoginAttemptService loginAttemptService = new LoginAttemptService();
        CustomAuthenticationFailureHandler customAuthenticationFailureHandler = new CustomAuthenticationFailureHandler(loginAttemptService);
        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(loginAttemptService);
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin((formLogin) ->
                formLogin.loginPage("/auth/login")
                        .usernameParameter("id")
                        .passwordParameter("pwd")
                        .loginProcessingUrl("/auth/login/process")
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler(customAuthenticationSuccessHandler)

        ).authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/private-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                        .requestMatchers("/public-project/**").permitAll()
                        .requestMatchers("/auth/login/**").permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(passwordEncoder());
    }

}