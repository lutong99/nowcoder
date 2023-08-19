package org.example.nowcoder.quartz;

import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.constant.DiscussPostConstant;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.service.DiscussPostService;
import org.example.nowcoder.service.ElasticsearchService;
import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

@Slf4j
public class PostScoreRefreshJob implements Job, CommunityConstant, DiscussPostConstant {

    private static final Date epoch = new Date(1408032000000L); // 2014-08-15 00:00:00
    private RedisTemplate<String, Object> redisTemplate;

    private DiscussPostService discussPostService;

    private LikeService likeService;

    private ElasticsearchService<DiscussPost> elasticsearchService;

    @Autowired
    public void setElasticsearchService(ElasticsearchService<DiscussPost> elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String postScoreKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(postScoreKey);

        if (operations.size() != null && operations.size() == 0) {
            log.info("[刷新分数任务取消] 没有要刷新的帖子");
            return;
        }

        log.info("[刷新分数任务准备] 刷新{} 个帖子", operations.size());
        while (operations.size() > 0) {
            Integer postId = (Integer) operations.pop();
            log.info("[刷新分数任务开始] 正在刷新帖子, postId: {}", postId);
            this.refresh(postId);
            log.info("[刷新分数任务开始] 刷新帖子成功, postId: {}", postId);
        }
        log.info("[刷新分数任务结束] ");
    }

    private void refresh(Integer postId) {
        DiscussPost post = discussPostService.getById(postId);
        if (post == null) {
            log.error("该帖子不存在，postId = {}", postId);
            return;
        }
        Long likeCount = likeService.likeCount(CommentConstant.ENTITY_TYPE_POST, postId);
        Integer commentCount = post.getCommentCount();
        double highlight = post.getStatus() == STATUS_HIGHLIGHT ? 75.0 : 0.0;
        double preScore = highlight + commentCount * 10.0 + likeCount * 2.0;
        double score = Math.log10(Math.max(preScore, 0)) + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24.0); // 减去发表时间距离天数
        post.setScore(score);
        discussPostService.updateById(post);
        elasticsearchService.save(post);
    }
}
