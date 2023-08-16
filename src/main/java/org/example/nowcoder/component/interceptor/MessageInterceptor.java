package org.example.nowcoder.component.interceptor;

import org.example.nowcoder.component.UserHostHolder;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MessageInterceptor implements HandlerInterceptor {

    private UserHostHolder userHostHolder;

    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserHostHolder(UserHostHolder userHostHolder) {
        this.userHostHolder = userHostHolder;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = userHostHolder.getUser();
        if (user != null && modelAndView != null) {
            int messageNoticeUnreadCount = messageService.getMessageUnreadCount(user.getId());
            modelAndView.addObject("messageNoticeUnreadCount", messageNoticeUnreadCount);
        }

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
