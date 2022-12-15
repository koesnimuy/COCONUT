package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 필터 설정 하기
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 권한 설정
        http.authorizeHttpRequests()
            .antMatchers("/admin", "/admin/**").hasAuthority("ADMIN")
            .antMatchers("/teacher", "/teacher/**").hasAuthority("TEACHER")
            .antMatchers("/student", "/student/**").hasAuthority("STUDENT")
            .anyRequest().permitAll();
        
        // 로그인
        http.formLogin()
            .loginPage("/login.do")
            .loginProcessingUrl("/login")
            .usernameParameter("id")
            .passwordParameter("pw")
            .defaultSuccessUrl("/adhome")
            .permitAll();

        // 로그아웃
        http.logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/adhome")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .permitAll();

        // 로그인 시 세션을 서버에 저장하지 않음
        // ALWAYS 항상 세션을 생성
        // IF_REQUIRED 기본값
        // NEVER 생성하지만 있으면 기존것 사용
        // STATELESS 생성하지 않고 기존것도 사용하지 않음
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //post호출시 csrf키를 요구하지않음
        // http.csrf().disable(); <= 비권장

        http.csrf().ignoringAntMatchers("/api/**");
        http.headers().frameOptions().sameOrigin();

        // 접근불가 페이지일때 보여지는 url주소
        http.exceptionHandling().accessDeniedPage("/page403");

        return http.build();
    }

    // 암호의 hash알고리즘 설정
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // userDetailsService 대체용
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
