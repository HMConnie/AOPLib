package com.sgcai.android.aop.page;

import com.sgcai.android.aop.AopSDK;
import com.sgcai.android.aop.callback.StartActivityIntercept;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

@Aspect
public class StartActivityAspect {

    @Pointcut("execution(* android.app.Activity.startActivityForResult(..))")
    public void pointCutOnNeedLoginMethod() {

    }


    @Around("pointCutOnNeedLoginMethod()")
    public void handleLoginMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object target = joinPoint.getTarget();
        if (target != null && args != null && args.length == 3) {
            List<StartActivityIntercept> intercepts = AopSDK.getIntercepts();
            for (StartActivityIntercept intercept : intercepts) {
                if (intercept.isIntercept(target, args)) {
                    return;
                }
            }
        }

        joinPoint.proceed();

    }


}