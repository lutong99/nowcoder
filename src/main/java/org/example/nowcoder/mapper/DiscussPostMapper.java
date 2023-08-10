package org.example.nowcoder.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.DiscussPostExample;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface DiscussPostMapper {
    int countByExample(DiscussPostExample example);

    int deleteByExample(DiscussPostExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DiscussPost record);

    int insertSelective(DiscussPost record);

    List<DiscussPost> selectByExampleWithBLOBs(DiscussPostExample example);

    List<DiscussPost> selectByExample(DiscussPostExample example);

    DiscussPost selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    int updateByExampleWithBLOBs(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    int updateByExample(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    int updateByPrimaryKeySelective(DiscussPost record);

    int updateByPrimaryKeyWithBLOBs(DiscussPost record);

    int updateByPrimaryKey(DiscussPost record);
}