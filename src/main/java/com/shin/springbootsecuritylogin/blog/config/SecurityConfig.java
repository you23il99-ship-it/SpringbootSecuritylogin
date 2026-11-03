package com.shin.springbootsecuritylogin.blog.config;

import com.shin.springbootsecuritylogin.blog.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                // CSRF 보호 유지
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // H2 콘솔 iframe 접근 허용
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 페이지 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/signup",
                                "/favicon.ico",
                                "/h2-console/**",
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 사용자 정의 로그인 컨트롤러 사용
                .formLogin(form -> form.disable());

        return http.build();
    }

    // BCrypt 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DB 회원 정보 조회 및 비밀번호 검증 담당
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // 로그인 인증 관리자
    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider authenticationProvider) {

        return new ProviderManager(authenticationProvider);
    }

    // 로그인 인증 정보를 세션에 저장
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}