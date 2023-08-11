package org.example.nowcoder.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import org.example.nowcoder.entity.User;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LoginController implements CommunityConstant {

    @GetMapping("/register")
    public String registerPage() {
        return "/site/register";
    }

    @GetMapping("/login")
    public String login() {
        return "/site/login";
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        log.info("user is {}", user);
        Map<String, Object> registerMap = userService.register(user);
        log.info("registerMap is {}", registerMap);
        if (registerMap == null || registerMap.isEmpty()) {
            model.addAttribute(OPERATE_RESULT_MSG, "您已经注册成功，我门已向您的邮箱发送了一封激活邮件，请尽快激活");
            model.addAttribute(OPERATE_RESULT_TARGET, "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", registerMap.get("usernameMsg"));
            model.addAttribute("emailMsg", registerMap.get("emailMsg"));
            model.addAttribute("passwordMsg", registerMap.get("passwordMsg"));
            return "/site/register";
        }

    }

    //http://localhost:8080/community/activation/101/code
    @GetMapping("/activate/{userId}/{code}")
    public String activate(Model model, @PathVariable("userId") Integer userId, @PathVariable("code") String code) {
        int result = userService.activate(userId, code);
        if (result == ACTIVATE_SUCCESS) {
            model.addAttribute(OPERATE_RESULT_MSG, "激活成功，您的账号可以正常使用了！");
            model.addAttribute(OPERATE_RESULT_TARGET, "/login");
        } else if (result == ACTIVATE_REPEAT) {
            model.addAttribute(OPERATE_RESULT_MSG, "无效操作，账号已激活，请不要重复激活！");
            model.addAttribute(OPERATE_RESULT_TARGET, "/index");
        } else {
            model.addAttribute(OPERATE_RESULT_MSG, "激活失败，请确认您的激活链接是否正确");
            model.addAttribute(OPERATE_RESULT_TARGET, "/index");
        }
        return "/site/operate-result";
    }

    private Producer kaptchaProducer;

    @Autowired
    public void setKaptchaProducer(Producer kaptchaProducer) {
        this.kaptchaProducer = kaptchaProducer;
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession httpSession) {
        // get kaptcha
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // session saves kaptcha
        httpSession.setAttribute("kaptcha", text);

        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.error("验证码响应失败{}", e.getMessage());
        }

    }
}
