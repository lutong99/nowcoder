package org.example.nowcoder.config;

import org.example.nowcoder.component.interceptor.AlphaInterceptor;
import org.example.nowcoder.component.interceptor.DateInterceptor;
import org.example.nowcoder.component.interceptor.LoginTicketInterceptor;
import org.example.nowcoder.component.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    private AlphaInterceptor alphaInterceptor;

    private LoginTicketInterceptor loginTicketInterceptor;


    private MessageInterceptor messageInterceptor;

    private DateInterceptor dateInterceptor;

    @Autowired
    public void setDateInterceptor(DateInterceptor dateInterceptor) {
        this.dateInterceptor = dateInterceptor;
    }

    @Autowired
    public void setMessageInterceptor(MessageInterceptor messageInterceptor) {
        this.messageInterceptor = messageInterceptor;
    }

    @Autowired
    public void setAlphaInterceptor(AlphaInterceptor alphaInterceptor) {
        this.alphaInterceptor = alphaInterceptor;
    }

    @Autowired
    public void setLoginTicketInterceptor(LoginTicketInterceptor loginTicketInterceptor) {
        this.loginTicketInterceptor = loginTicketInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login", "/user/**");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.JPG", "/**/*.map", "/error");

        // loginRequiredInterceptor
        // registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        // 统计读消息的数量
        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(dateInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");


        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
