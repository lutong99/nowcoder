package org.example.nowcoder.service.impl;

import org.example.nowcoder.component.SensitiveFilter;
import org.example.nowcoder.entity.Comment;
import org.example.nowcoder.entity.CommentExample;
import org.example.nowcoder.mapper.CommentMapper;
import org.example.nowcoder.service.CommentService;
import org.example.nowcoder.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentMapper commentMapper;
    private SensitiveFilter sensitiveFilter;
    private DiscussPostService discussPostService;

    @Autowired
    public void setSensitiveFilter(SensitiveFilter sensitiveFilter) {
        this.sensitiveFilter = sensitiveFilter;
    }

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @Autowired
    public void setCommentMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public List<Comment> getListByEntity(Integer entityType, Integer entityId) {
        CommentExample commentExample = new CommentExample();
        commentExample.setOrderByClause("create_time asc");
        commentExample.createCriteria().andEntityTypeEqualTo(entityType).andEntityIdEqualTo(entityId);
        return commentMapper.selectByExample(commentExample);
    }

    @Override
    public int getCountByEntity(Integer entityType, Integer entityId) {
        CommentExample commentExample = new CommentExample();
        commentExample.setOrderByClause("create_time asc");
        commentExample.createCriteria().andEntityTypeEqualTo(entityType).andEntityIdEqualTo(entityId);
        return commentMapper.countByExample(commentExample);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("请求的参数不合法");
        }
        comment.setTargetId(Objects.requireNonNullElse(comment.getTargetId(), TARGET_ID_DEFAULT));
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        comment.setStatus(STATUS_DEFAULT);
        comment.setCreateTime(new Date());
        int insert = commentMapper.insert(comment);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            Integer postId = comment.getEntityId();
            int count = getCountByEntity(ENTITY_TYPE_POST, postId);
            discussPostService.updateCommentCount(postId, count);
        }

        return insert;
    }

}
