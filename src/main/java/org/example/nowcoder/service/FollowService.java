package org.example.nowcoder.service;

import org.example.nowcoder.constant.CommentConstant;

public interface FollowService extends CommentConstant {


    void follow(int userId, int entityType, int entityId);


    void unfollow(int userId, int entityType, int entityId);


    /**
     * 某个实体的粉丝
     */
    Long followerCount(Integer entityType, Integer entityId);

    /**
     * 某个人关注的实体数量
     */
    Long followeeCount(Integer userId, Integer entityType);

    /**
     * 用户有没有关注实体，查看实体的关注者中，有没有这个用户
     * @return {@code true} {@code userId} 用户 关注了{@code entity(entityType, entityId)}
     *          {@code false} 用户没有关注
     */
    Boolean followStatus(Integer userId, Integer entityType, Integer entityId);

}
