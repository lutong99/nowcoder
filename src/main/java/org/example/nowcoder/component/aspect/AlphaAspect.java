package org.example.nowcoder.component.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

//@Component
//@Aspect
@Slf4j
public class AlphaAspect {

    @Pointcut("execution(* org.example.nowcoder.service.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        log.info("面向切面编程 before");
    }

    @After("pointcut()")
    public void after() {
        log.info("面向切面编程 after");
    }


    @AfterReturning("pointcut()")
    public void afterReturning() {
        log.info("面向切面编程 afterReturning");
    }


    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        log.info("面向切面编程 afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("面向切面编程 around before");

        Object obj = joinPoint.proceed();
        log.info("面向切面编程 around after");
        return obj;

    }


}
