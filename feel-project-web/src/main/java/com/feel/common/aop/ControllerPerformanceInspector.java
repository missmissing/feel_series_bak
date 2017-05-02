package com.feel.common.aop;

import com.google.common.base.Stopwatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 监控微服务包下的controller方法运行消耗时间
 *
 * @author yuweijun 2017-04-10
 */
@Aspect
public class ControllerPerformanceInspector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerPerformanceInspector.class);

    @Around("execution(public * com.lyancafe.wechat.controller.*.*(..))")
    public Object ellipseTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object retVal = joinPoint.proceed();
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        StringBuffer message = new StringBuffer();
        message.append(joinPoint.getTarget().getClass().getName());
        message.append(".");
        message.append(joinPoint.getSignature().getName());
        message.append("(");
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String name = arg == null ? null : arg.getClass().getSimpleName();
            message.append(name).append(",");
        }
        if (args.length > 0) {
            message.deleteCharAt(message.length() - 1);
        }
        message.append(")");
        message.append(" execution time: ");
        message.append(elapsed);
        message.append(" ms");

        LOGGER.info(message.toString());
        return retVal;
    }

}

