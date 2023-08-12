package org.example.nowcoder.service.impl;

import org.example.nowcoder.entity.Comment;
import org.example.nowcoder.entity.CommentExample;
import org.example.nowcoder.mapper.CommentMapper;
import org.example.nowcoder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentMapper commentMapper;

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

}
