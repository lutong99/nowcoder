package org.example.nowcoder.service;

import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.entity.Comment;

import java.util.List;

public interface CommentService extends CommentConstant {

    List<Comment> getListByEntity(Integer entityType, Integer entityId);

    int getCountByEntity(Integer entityType, Integer entityId);

    int addComment(Comment comment);

    List<Comment> getListByUserId(Integer userId);

    int getCountByUserId(Integer userId);

    Integer getPostId(Integer id);

    Comment getById(Integer id);

}
