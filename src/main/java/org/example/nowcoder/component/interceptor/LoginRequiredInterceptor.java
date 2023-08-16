package org.example.nowcoder.component.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.annotation.LoginRequired;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.ExceptionAdviceConstant;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Component
@Slf4j
public class LoginRequiredInterceptor implements HandlerInterceptor {

    private UserHostHolder userHostHolder;


    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
            User loginUser = userHostHolder.getUser();
            if (loginRequired != null && loginUser == null) {
                String xRequestWith = request.getHeader(ExceptionAdviceConstant.X_REQUEST_WITH);
                if (ExceptionAdviceConstant.REQUEST_ASYNCHRONOUS.equals(xRequestWith)) {
                    // 异步请求
                    log.info("未登录异步请求: {}", request.getRequestURI());
                    response.setContentType("application/json; charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    ApiResponse errorResponse = ApiResponse.failure("请先登录后再做操作");
                    String errorResponseString = objectMapper.writeValueAsString(errorResponse);
                    writer.write(errorResponseString);
                } else {
                    log.info("未登录异步请求: {}", request.getRequestURI());
                    response.sendRedirect(request.getContextPath() + "/login");
                }
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
