package org.example.nowcoder.entity.vo;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

@Data
public class PageInfo<T> {

    private Pageable pageable;

    /**
     * 是否为第一页
     */
    private boolean isFirstPage = false;
    /**
     * 是否为最后一页
     */
    private boolean isLastPage = false;
    /**
     * 是否有前一页
     */
    private boolean hasPreviousPage = false;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage = false;

    /**
     * 所有导航页号
     */
    private int[] navigatepageNums;

    /**
     * 前一页
     */
    private int prePage;
    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 当前页
     */
    private int pageNum;
    /**
     * 每页的数量
     */
    private int pageSize;

    private int pages;

    private SearchHits<T> searchHits;

    public PageInfo(Pageable pageable, SearchHits<T> searchHits) {
        this.pageable = pageable;
        this.searchHits = searchHits;
        this.pageSize = pageable.getPageSize();
        this.pageNum = pageable.getPageNumber() + 1;
        this.isFirstPage = pageable.getPageNumber() == 0;
        this.pages = (int) (searchHits.getTotalHits() + pageSize - 1) / pageSize;
        this.isLastPage = pages == pageNum;
        this.hasPreviousPage = pageable.getPageNumber() > 0;
        this.hasNextPage = !isLastPage;
        this.prePage = Math.max(this.pageNum - 1, 0);
        this.nextPage = pageNum + 1 > pages ? pageNum : pageNum + 1;
        calcNavigatepageNums();
        calcPage();
        judgePageBoudary();
    }

    private void calcNavigatepageNums() {
        int navigatePages = 5;
        //当总页数小于或等于导航页码数时
        if (pages <= navigatePages) {
            navigatepageNums = new int[pages];
            for (int i = 0; i < pages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = pageNum - navigatePages / 2;
            int endNum = pageNum + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > pages) {
                endNum = pages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
    }


    /**
     * 计算前后页，第一页，最后一页
     */
    private void calcPage() {
        if (navigatepageNums != null && navigatepageNums.length > 0) {
            if (pageNum > 1) {
                prePage = pageNum - 1;
            }
            if (pageNum < pages) {
                nextPage = pageNum + 1;
            }
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = pageNum == 1;
        isLastPage = pageNum == pages || pages == 0;
        hasPreviousPage = pageNum > 1;
        hasNextPage = pageNum < pages;
    }

}
