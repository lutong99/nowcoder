package org.example.nowcoder.service;


import org.example.nowcoder.constant.DiscussPostConstant;
import org.example.nowcoder.entity.DiscussPost;

import java.util.List;

public interface DiscussPostService extends DiscussPostConstant {

    List<DiscussPost> getAll();

    List<DiscussPost> getAllByTimeDesc();

    List<DiscussPost> getAllByHotDesc();

    int addDiscussPost(DiscussPost discussPost);

}
