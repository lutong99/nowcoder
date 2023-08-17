package org.example.nowcoder.elasticsearch;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.mapper.elasticsearch.DiscussPostRepository;
import org.example.nowcoder.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ElasticsearchTests {


    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    DiscussPostRepository discussPostRepository;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Test
    public void testSave() {

        List<DiscussPost> all = discussPostService.getAll();
        Iterable<DiscussPost> discussPosts = discussPostRepository.saveAll(all);
        discussPosts.forEach(System.out::println);

    }

    @Test
    public void testDelete() {

        discussPostRepository.deleteById(265);

    }

    @Test
    public void testUpdate() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setId(272);
        discussPost.setContent("我的天呀，我改了");
        discussPostRepository.save(discussPost);
    }

    @Test
    public void testSearch() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .
                withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "content", "title"))
                .withSorts(
                        SortBuilders.fieldSort("type").order(SortOrder.DESC),
                        SortBuilders.fieldSort("score").order(SortOrder.DESC),
                        SortBuilders.fieldSort("createTime").order(SortOrder.DESC)
                ).withPageable(PageRequest.of(2, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                )
                .build();
        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        List<DiscussPost> discussPostList = new ArrayList<>();
        for (SearchHit<DiscussPost> searchHit : searchHits) {
            List<String> content = searchHit.getHighlightField("content");
            List<String> title = searchHit.getHighlightField("title");
            DiscussPost discussPost = searchHit.getContent();
            discussPost.setContent(content.get(0));
            discussPost.setTitle(title.get(0));
            discussPostList.add(discussPost);

        }
        discussPostList.forEach(System.out::println);
        Pageable pageable = searchQuery.getPageable();
        System.out.println("pageable = " + pageable.first());
        System.out.println("pageable.getPageSize() = " + pageable.getPageSize());
        System.out.println("search.getTotalHits() = " + search.getTotalHits());


    }


}
