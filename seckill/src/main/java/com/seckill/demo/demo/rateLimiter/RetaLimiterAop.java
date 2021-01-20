package com.seckill.demo.demo.rateLimiter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.util.concurrent.RateLimiter;
import com.seckill.demo.demo.util.DataResponse;

/**
 *
 * 限流的 注解的方法的捕获 及处理
 * @ClassName: RetaLimiterAop
 * @version: V1.0
 */
@Aspect
@Component
public class RetaLimiterAop {

    // 存放 令牌桶， 一个 url 一个桶
    private Map<String, RateLimiter> retaLimterMap = new ConcurrentHashMap<>();

    /**
     * 定义切点
     *
     * @author wangwenzhao
     */
    @Pointcut("execution(* com.seckill.demo.demo.rateLimiter.RateLimiterResource..*.*(..))")
    private void intAop() {
    };

    @Around("intAop()")
    private Object retaBefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("-----------------------aop---------");
        // 获取aop 拦截的方法
        Method method = getSinatureMethod(proceedingJoinPoint);
        if (Objects.isNull(method)) {
            System.out.println("----获取拦截方法 失败！！！！");
            return null;
        }
        // 判断 方法上有没有 自定义限流 注解 @ExtRateLimiter
        ExtRateLimiter extRateLimiter = method.getDeclaredAnnotation(ExtRateLimiter.class);
        if (Objects.isNull(extRateLimiter)) {
            // 如果不含有 直接执行目标方法
            return proceedingJoinPoint.proceed();
        }

        // 有的 话，取参数值
        double permitsPerSecond = extRateLimiter.permitsPerSecond();
        long timeout = extRateLimiter.timeout();

        // 调用 原生的 RetaLimiter 请求。 保证 一个 url 一个桶，相同的请求在同一个桶
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String resulteUrI = attributes.getRequest().getRequestURI();

        RateLimiter rateLimiter = null;
        if (retaLimterMap.containsKey(resulteUrI)) {
            // 如果有 ，获取之前的 桶。
            rateLimiter = retaLimterMap.get(resulteUrI);
        } else {
            // 没有的话 创建要给 新桶存放令牌
            rateLimiter = RateLimiter.create(permitsPerSecond);
            retaLimterMap.put(resulteUrI, rateLimiter);
        }

        // 有效 时间内 获取令牌，获取到 执行目标方法，没活动到走服务降级
        boolean token = rateLimiter.tryAcquire(timeout, TimeUnit.MICROSECONDS);
        if (!token) {
            skillfallback();
            return null;
        }

        return proceedingJoinPoint.proceed();

    }

    private DataResponse skillfallback() throws IOException {
        System.out.println("------------服务降级----------back");
        return DataResponse.getInstance("------抢购限流---抢购活动结束-------");
    }

    /**
     * @throws IOException
     */
    private void fallback() throws IOException {
        System.out.println("限流 执行 服务降级-------");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            writer.println("再抢 也抢不到你想 要的东西了，歇一歇吧 朋友！");
        } catch (Exception e) {

        } finally {
            writer.close();

        }
    }

    /**
     * 获取 aop 拦截方法
     *
     * @author wangwenzhao
     * @return
     */
    private Method getSinatureMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method;

    }

}
