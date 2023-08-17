package org.example.nowcoder.controller;

import org.example.nowcoder.constant.CommentConstant;
import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.vo.PageInfo;
import org.example.nowcoder.service.ElasticsearchService;
import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {

    private UserService userService;

    private LikeService likeService;
    private ElasticsearchService<DiscussPost> elasticsearchService;

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setElasticsearchService(ElasticsearchService<DiscussPost> elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam("keyword") String keyword,
                         @RequestParam(value = "num", defaultValue = PAGE_OFFSET) Integer pageNum,
                         @RequestParam(value = "size", defaultValue = PAGE_SIZE_POST) Integer pageSize
    ) {
        NativeSearchQuery nativeSearchQuery = elasticsearchService.getNativeSearchQuery(keyword, pageNum - 1, pageSize);
        PageInfo<DiscussPost> pageInfo = elasticsearchService.getPageInfo(nativeSearchQuery);
        SearchHits<DiscussPost> searchHits = pageInfo.getSearchHits();
        List<Map<String, Object>> discussPostMapList = new ArrayList<>();
        if (searchHits != null) {
            for (SearchHit<DiscussPost> searchHit : searchHits) {

                Map<String, Object> discussPostMap = new HashMap<>();
                DiscussPost discussPost = searchHit.getContent();
                List<String> content = searchHit.getHighlightField("content");
                if (content.size() > 0)
                    discussPost.setContent(content.get(0));

                List<String> title = searchHit.getHighlightField("title");
                if (title.size() > 0)
                    discussPost.setTitle(title.get(0));
                discussPostMap.put("post", discussPost);
                discussPostMap.put("user", userService.getById(discussPost.getUserId()));
                discussPostMap.put("likeCount", likeService.likeCount(CommentConstant.ENTITY_TYPE_POST, discussPost.getId()));
                discussPostMapList.add(discussPostMap);

            }
        }
        model.addAttribute("page", pageInfo);
        model.addAttribute("postMapList", discussPostMapList);
        model.addAttribute("keyword", keyword);
        return "site/search";
    }


}
