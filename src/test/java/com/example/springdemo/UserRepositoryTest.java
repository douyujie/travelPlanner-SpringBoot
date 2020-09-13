package com.example.springdemo;

import com.example.springdemo.models.User;
import com.example.springdemo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    /**
     * 保存person到数据库
     */
    @Before
    public void setUp() {
        assertNotNull(userRepository);
        User user = new User("YujieDou", "yujiedou@usc.edu", "password");
        userRepository.saveAndFlush(user);
    }

    /**
     * 使用 JPA 自带的方法查找 person
     */
    @Test
    public void should_get_person() {
        assertTrue(userRepository.existsByUsername("YujieDou"));
        assertTrue(userRepository.existsByEmail("yujiedou@usc.edu"));

        // 清空数据库
        userRepository.deleteAll();
    }
}
