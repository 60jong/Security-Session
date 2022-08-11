package com.jongky.sessionprac;

import com.jongky.sessionprac.handler.LoginFailureHandler;
import com.jongky.sessionprac.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler successHandler = new LoginSuccessHandler();
    private final AuthenticationFailureHandler failureHandler = new LoginFailureHandler();
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().antMatchers("/user/**", "/sign**", "/login");
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();

        http.formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")// "/login" 이 호출되면 시큐리티가 낚아채서 로그인을 진행한다.
                .successHandler(successHandler)
                .failureHandler(failureHandler);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry());

        http.rememberMe()
                .key("ykj")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(600)
                .userDetailsService(userDetailsService)
                .authenticationSuccessHandler(successHandler);

        return http.build();
    }
}
