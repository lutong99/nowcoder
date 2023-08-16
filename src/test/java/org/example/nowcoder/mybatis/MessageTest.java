package org.example.nowcoder.mybatis;

import org.example.nowcoder.entity.Message;
import org.example.nowcoder.entity.MessageExample;
import org.example.nowcoder.mapper.MessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MessageTest {


    @Autowired
    MessageMapper messageMapper;

    @Test
    public void testMapSelectList() {

        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andIdEqualTo(9888);
        List<Message> messages = messageMapper.selectByExample(messageExample);
        System.out.println("messages = " + messages);
        System.out.println(messages == null);

    }


}
