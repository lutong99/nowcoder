package org.example.nowcoder.component.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.constant.LoginTicketConstant;
import org.example.nowcoder.entity.LoginTicket;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Slf4j
public class LoginTicketInterceptor implements HandlerInterceptor {

    private UserService userService;
    private UserHostHolder userHostHolder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = WebUtil.getCookieValue(request, "ticket");
        log.info("用户访问{} 登录验证开始", request.getServletPath());
        if (ticket != null) {
            LoginTicket loginTicketByTicket = userService.getLoginTicketByTicket(ticket);
            if (loginTicketByTicket != null) {
                if (loginTicketByTicket.getStatus().equals(LoginTicketConstant.VALID_STATUS) && loginTicketByTicket.getExpired().after(new Date())) {
                    User user = userService.getById(loginTicketByTicket.getUserId());
                    userHostHolder.setUser(user);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), userService.getAuthorities(user.getId()));
                    SecurityContextHolder.setContext(new SecurityContextImpl(usernamePasswordAuthenticationToken));
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = userHostHolder.getUser();
        if (modelAndView != null && user != null) {
            modelAndView.addObject("loginUser", user);
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userHostHolder.clearUsers();
//        SecurityContextHolder.clearContext();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

