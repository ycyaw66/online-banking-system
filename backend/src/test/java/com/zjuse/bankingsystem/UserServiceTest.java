package com.zjuse.bankingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import org.assertj.core.api.AssertJProxySetup;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zjuse.bankingsystem.entity.Blacklist;
import com.zjuse.bankingsystem.entity.CardOfPerson;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.entity.UserPrivilege;
import com.zjuse.bankingsystem.mapper.BlacklistMapper;
import com.zjuse.bankingsystem.mapper.UserMapper;
import com.zjuse.bankingsystem.service.BlacklistService;
import com.zjuse.bankingsystem.service.UserAndCardService;
import com.zjuse.bankingsystem.service.UserPrivilegeService;
import com.zjuse.bankingsystem.utils.ApiResult;

@SpringBootTest
@MapperScan("com.zjuse.bankingsystem.mapper")
public class UserServiceTest {
    
    @Autowired
    UserMapper userMapper;

    @Autowired
    BlacklistService blacklistService;

    @Autowired
    UserPrivilegeService userPrivilegeService;

    @Autowired
    UserAndCardService userAndCardService;

    User RandomUser() {
        User user = new User();
        Random random = new Random();
        user.setUsername("test" + Integer.toString(random.nextInt(100000)));
        user.setId_number(Integer.toString(random.nextInt(10000000)));
        user.setEmail("sadfsdfa");
        user.setPassword("34321413242134");
        return user;
    }
    
    @Test
    List<User> InsertTest(Integer N) {
        // System.out.println("InsertTest");
        List<User> users = new Vector<User>();
        for(int i = 0; i < N; i++) {
            try {
                User user = RandomUser();
                userMapper.insert(user);
                users.add(user);
            }
            catch (Exception e) {
                System.out.println("system out " + e.getMessage());

            }
        }
        assertTrue(true);
        return users;
    }

    @Test
	void InsertBlackList() {
		InsertTest(10);
		for(Long i = 1L; i <= 5L; i++) {
            ApiResult apiResult = blacklistService.addBlacklist(i, "test");
			assertTrue(apiResult.ok);
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

    @Test
    void PrivilegeTest() {
        InsertTest(10);
        for(Long i = 1L; i <= 5L; i++) {
            UserPrivilege userPrivilege = new UserPrivilege(i, true, true, true);
            assertTrue(userPrivilegeService.insertUserPrivilege(userPrivilege).ok);
        }
        for(Long i = 1L; i <= 5L; i++) {
            ApiResult apiResult = userPrivilegeService.getUserPrivilege(i);
            assertTrue(apiResult.ok);
            UserPrivilege userPrivilege = (UserPrivilege) apiResult.payload;
            assertTrue(userPrivilege.isLoss());
            assertTrue(userPrivilege.isTranscations());
            assertTrue(userPrivilege.isTransfer());
            userPrivilege.setLoss(false);
            assertTrue(userPrivilegeService.modifyUserPrivilege(userPrivilege).ok);
        }
        for(Long i = 1L; i <= 5L; i++) {
            ApiResult apiResult = userPrivilegeService.getUserPrivilege(i);
            assertTrue(apiResult.ok);
            UserPrivilege userPrivilege = (UserPrivilege) apiResult.payload;
            assertFalse(userPrivilege.isLoss());
        }

        UserPrivilege userPrivilege = new UserPrivilege(15L, true, true, true);
        assertFalse(userPrivilegeService.insertUserPrivilege(userPrivilege).ok);
        assertFalse(userPrivilegeService.modifyUserPrivilege(userPrivilege).ok);
        assertFalse(userPrivilegeService.getUserPrivilege(13L).ok);
    }

    @Test
    void BindCardTest() {
        List<User> list = InsertTest(10);
        Long N0 = 50000000000000000L;
        Map<CardOfPerson, Integer> map = new HashMap<CardOfPerson, Integer>();
        Long M = 20L;
        for(Long i = 1L; i <= M; i++) {
            Random random = new Random();
            Long cardId = i;
            Integer index = random.nextInt(10);
            assertTrue(userAndCardService.bindUserAndCard(i, list.get(index).getId_number()).ok);
            CardOfPerson cardOfPerson = new CardOfPerson();
            cardOfPerson.setCardId(cardId);
            cardOfPerson.setUserId(list.get(index).getId());
            map.put(cardOfPerson, 1);
        }
        Long total = 0L;
        for(User user : list) {
            ApiResult apiResult = userAndCardService.getAllCard(user.getId());
            assertTrue(apiResult.ok);
            List<Long> cardList = (List<Long>) apiResult.payload;
            for(Long item : cardList) {
                CardOfPerson cardOfPerson = new CardOfPerson();
                cardOfPerson.setCardId(item);
                cardOfPerson.setUserId(user.getId());
                assertTrue(map.containsKey(cardOfPerson));
            }
            total += cardList.size();
        }
        assertEquals(M, total);
    }


}
