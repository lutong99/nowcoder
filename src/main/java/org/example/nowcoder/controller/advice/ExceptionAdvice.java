package org.example.nowcoder.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.constant.ExceptionAdviceConstant;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class ExceptionAdvice implements ExceptionAdviceConstant {


    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("服务器发生异常 {} : {}", e, e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        String xRequestWith = request.getHeader("x-requested-with");
        if (REQUEST_ASYNCHRONOUS.equals(xRequestWith)) {
            log.error("统一处理异常，异步请求");
            // 异步请求
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            ApiResponse errorResponse = ApiResponse.failure("服务器内部异常");
            String errorResponseString = objectMapper.writeValueAsString(errorResponse);
            writer.write(errorResponseString);
        } else {
            log.error("统一处理异常，同步请求");
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }


}
