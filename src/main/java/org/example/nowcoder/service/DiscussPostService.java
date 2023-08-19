package org.example.nowcoder.service;


import org.example.nowcoder.constant.DiscussPostConstant;
import org.example.nowcoder.entity.DiscussPost;

import java.util.List;

public interface DiscussPostService extends DiscussPostConstant {

    List<DiscussPost> getAll();

    List<DiscussPost> getAllByTimeDesc();

    List<DiscussPost> getAllByHotDesc();

    int addDiscussPost(DiscussPost discussPost);

    DiscussPost getById(Integer id);

    int updateCommentCount(Integer id, Integer count);

    List<DiscussPost> getAllByUserId(Integer userId);

    int getPostCountByUserId(Integer userId);

    int updateStatus(Integer postId, Integer status);

    int updateType(Integer postId, Integer type);

    int updateById(DiscussPost post);

}
