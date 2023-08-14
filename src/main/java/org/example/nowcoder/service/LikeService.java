package org.example.nowcoder.service;

import org.example.nowcoder.constant.CommentConstant;

/**
 * 点赞的功能Service
 */
public interface LikeService extends CommentConstant {

    /**
     * 为帖子或者评论点赞
     * @param userId 点赞发起人的用户id
     * @param entityType 点赞的类型，帖子 or 评论
     * @param entityId 类型的id，帖子 or 评论的id
     * @param entityUserId 被点赞的用户id
     */
    void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId);

    Long likeCount(Integer entityType, Integer entityId);

    int likeStatus(Integer userId, Integer entityType, Integer entityId);

    int likeUserCount(Integer userId);

}
