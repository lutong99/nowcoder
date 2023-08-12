package org.example.nowcoder.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveFilterTest {
    private SensitiveFilter filter;

    @Autowired
    public void setFilter(SensitiveFilter filter) {
        this.filter = filter;
    }

    @Test
    public void test() {
        String text = "%%%做&*……&爱#@不到3秒男子便@身#￥%￥寸@%￥了@";
        text = filter.filter(text);
        System.out.println("text = " + text);
        text = "做爱不到3秒男子便身寸了";
        text = filter.filter(text);
        System.out.println("text = " + text);

    }
}
