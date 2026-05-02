package com.mall.mallsys.common.filter;

import com.mall.mallsys.common.utils.JwtUtils;
import com.mall.mallsys.modules.system.entity.SysUser;
import com.mall.mallsys.modules.system.service.SysUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * JWT 认证过滤器
 *
 * @author mall
 * @since 1.0.0
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SysUserService sysUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. 从请求头获取 Token
            String token = getTokenFromRequest(request);

            // 2. 验证 Token
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                // 3. 从 Token 中获取用户 ID
                Long userId = jwtUtils.getUserIdFromToken(token);

                // 4. 加载用户信息和权限
                SysUser user = sysUserService.getById(userId);

                // 5. 检查用户状态
                if (user != null && user.getStatus() == 1) {
                    // 6. 获取用户权限并转换为 Spring Security 授权对象
                    List<String> permissions = sysUserService.getUserPermissions(userId);
                    List<SimpleGrantedAuthority> authorities = permissions.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    // 7. 设置认证信息到 SecurityContext
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    authorities
                            );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("用户 {} 认证成功", user.getUsername());
                }
            }
        } catch (Exception e) {
            log.error("JWT 认证失败：{}", e.getMessage());
        }

        // 7. 继续执行过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // 格式：Bearer <token>
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}