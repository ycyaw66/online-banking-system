package com.zjuse.bankingsystem;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.assertj.core.api.AssertJProxySetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zjuse.bankingsystem.entity.Blacklist;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.mapper.BlacklistMapper;
import com.zjuse.bankingsystem.mapper.UserMapper;
import com.zjuse.bankingsystem.service.BlacklistService;
import com.zjuse.bankingsystem.utils.ApiResult;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    UserMapper userMapper;

    @Autowired
    BlacklistService blacklistService;

    User RandomUser() {
        User user = new User();
        Random random = new Random();
        user.setName("test" + Integer.toString(random.nextInt(100000)));
        user.setIdentityNumber(Integer.toString(random.nextInt(10000000)));
        user.setAge(20);
        user.setEmail("sadfsdfa");
        return user;
    }
    @Test
    List<User> InsertTest() {
        // System.out.println("InsertTest");
        List<User> users = new Vector<User>();
        for(int i = 0; i < 5; i++) {
            User user = RandomUser();
            userMapper.insert(user);
            users.add(user);
        }
        assertTrue(true);
        return users;
    }

    @Test
	void InsertBlackList() {
		InsertTest();
		for(Long i = 1L; i <= 5L; i++) {
			assertTrue(blacklistService.addBlacklist(i, "test").ok);
		}
		ApiResult apiResult = blacklistService.getBlacklist();
		assertTrue(apiResult.ok);
		List<Blacklist> list = (List<Blacklist>) apiResult.payload;
		assertTrue(list.size() == 5);
		for(Long i = 1L; i <= 5L; i++) {
			assertTrue(blacklistService.isInblacklist(i).ok);
			assertTrue((boolean)blacklistService.isInblacklist(i).payload);
		}
		for(Long i = 1L; i <= 5L; i++) {
			assertTrue(blacklistService.removeBlacklist(i).ok);
		}
		for(Long i = 1L; i <= 5L; i++) {
			assertTrue(blacklistService.isInblacklist(i).ok);
			assertFalse((boolean)blacklistService.isInblacklist(i).payload);
		}

	}


}
