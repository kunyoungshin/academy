package nhn.academy.config;

import nhn.academy.auth.CustomAuthenticationFailureHandler;
import nhn.academy.auth.CustomAuthenticationSuccessHandler;
import nhn.academy.auth.RedisSecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private RedisSecurityContextRepository redisSecurityContextRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin((formLogin) ->
                formLogin.loginPage("/auth/login")
                        .usernameParameter("id")
                        .passwordParameter("pwd")
                        .loginProcessingUrl("/auth/login/process")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)

        ).authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/private-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                        .requestMatchers("/public-project/**").permitAll()
                        .requestMatchers("/auth/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/members").permitAll()
                        .anyRequest().authenticated()
        );
        // csrf diable
        http.csrf(AbstractHttpConfigurer::disable);
        http.securityContext(s->s.securityContextRepository(redisSecurityContextRepository));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}