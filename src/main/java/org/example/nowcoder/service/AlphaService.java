package org.example.nowcoder.service;

import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.constant.DiscussPostConstant;
import org.example.nowcoder.constant.UserConstant;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.mapper.AlphaDao;
import org.example.nowcoder.mapper.DiscussPostMapper;
import org.example.nowcoder.mapper.UserMapper;
import org.example.nowcoder.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("singleton")
@Slf4j
public class AlphaService {

    private AlphaDao alphaDao;
    private UserMapper userMapper;
    private DiscussPostMapper discussPostMapper;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public AlphaService() {
        System.out.println("AlphaService Constructor");
    }

    @Autowired
    public void setDiscussPostMapper(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void init() {
        System.out.println("init method PostConstructor");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("pre destroy");
    }

    //    @Autowired
    public void setAlphaDao(AlphaDao alphaDao) {
        this.alphaDao = alphaDao;
    }

    public String find() {
        return alphaDao.select();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String save() {
        User user = new User();
        String salt = CommunityUtil.generateUUID().substring(0, 5);
        user.setSalt(salt);
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setHeaderUrl("https://images.nowcoder.com/heade/98t.png");
        user.setType(UserConstant.TYPE_NORMAL);
        user.setStatus(UserConstant.STATUS_INACTIVATED);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setEmail("zhangsan@qq.com");
        user.setCreateTime(new Date());
        userMapper.insert(user);

        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle("hello");
        discussPost.setContent("hello world");
        discussPost.setCreateTime(new Date());
        discussPost.setUserId(user.getId());
        discussPost.setType(DiscussPostConstant.TYPE_NORMAL);
        discussPost.setStatus(DiscussPostConstant.STATUS_NORMAL);

        discussPostMapper.insert(discussPost);
        Integer.valueOf("1234j");
        return "success";
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public String save1() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(status -> {
            User user = new User();
            String salt = CommunityUtil.generateUUID().substring(0, 5);
            user.setSalt(salt);
            user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
            user.setHeaderUrl("https://images.nowcoder.com/heade/98t.png");
            user.setType(UserConstant.TYPE_NORMAL);
            user.setStatus(UserConstant.STATUS_INACTIVATED);
            user.setActivationCode(CommunityUtil.generateUUID());
            user.setEmail("zhangsan@qq.com");
            user.setCreateTime(new Date());
            userMapper.insert(user);

            DiscussPost discussPost = new DiscussPost();
            discussPost.setTitle("hello");
            discussPost.setContent("hello world");
            discussPost.setCreateTime(new Date());
            discussPost.setUserId(user.getId());
            discussPost.setType(DiscussPostConstant.TYPE_NORMAL);
            discussPost.setStatus(DiscussPostConstant.STATUS_NORMAL);

            discussPostMapper.insert(discussPost);
            Integer.valueOf("1234j");
            return "success";
        });
    }

    //    @Async
    public void testThreadPoolExecutor() {
        log.debug("testThreadPoolExecutorSimple");
    }

    //    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public void testScheduledThreadPoolExecutor() {
        log.debug("testScheduledThreadPoolExecutor");
    }

}
