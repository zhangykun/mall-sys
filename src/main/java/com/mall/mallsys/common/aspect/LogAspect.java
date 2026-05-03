package com.mall.mallsys.common.aspect;

import com.alibaba.fastjson2.JSON;
import com.mall.mallsys.common.annotation.Log;
import com.mall.mallsys.modules.system.entity.SysLog;
import com.mall.mallsys.modules.system.entity.SysUser;
import com.mall.mallsys.modules.system.service.SysLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志切面
 * 拦截 @Log 注解，自动记录操作日志到数据库
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 定义切点：所有标注了 @Log 注解的方法
     */
    @Pointcut("@annotation(com.mall.mallsys.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 环绕通知：在目标方法执行前后记录日志
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 记录开始时间
        long startTime = System.currentTimeMillis();

        // 2. 获取 HttpServletRequest 对象
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 3. 获取执行方法和注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        String operation = logAnnotation != null ? logAnnotation.value() : "";

        // 4. 获取操作人
        String username = null;
        Object securityUser = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (securityUser instanceof SysUser sysUser) {
            username = sysUser.getUsername();
        }

        // 5. 构建日志对象
        SysLog sysLog = new SysLog();
        sysLog.setUsername(username);
        sysLog.setOperation(operation);
        // 方法全限定名
        sysLog.setMethod(joinPoint.getTarget().getClass().getName() + "." + method.getName());

        // 请求参数（转 JSON，避免过长的参数）
        String params = JSON.toJSONString(joinPoint.getArgs());
        // 参数超过 5000 字符截断，防止数据库字段溢出
        if (params.length() > 5000) {
            params = params.substring(0, 5000);
        }
        sysLog.setParams(params);

        // 获取 IP 地址和浏览器信息
        if (request != null) {
            sysLog.setIp(getClientIp(request));
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.length() > 100) {
                userAgent = userAgent.substring(0, 100);
            }
            sysLog.setBrowser(userAgent);
        }

        // 6. 执行目标方法，记录结果
        Object result;
        try {
            result = joinPoint.proceed();  // 执行原方法
            sysLog.setStatus(1);           // 标记成功
            return result;
        } catch (Throwable e) {
            sysLog.setStatus(0);           // 标记失败
            // 截取异常信息前 500 字符存入数据库
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 500) {
                errorMsg = errorMsg.substring(0, 500);
            }
            sysLog.setErrorMsg(errorMsg);
            throw e;
        } finally {
            // 7. 计算执行耗时（无论成功失败都会执行）
            sysLog.setTime(System.currentTimeMillis() - startTime);
            sysLog.setCreateTime(LocalDateTime.now());
            // 8. 保存日志到数据库
            sysLogService.save(sysLog);
        }
    }

    /**
     * 获取客户端真实 IP 地址
     * 考虑了代理服务器（Nginx 等）转发的情况
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理时取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}