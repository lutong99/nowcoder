package org.example.nowcoder.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ObjectMapperTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Test
    public void testObjectMapper() throws JsonProcessingException {
        User user = userService.getById(111);
        String jsonValue = objectMapper.writeValueAsString(user);
        System.out.println("jsonValue = " + jsonValue);
    }

}
