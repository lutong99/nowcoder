package org.example.nowcoder.component.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class ServiceAspect {

    @Pointcut("execution(* org.example.nowcoder.service.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 用户【】在【】访问了【】

        Signature signature = joinPoint.getSignature();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = signature.getDeclaringTypeName() + "." + signature.getName();
        if (requestAttributes == null) {
            log.info("不是通过Servlet访问了{}", method);
            return;
        }
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        String ipAddress = request.getRemoteHost();
        log.info("用户【{}】访问了【{}】", ipAddress, method);
    }

}
