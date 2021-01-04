package com.seckill.demo.demo.rateLimiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *<p>Description: </p>
 * @ClassName: ExtRateLimiter
 * @author wangwenzhao
 * @version: V1.0
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtRateLimiter {

    double permitsPerSecond();

    long timeout();
}
