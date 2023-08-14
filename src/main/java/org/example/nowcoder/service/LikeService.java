package org.example.nowcoder.service;

import org.example.nowcoder.constant.CommentConstant;

/**
 * 点赞的功能Service
 */
public interface LikeService extends CommentConstant {

    void like(Integer userId, Integer entityType, Integer entityId);

    Long likeCount(Integer entityType, Integer entityId);

    int likeStatus(Integer userId, Integer entityType, Integer entityId);

}
