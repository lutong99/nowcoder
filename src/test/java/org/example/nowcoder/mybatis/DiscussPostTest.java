package org.example.nowcoder.mybatis;

import org.example.nowcoder.NowcoderApplication;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.DiscussPostExample;
import org.example.nowcoder.mapper.DiscussPostMapper;
import org.example.nowcoder.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


@SpringBootTest
@ContextConfiguration(classes = NowcoderApplication.class)
public class DiscussPostTest {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    public void testUser() {
        DiscussPostExample example = new DiscussPostExample();
        DiscussPostMapper discussPostMapper = applicationContext.getBean(DiscussPostMapper.class);
        List<DiscussPost> discussPostList = discussPostMapper.selectByExample(example);
        System.out.println("discussPostList.size() = " + discussPostList.size());

    }

    @Test
    public void testDiscussPostSelect() {
        DiscussPostService discussPostService = applicationContext.getBean(DiscussPostService.class);
        List<DiscussPost> allByTimeDesc = discussPostService.getAllByTimeDesc();
        allByTimeDesc.forEach(System.out::println);

        List<DiscussPost> allByHotDesc = discussPostService.getAllByHotDesc();
        allByHotDesc.forEach(System.out::println);
    }
}
