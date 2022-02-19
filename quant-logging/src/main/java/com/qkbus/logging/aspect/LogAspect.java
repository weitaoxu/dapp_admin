
package com.qkbus.logging.aspect;

import com.qkbus.logging.domain.SysLog;
import com.qkbus.logging.service.LogService;
import com.qkbus.utils.RequestHolder;
import com.qkbus.utils.SecurityUtils;
import com.qkbus.utils.StringUtils;
import com.qkbus.utils.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 少林一枝花
 * @date 2021-11-24
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    private final LogService logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.qkbus.logging.aop.log.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        SysLog sysLog = new SysLog("INFO", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.save(getUsername(), StringUtils.getBrowser(request),
                StringUtils.getIp(RequestHolder.getHttpServletRequest()), joinPoint,
                sysLog, getUid());
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog sysLog = new SysLog("ERROR", System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        sysLog.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.save(getUsername(), StringUtils.getBrowser(request),
                StringUtils.getIp(RequestHolder.getHttpServletRequest()),
                (ProceedingJoinPoint) joinPoint, sysLog, getUid());
    }

    public String getUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return "";
        }
    }

    public Long getUid() {
        try {
            return SecurityUtils.getUserId();
        } catch (Exception e) {
            return 0L;
        }
    }
}
