package org.example.nowcoder.config;

import org.example.nowcoder.component.interceptor.AlphaInterceptor;
import org.example.nowcoder.component.interceptor.LoginRequiredInterceptor;
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

    private LoginRequiredInterceptor loginRequiredInterceptor;

    private MessageInterceptor messageInterceptor;

    @Autowired
    public void setMessageInterceptor(MessageInterceptor messageInterceptor) {
        this.messageInterceptor = messageInterceptor;
    }

    @Autowired
    public void setLoginRequiredInterceptor(LoginRequiredInterceptor loginRequiredInterceptor) {
        this.loginRequiredInterceptor = loginRequiredInterceptor;
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
        registry.addInterceptor(alphaInterceptor).
                excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login", "/user/**");

        registry.addInterceptor(loginTicketInterceptor).
                excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor).
                excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(messageInterceptor).
                excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
