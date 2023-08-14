package org.example.nowcoder.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Component
@Slf4j
public class AlphaInterceptor implements HandlerInterceptor {

    DataSourceProperties dataSourceProperties;


    DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Controller 之前执行
        log.debug("preHandle: handler is {}", handler);
        log.debug("preHandle datasourceProperties.password is {}", dataSourceProperties.getPassword());
        log.debug("preHandle datasourceProperties.username is {}", dataSourceProperties.getUsername());
        log.debug("preHandle datasourceProperties.name is {}", dataSourceProperties.getName());
        log.debug("preHandle datasourceProperties.url is {}", dataSourceProperties.getUrl());
        log.debug("preHandle datasource.type is {}", dataSource.getClass());

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在Controller之后执行
        log.debug("postHandle handler is {}", handler);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在模板引擎之后执行
        log.debug("afterCompletion handler is {}", handler);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
