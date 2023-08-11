package org.example.nowcoder.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.nowcoder.entity.LoginTicket;
import org.example.nowcoder.entity.LoginTicketExample;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.UserExample;
import org.example.nowcoder.mapper.LoginTicketMapper;
import org.example.nowcoder.mapper.UserMapper;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, CommunityConstant, LoginTicketConstant, UserConstant {

    private UserMapper userMapper;

    private MailClient mailClient;
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    public void setMailClient(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUser(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 验证用户信息是否正确
        if (user == null) {
            throw new IllegalStateException("参数不能为空");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "注册邮箱不能为空");
            return map;
        }

        User userSelect = getUserByUsername(user.getUsername());
        if (userSelect != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }

        userSelect = getUserByEmail(user.getEmail());
        if (userSelect != null) {
            map.put("emailMsg", "该邮箱已存在");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(TYPE_NORMAL);
        user.setStatus(STATUS_INACTIVATED);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());

        userMapper.insert(user);


        // 给用户发送邮件激活

        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activate/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "账号激活", content);
        return map;
    }

    @Override
    public User getUserByUsername(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public User getUserByEmail(String email) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public int activate(Integer userId, String code) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ACTIVATE_FAILURE;
        }
        if (user.getStatus() == STATUS_ACTIVATED) {
            return ACTIVATE_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            user.setStatus(1);
            userMapper.updateByPrimaryKeySelective(user);
            return ACTIVATE_SUCCESS;
        } else {
            return ACTIVATE_FAILURE;
        }
    }

    @Autowired
    public void setLoginTicketMapper(LoginTicketMapper loginTicketMapper) {
        this.loginTicketMapper = loginTicketMapper;
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "用户密码不能为空");
            return map;
        }

        User user = getUserByUsername(username);
        if (user == null) {
            map.put("usernameMsg", "用户不存在");
            return map;
        }
        if (user.getStatus() == STATUS_INACTIVATED) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }
        if (!Objects.equals(CommunityUtil.md5(password + user.getSalt()), user.getPassword())) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(VALID_STATUS);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insert(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        LoginTicketExample example = new LoginTicketExample();
        example.createCriteria().andTicketEqualTo(ticket);
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setTicket(ticket);
        loginTicket.setStatus(INVALID_STATUS);
        loginTicketMapper.updateByExampleSelective(loginTicket, example);

    }

    @Override
    public Map<String, Object> sendResetCode(String email) {
        Map<String, Object> map = new HashMap<>();
        User userByEmail = getUserByEmail(email);
        if (userByEmail == null) {
            map.put("code", 300);
            map.put("msg", "该邮箱未注册");
        } else {
            String code = CommunityUtil.generateRandomCode(6);
            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("code", code);
            String content = templateEngine.process("/mail/forget", context);
            mailClient.sendMail(email, "重置密码", content);
            map.put("code", 200);
            map.put("msg", "验证码已发送");
            map.put("verifyCode", code);
        }
        return map;

    }

    @Override
    public Map<String, Object> resetPassword(String email, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "邮箱格式不正确");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码格式不正确");
            return map;
        }

        User userByEmail = getUserByEmail(email);
        userByEmail.setPassword(CommunityUtil.md5(password + userByEmail.getSalt()));
        userMapper.updateByPrimaryKeySelective(userByEmail);
        map.put("successMsg", "密码重置成功");
        return map;
    }

    @Override
    public LoginTicket getLoginTicketByTicket(String ticket) {
        LoginTicketExample example = new LoginTicketExample();
        example.createCriteria().andTicketEqualTo(ticket);
        List<LoginTicket> loginTickets = loginTicketMapper.selectByExample(example);
        if (loginTickets == null || loginTickets.size() == 0) {
            return null;
        }
        return loginTickets.get(0);
    }
}
