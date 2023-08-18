package org.example.nowcoder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.constant.ExceptionAdviceConstant;
import org.example.nowcoder.entity.vo.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.io.PrintWriter;

@Configuration
@Slf4j
public class SecurityConfig implements CommunityConstant {

    /*
         在这个类中，配置 web 权限和 HTTP权限
    */

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests((http) -> {
            http.antMatchers(
                    "/actuator/**",
                    "/alpha/**",
                    "/comment/add/**",
                    "/discuss/add",
                    "/follow",
                    "/like",
                    "/message/**",
                    "/notice/**",
                    "/user/setting",
                    "/user/upload",
                    "/user/updatePassword"
            ).hasAnyAuthority(
                    AUTHORITY_USER, AUTHORITY_MODERATOR, AUTHORITY_ADMIN
            ).anyRequest().permitAll();
        });


        // 没有权限时
        httpSecurity.exceptionHandling().authenticationEntryPoint(((request, response, authException) -> {
            String xRequestWith = request.getHeader(ExceptionAdviceConstant.X_REQUEST_WITH);
            if (ExceptionAdviceConstant.REQUEST_ASYNCHRONOUS.equals(xRequestWith)) {
                // 异步请求
                log.info("未登录异步请求: {}", request.getServletPath());
                response.setContentType("application/json; charset=utf-8");
                PrintWriter writer = response.getWriter();
                ApiResponse errorResponse = ApiResponse.failure("您还没有登录，请登录后再进行访问");
                String errorResponseString = objectMapper.writeValueAsString(errorResponse);
                writer.write(errorResponseString);
            } else {
                log.info("未登录异步请求: {}", request.getServletPath());
                response.sendRedirect(request.getContextPath() + "/login");
            }
        })).accessDeniedHandler(((request, response, accessDeniedException) -> {
            String xRequestWith = request.getHeader(ExceptionAdviceConstant.X_REQUEST_WITH);
            if (ExceptionAdviceConstant.REQUEST_ASYNCHRONOUS.equals(xRequestWith)) {
                // 异步请求
                log.info("没有权限访问: {}", request.getServletPath());
                response.setContentType("application/json; charset=utf-8");
                PrintWriter writer = response.getWriter();
                ApiResponse errorResponse = ApiResponse.failure("您还没有权限执行该操作");
                String errorResponseString = objectMapper.writeValueAsString(errorResponse);
                writer.write(errorResponseString);
            } else {
                log.info("未登录异步请求: {}", request.getServletPath());
                response.sendRedirect(request.getContextPath() + "/denied");
            }
        }));
        httpSecurity.logout().logoutUrl("/securitylogout");
        return httpSecurity.build();
    }
}