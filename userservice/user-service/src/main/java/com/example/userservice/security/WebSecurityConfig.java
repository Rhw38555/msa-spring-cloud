package com.example.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final Environment env;

    public WebSecurityConfig(JwtAuthTokenProvider jwtAuthTokenProvider, Environment env) {
        this.jwtAuthTokenProvider = jwtAuthTokenProvider;
        this.env = env;
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 권한에 따라 허용하는 url 설정
        // /login, /signup 페이지는 모두 허용, 다른 페이지는 인증된 사용자만 허용

        http.csrf().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
//                .anyRequest().authenticated()
                .antMatchers("/actuator/**","/login").permitAll()
                .antMatchers(HttpMethod.POST,"/users").permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
