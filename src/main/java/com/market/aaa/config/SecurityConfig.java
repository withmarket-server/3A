package com.market.aaa.config;

import com.market.aaa.jwt.JwtAuthenticationFilter;
import com.market.aaa.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 과거에는 Security 설정을 WebSecurityConfigurerAdapter 클래스를 상속받아서 구현했지만,
 * Spring Boot 버전이 올라가면서 해당 방식은 Deprecated 되었다.
 * 따라서 이제는 빈 등록을 통해 Security를 설정한다
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api이므로 basic auth 및 (header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다)
                .csrf().disable() // csrf 보안을 사용하지 않는다는 설정이다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정이다.
                .and()
                .authorizeRequests()
                .antMatchers("/members/login").permitAll() // 해당 API에 대해서는 모든 요청을 허가한다는 설정이다.
                .antMatchers("/members/test").hasRole("USER") // 해당 API에 대해서는 USER 권한이 있어야 요청할 수 있다는 설정이다.
                .anyRequest().authenticated() // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정이다.
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
                // JWT 인증을 위하여 직접 구현한 JwtAuthenticationFilter 필터를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정이다.
        return http.build();
    }

    // JWT를 사용하기 위해서는 기본적으로 password encoder가 필요한데, 여기서는 Bycrypt encoder를 사용했다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}