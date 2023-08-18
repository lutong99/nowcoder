package org.example.nowcoder.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.example.nowcoder.entity.DiscussPost;
import org.example.nowcoder.entity.vo.PageInfo;
import org.example.nowcoder.mapper.elasticsearch.DiscussPostRepository;
import org.example.nowcoder.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService<DiscussPost> {

    private DiscussPostRepository discussPostRepository;
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    public void setDiscussPostRepository(DiscussPostRepository discussPostRepository) {
        this.discussPostRepository = discussPostRepository;
    }

    @Autowired
    public void setElasticsearchRestTemplate(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    @Override
    public void save(DiscussPost t) {
        discussPostRepository.save(t);
    }

    @Override
    public void delete(DiscussPost t) {
        discussPostRepository.delete(t);
    }

    @Override
    public void delete(Integer postId) {
        discussPostRepository.deleteById(postId);
    }

    public SearchHits<DiscussPost> search(NativeSearchQuery searchQuery) {
        return elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
    }

    @Override
    public NativeSearchQuery getNativeSearchQuery(String keyword, int offset, int limit) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withSorts(
                        SortBuilders.fieldSort("type").order(SortOrder.DESC),
                        SortBuilders.fieldSort("score").order(SortOrder.DESC),
                        SortBuilders.fieldSort("createTime").order(SortOrder.DESC)
                ).withPageable(PageRequest.of(offset, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                );
        if (!StringUtils.isBlank(keyword)) {
            nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keyword, "content", "title"));
        }
        return nativeSearchQueryBuilder.build();
    }

    @Override
    public PageInfo<DiscussPost> getPageInfo(NativeSearchQuery searchQuery) {
        Pageable pageable = searchQuery.getPageable();
        SearchHits<DiscussPost> search = search(searchQuery);
        return new PageInfo<>(pageable, search);
    }
}
