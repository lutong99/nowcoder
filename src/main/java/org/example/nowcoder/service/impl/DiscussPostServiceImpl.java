package org.example.nowcoder.service.impl;

import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.DiscussPostExample;
import org.example.nowcoder.mapper.DiscussPostMapper;
import org.example.nowcoder.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    private DiscussPostMapper discussPostMapper;

    @Autowired
    public void setDiscussPostMapper(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }

    @Override
    public List<DiscussPost> getAll() {
        DiscussPostExample discussPostExample = null;
        return discussPostMapper.selectByExample(null);
    }
}
