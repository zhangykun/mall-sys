package com.mall.mallsys.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * @author mall
 * @since 1.0.0
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;  // 过期时间（毫秒）

    /**
     * 获取签名密钥（兼容各种长度的 secret）
     */
    private SecretKey getSigningKey() {
        // 方式一：如果 secret 长度足够（至少 32 字节），可以直接使用
        // 方式二：对 secret 进行 SHA-256 哈希后使用（推荐，更安全）
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 JWT Token
     * @param userId 用户 ID
     * @param username 用户名
     * @return Token
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("tokenType", "access_token");
        return createToken(claims, username);
    }

    /**
     * 创建 Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)                          // 自定义声明
                .setSubject(subject)                        // 主题（用户名）
                .setIssuedAt(now)                           // 签发时间
                .setExpiration(expirationDate)              // 过期时间
                .signWith(getSigningKey())                  // 使用新 API 签名
                .compact();
    }

    /**
     * 从 Token 获取用户 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        return Long.valueOf(userIdObj.toString());
    }

    /**
     * 从 Token 获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 验证签名和过期时间
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            log.error("JWT 签名验证失败：{}", e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("JWT Token 已过期：{}", e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.error("JWT Token 格式错误：{}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 检查 Token 是否过期
     */
    private boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 从 Token 获取 Claims（使用新 API）
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}