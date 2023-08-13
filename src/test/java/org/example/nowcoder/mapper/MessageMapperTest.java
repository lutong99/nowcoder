package org.example.nowcoder.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootTest
public class MessageMapperTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageMapper messageMapper;

    @Test
    public void testGetNewestIds() {
        List<Integer> newestMessageIds = messageMapper.getNewestMessageIds(111);
        newestMessageIds.forEach(System.out::println);
    }

}
