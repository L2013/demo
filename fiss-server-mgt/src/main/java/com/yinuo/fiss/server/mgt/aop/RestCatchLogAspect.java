package com.yinuo.fiss.server.mgt.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yinuo.base.dto.Response;
import com.yinuo.base.exception.ResultCodeEnum;
import com.yinuo.utils.TryCatchFuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * @author liang
 */
@Aspect
@Slf4j
public class RestCatchLogAspect {

    @Pointcut("execution(public com.yinuo.base.dto.Response *(..))")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();

        logRequest(joinPoint);

        Object response = null;
        try {
            response = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            response = Response.failure(ResultCodeEnum.EXCEPTION.getCode(), e.getMessage());
        } finally {
            logResponse(startTime, response);
        }
        return response;
    }

    private void logResponse(long startTime, Object response) {
        TryCatchFuncUtil.swallowThrowable(() -> {
            log.debug("RESPONSE: " + JSON.toJSONString(response));
            log.debug("COST: " + (System.currentTimeMillis() - startTime) + "ms");
        });
    }

    private void logRequest(ProceedingJoinPoint joinPoint) {
        TryCatchFuncUtil.swallowThrowable(() -> {
            if (!log.isDebugEnabled()) {
                return;
            }
            log.debug("START PROCESSING: " + joinPoint.getSignature().toShortString());
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                log.debug("REQUEST ARG: " + JSON.toJSONString(arg, SerializerFeature.IgnoreErrorGetter));
            }
        });
    }

}
