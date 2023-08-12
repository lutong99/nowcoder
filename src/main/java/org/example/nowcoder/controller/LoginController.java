package org.example.nowcoder.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class LoginController implements CommunityConstant {

    private UserService userService;
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @GetMapping("/register")
    public String registerPage() {
        return "site/register";
    }

    @GetMapping("/login")
    public String login() {
        return "site/login";
    }

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
            return "site/operate-result";
        } else {
            model.addAttribute("usernameMsg", registerMap.get("usernameMsg"));
            model.addAttribute("emailMsg", registerMap.get("emailMsg"));
            model.addAttribute("passwordMsg", registerMap.get("passwordMsg"));
            return "site/register";
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
        return "site/operate-result";
    }

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
        httpSession.setAttribute(CommunityConstant.KAPTCHA, text);

        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.error("验证码响应失败{}", e.getMessage());
        }

    }

    @PostMapping("/login")
    public String login(Model model, HttpSession session, HttpServletResponse response,
                        String username, String password, String code, boolean remember) {
        log.info("remember: {}", remember);
        // kaptcha
        String kaptcha = (String) session.getAttribute(CommunityConstant.KAPTCHA);
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equals(code)) {
            model.addAttribute("codeMsg", "验证码不正确！");
            return "site/login";
        }

        // user info
        int expiredSeconds = remember ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "site/login";
        }

    }


    @GetMapping("/logout")
    public String logout(Model model, @CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

    @GetMapping("/forget")
    public String forgetPage() {
        return "site/forget";
    }

    @GetMapping("/getCode/{email}")
    @ResponseBody
    public Map<String, Object> resetCode(@PathVariable("email") String email, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(email) || !CommunityUtil.isValidEmail(email)) {
            map.put("code", 300);
            map.put("msg", "邮箱格式不正确");
            return map;
        }
        map = userService.sendResetCode(email);
        if (map.containsKey("verifyCode")) {
            session.setAttribute("verifyCode", map.get("verifyCode"));
            map.remove("verifyCode");
        }
        return map;
    }

    @PostMapping("/reset")
    public String resetPassword(Model model, HttpSession session, String email, String password, String code) {
        String verifyCode = (String) session.getAttribute("verifyCode");
        if (!StringUtils.equals(code, verifyCode)) {
            model.addAttribute("codeMsg", "验证码不正确或已失效");
            return "site/forget";
        }

        Map<String, Object> map = userService.resetPassword(email, password);
        if (map.containsKey("successMsg")) {
            model.addAttribute(OPERATE_RESULT_MSG, map.get("successMsg"));
            model.addAttribute(OPERATE_RESULT_TARGET, "/login");
            return "site/operate-result";
        } else {
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "site/forget";
        }
    }
}
