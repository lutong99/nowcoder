package org.example.nowcoder.service.impl;

import org.example.nowcoder.component.SensitiveFilter;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.DiscussPostExample;
import org.example.nowcoder.mapper.DiscussPostMapper;
import org.example.nowcoder.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    private DiscussPostMapper discussPostMapper;

    private SensitiveFilter sensitiveFilter;

    @Autowired
    public void setSensitiveFilter(SensitiveFilter sensitiveFilter) {
        this.sensitiveFilter = sensitiveFilter;
    }

    @Autowired
    public void setDiscussPostMapper(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }

    @Override
    public List<DiscussPost> getAll() {
        return discussPostMapper.selectByExample(null);
    }

    @Override
    public List<DiscussPost> getAllByTimeDesc() {

        DiscussPostExample example = new DiscussPostExample();
        example.setOrderByClause("type desc, create_time desc");
        return discussPostMapper.selectByExample(example);

    }

    @Override
    public List<DiscussPost> getAllByHotDesc() {
        DiscussPostExample example = new DiscussPostExample();
        example.setOrderByClause("type desc, score desc, create_time desc");
        return discussPostMapper.selectByExample(example);
    }

    @Override
    public int addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) {
            throw new IllegalArgumentException("请求的参数不正确");
        }

        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        discussPost.setType(TYPE_NORMAL);
        discussPost.setStatus(STATUS_NORMAL);
        discussPost.setCommentCount(COMMENT_COUNT_DEFAULT);
        discussPost.setScore(SCORE_DEFAULT);

        return discussPostMapper.insert(discussPost);
    }

    @Override
    public DiscussPost getById(Integer id) {
        return discussPostMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateCommentCount(Integer id, Integer count) {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setId(id);
        discussPost.setCommentCount(count);
        return discussPostMapper.updateByPrimaryKeySelective(discussPost);
    }

    @Override
    public List<DiscussPost> getAllByUserId(Integer userId) {

        if (userId == null) {
            throw new RuntimeException("传入的参数不正确, userId == null");
        }

        DiscussPostExample discussPostExample = new DiscussPostExample();
        discussPostExample.setOrderByClause("create_time desc");
        discussPostExample.createCriteria().andUserIdEqualTo(userId);

        return discussPostMapper.selectByExample(discussPostExample);

    }

    @Override
    public int getPostCountByUserId(Integer userId) {
        List<DiscussPost> discussPostList = getAllByUserId(userId);
        return discussPostList == null ? 0 : discussPostList.size();
    }

    @Override
    public int updateStatus(Integer postId, Integer status) {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setId(postId);
        discussPost.setStatus(status);
        return discussPostMapper.updateByPrimaryKeySelective(discussPost);
    }

    @Override
    public int updateType(Integer postId, Integer type) {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setId(postId);
        discussPost.setType(type);

        return discussPostMapper.updateByPrimaryKeySelective(discussPost);
    }

    @Override
    public int updateById(DiscussPost post) {
        return discussPostMapper.updateByPrimaryKeySelective(post);
    }
}
