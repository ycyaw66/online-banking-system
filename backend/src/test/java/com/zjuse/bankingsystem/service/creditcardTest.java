package com.zjuse.bankingsystem.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zjuse.bankingsystem.entity.Card;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.entity.creditCard.CreditCard;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardApplication;
import com.zjuse.bankingsystem.utils.ApiResult;

import cn.hutool.core.lang.Assert;

@SpringBootTest
@MapperScan("com.zjuse.bankingsystem.mapper")
public class creditcardTest {
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    UserService userService;
    @Autowired
    CardService cardService;

    @Autowired
    UserAndCardService userAndCardService;
    
    final Integer N = 10;

    User RandomUser() {
        User user = new User();
        Random random = new Random();
        user.setUsername("test" + Integer.toString(random.nextInt(100000)));
        user.setIdNumber(Integer.toString(random.nextInt(10000000)));
        user.setEmail("sadfsdfa" + random.nextInt(100000) + "@qq.com");
        user.setPassword("34321413242134");
        return user;
    }


    List<User> Insert() {
        // System.out.println("InsertTest");
        List<User> users = new Vector<User>();
        for(int i = 0; i < N; i++) {
            User user = RandomUser();
            assertTrue(userService.registerNewUser(user).ok);
            users.add(user);
        }
        return users;
    }

    @Test
    void InsertTest() {
        List<User> users = new Vector<User>();
        for(int i = 0; i < N; i++) {
            User user = RandomUser();
            assertTrue(userService.registerNewUser(user).ok);
            users.add(user);
        }
    }

    @Test
    void CardTest() {
        List<User> user = Insert();
        for(User item : user) {
            creditCardService.addNewCreditCardRequest(item.getIdNumber(), new BigDecimal(1000), "123456");
        }
        ApiResult apiResult = creditCardService.queryRequestsByInspector(2);
        assertTrue(apiResult.ok);
        List<CreditCardApplication> creditCardApplication = (List<CreditCardApplication>) apiResult.payload;
        for(CreditCardApplication item : creditCardApplication) {
            assertTrue(creditCardService.acceptRequest(item.getId()).ok);
        }
        Long pre = null;
        for(User item : user) {
            ApiResult apiResult1 = cardService.getAllCardbyUserId(item.getId());
            assertTrue(apiResult1.ok);
            List<Card> creditCards = (List<Card>) apiResult1.payload;
            for(Card item1 : creditCards) {
                if (pre != null) {
                    assertTrue(userAndCardService.transfor(item1.getCardId(), pre, new BigDecimal(50), "123456", "??").ok);
                }
                Date date = new Date();
                assertTrue(creditCardService.bankPay(item1.getCardId(), "123456", new BigDecimal(100), date).ok);
                ApiResult apiResult2 = userAndCardService.consume(item1.getCardId(), new BigDecimal(10), "123456", "??");
                System.out.println(apiResult2.message);
                assertTrue(apiResult2.ok);
                pre = item1.getCardId();
            }
        }

        for(User item : user) {
            ApiResult apiResult1 = cardService.getAllCardbyUserId(item.getId());
            assertTrue(apiResult1.ok);
            List<Card> creditCards = (List<Card>) apiResult1.payload;
            for(Card item1 : creditCards) {
                assertTrue(userAndCardService.loss(item1.getCardId(), "123456").ok);
                assertFalse(userAndCardService.consume(item1.getCardId(), new BigDecimal(10), "123456", "??").ok);
            }
        }
        
    }
}
