package org.example.nowcoder.service.impl;

import org.example.nowcoder.entity.User;
import org.example.nowcoder.mapper.UserMapper;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.NowcoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUser(String userId) {
        return userMapper.selectByPrimaryKey(NowcoderUtils.parseInt(userId, 0));
    }
}
