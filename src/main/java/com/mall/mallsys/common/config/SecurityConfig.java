package com.mall.mallsys.common.config;

import com.mall.mallsys.common.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置类
 *
 * @author mall
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity  // 开启 Spring Security
@EnableMethodSecurity(prePostEnabled = true)  // 开启方法级权限控制
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 配置安全过滤链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF（JWT 不需要）
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 禁用 Session（无状态认证）
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. 配置 CORS（允许跨域）
                .cors(cors -> {})

                // 4. 授权配置
                .authorizeHttpRequests(auth -> auth
                        // 放行登录、登出接口
                        .requestMatchers("/api/system/login", "/api/system/logout").permitAll()
                        // 放行 Swagger/API 文档
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/webjars/**").permitAll()
                        // 放行 OPTIONS 请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )

                // 5. 添加 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 配置认证提供者
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
