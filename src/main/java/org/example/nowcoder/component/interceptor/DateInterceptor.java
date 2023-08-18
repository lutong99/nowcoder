package org.example.nowcoder.component.interceptor;

import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DateInterceptor implements HandlerInterceptor {

    private UserHostHolder userHostHolder;
    private DataService dataService;

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Autowired
    public void setDateService(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteHost();
        dataService.recordIP(ip);
        User user = userHostHolder.getUser();
        if (user != null) {
            dataService.recordDAU(user.getId());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
