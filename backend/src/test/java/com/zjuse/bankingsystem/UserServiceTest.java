package com.zjuse.bankingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
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
import com.zjuse.bankingsystem.entity.History;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.entity.UserPrivilege;
import com.zjuse.bankingsystem.mapper.BlacklistMapper;
import com.zjuse.bankingsystem.mapper.UserMapper;
import com.zjuse.bankingsystem.service.BlacklistService;
import com.zjuse.bankingsystem.service.CardService;
import com.zjuse.bankingsystem.service.EmailViladService;
import com.zjuse.bankingsystem.service.UserAndCardService;
import com.zjuse.bankingsystem.service.UserPrivilegeService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;

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

    @Autowired 
    CardService cardService;

    @Autowired
    EmailViladService emailViladService;

    User RandomUser() {
        User user = new User();
        Random random = new Random();
        user.setUsername("test" + Integer.toString(random.nextInt(100000)));
        user.setIdNumber(Integer.toString(random.nextInt(10000000)));
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

    void InitCardAndPerson(List<User> userlist, List<Long> cardList, Map<CardOfPerson, Integer> relation,Integer N, Integer M) {
        for(int i = 0; i < N; i++) {
            try {
                User user = RandomUser();
                userMapper.insert(user);
                userlist.add(user);
            }
            catch (Exception e) {
                System.out.println("system out " + e.getMessage());
            }
        }
        for(Long i = 1L; i <= M; i++) {
            Random random = new Random();
            ApiResult apiResult = cardService.registerCard(CardType.CREDIT_CARD);
            assertTrue(apiResult.ok);
            Long cardId = (Long) apiResult.payload;
            cardList.add(cardId);
            Integer index = random.nextInt(N);
            assertTrue(cardService.bindUserAndCard(i, userlist.get(index).getIdNumber()).ok);
            CardOfPerson cardOfPerson = new CardOfPerson(cardId, userlist.get(index).getId());
            relation.put(cardOfPerson, 1);
        }
    }

    @Test
    void BindCardTest() {
        Integer N = 10, M = 20;
        List<User> list = new Vector<>();
        List<Long> cardList = new Vector<>();
        Map<CardOfPerson, Integer> map = new HashMap<CardOfPerson, Integer>();
        InitCardAndPerson(list, cardList, map, N, M);
        Long total = 0L;
        for(User user : list) {
            ApiResult apiResult = cardService.getAllCardbyUserId(user.getId());
            assertTrue(apiResult.ok);
            List<Long> usercard = (List<Long>) apiResult.payload;
            for(Long item : usercard) {
                CardOfPerson cardOfPerson = new CardOfPerson();
                cardOfPerson.setCardId(item);
                cardOfPerson.setUserId(user.getId());
                assertTrue(map.containsKey(cardOfPerson));
            }
            total += usercard.size();
        }
        assertEquals(M, total);
    }

    @Test
    void TransferTest() {
        Integer N = 10, M = 20;
        List<User> list = new Vector<>();
        List<Long> cardList = new Vector<>();
        Map<CardOfPerson, Integer> map = new HashMap<CardOfPerson, Integer>();
        InitCardAndPerson(list, cardList, map, N, M);
        Random random = new Random();
        for(int i = 1; i <= M; i++) {
            Long cardId = cardList.get(i - 1);
            Integer index = random.nextInt(N);
            Long targetId = list.get(index).getId();
            ApiResult apiResult = userAndCardService.transfor(cardId, targetId, new BigDecimal("12342134"), "12342314", "transfer" + i);
            if (targetId == cardId) {
                assertFalse(apiResult.ok);
                
            }
            else {
                assertTrue(apiResult.ok);
                apiResult = userAndCardService.history(cardId);
                assertTrue(apiResult.ok);
                List<History> history = (List<History>) apiResult.payload;
                boolean flag = false;
                for(History item : history) {
                    if (item.getTargetCard() == targetId && item.getRemark().equals("transfer" + i)) {
                        flag = true;
                        break;
                    }
                }
                assertTrue(flag);
            }
        }
    }


    @Test
    void EmailSendTest() {
        ApiResult apiResult = emailViladService.sendEmail("XuanyiZhou000@outlook.com", null);
        System.out.println("### " + apiResult.message);
        assertTrue(apiResult.ok);
        String uuid = (String) apiResult.payload;
        System.out.println("### " + uuid.hashCode());

        String code = "123456";
        assertTrue(emailViladService.validCode(uuid, "XuanyiZhou000@outlook.com", code).ok);
        apiResult = emailViladService.sendEmail("XuanyiZhou000@outlook.com", uuid);
        System.out.println("### " + apiResult.message);
        assertFalse(apiResult.ok);
    }
}