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
import java.text.SimpleDateFormat;
import java.util.Date;
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

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        String ipAddress = request.getRemoteHost();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Signature signature = joinPoint.getSignature();
        String method = signature.getDeclaringTypeName() + "." + signature.getName();
        log.info("用户【{}】在【{}】访问了【{}】", ipAddress, date, method);
    }

}
