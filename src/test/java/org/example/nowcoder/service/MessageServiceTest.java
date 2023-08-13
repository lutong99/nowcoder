package org.example.nowcoder.service;

import org.example.nowcoder.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageService messageService;

    @Test
    void getConversations() {
        List<Message> conversations = messageService.getConversations(111);
        conversations.forEach(System.out::println);
        int size = conversations.size();
        System.out.println("size = " + size);
    }

    @Test
    void getConversationCount() {
        int conversationCount = messageService.getConversationCount(111);
        System.out.println("conversationCount = " + conversationCount);
    }

    @Test
    void getLetters() {
        List<Message> letters = messageService.getMessagesByConversationId("111_112");
        letters.forEach(System.out::println);
    }

    @Test
    void getLettersCount() {
        int lettersCount = messageService.getMessageCount("111_112");
        System.out.println("lettersCount = " + lettersCount);

    }

    @Test
    void getLettersUnread() {
        int lettersUnread = messageService.getMessageUnreadCount(131, "111_131");
        System.out.println("lettersUnread = " + lettersUnread);
    }
}