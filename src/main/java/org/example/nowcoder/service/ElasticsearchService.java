package org.example.nowcoder.service;

import org.example.nowcoder.entity.vo.PageInfo;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

public interface ElasticsearchService<T> {

    void save(T t);

    void delete(T t);

    NativeSearchQuery getNativeSearchQuery(String keyword, int offset, int limit);

    PageInfo<T> getPageInfo(NativeSearchQuery query);

}
