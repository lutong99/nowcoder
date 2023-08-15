package org.example.nowcoder.api;


import com.github.pagehelper.PageInfo;
import org.example.nowcoder.util.CommunityUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PageHelperTests {

    @Test
    public void testListPage() {
//        Page<String> strings = PageHelper.startPage(1, 10);
        List<String> stringList = getStringList();
        PageInfo<String> stringPageInfo = new PageInfo<>(stringList, 5);
        int nextPage = stringPageInfo.getNextPage();


    }

    private List<String> getStringList() {

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String string = CommunityUtil.generateUUID();
            stringList.add(string);
        }

        return stringList;
    }

}
