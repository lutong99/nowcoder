package org.example.nowcoder.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.UserExample;
import org.example.nowcoder.mapper.UserMapper;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.CommunityConstant;
import org.example.nowcoder.util.CommunityUtil;
import org.example.nowcoder.util.MailClient;
import org.example.nowcoder.util.NowcoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    private UserMapper userMapper;

    private MailClient mailClient;
    private TemplateEngine templateEngine;

    @Autowired
    public void setMailClient(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUser(String userId) {
        return userMapper.selectByPrimaryKey(NowcoderUtils.parseInt(userId, 0));
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
        user.setType(0);
        user.setStatus(0);
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
        if (user.getStatus() == 1) {
            return ACTIVATE_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            user.setStatus(1);
            userMapper.updateByPrimaryKeySelective(user);
            return ACTIVATE_SUCCESS;
        } else {
            return ACTIVATE_FAILURE;
        }
    }
}
