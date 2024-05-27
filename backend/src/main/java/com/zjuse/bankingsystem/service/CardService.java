package com.zjuse.bankingsystem.service;

import java.util.List;
import java.util.Vector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.Card;
import com.zjuse.bankingsystem.entity.CardOfPerson;
import com.zjuse.bankingsystem.entity.UserPrivilege;
import com.zjuse.bankingsystem.mapper.CardMapper;
import com.zjuse.bankingsystem.mapper.CardOfPersonMapper;
import com.zjuse.bankingsystem.mapper.UserPrivilegeMapper;
import com.zjuse.bankingsystem.mapper.CardMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;

@Service
public class CardService {
    @Autowired
    CardMapper cardMapper;

    @Autowired
    BlacklistService blacklistService;

    @Autowired
    UserService userService;

    @Autowired
    CardOfPersonMapper cardOfPersonMapper;

    public ApiResult registerCard(CardType cardType) {
        try {
            Card card = new Card(null, cardType);
            cardMapper.insert(card);
            ApiResult apiResult = new ApiResult(true, "success", card.getCardId());
            return apiResult;
        } catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public CardType getCardType(Long cardId) throws Exception {
        QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId);
        Card card = cardMapper.selectOne(queryWrapper);
        if (card == null) {
            // return new ApiResult(false, "card not found");
            throw new Exception("card not found");
        }
        return card.getCardType();
    }


    public ApiResult bindUserAndCard(Long cardId, String id_number) {
        try {
            ApiResult apiResult =  userService.getUserId(id_number);
            if (apiResult.ok == false) {
                return apiResult;
            }
            Long userId = (Long) apiResult.payload;

            apiResult = blacklistService.isInblacklist(userId);
            if (apiResult.ok == false) {
                return apiResult;
            }
            Boolean isInBlacklist = (boolean) apiResult.payload;
            if (isInBlacklist) {
                return new ApiResult(false, "user is in blacklist");
            }

            CardOfPerson cardOfPerson = new CardOfPerson();
            cardOfPerson.setCardId(cardId);
            cardOfPerson.setUserId(userId);
            cardOfPersonMapper.insert(cardOfPerson);
            // cardOfPersonMapper

            return new ApiResult(true, "success");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult getAllCardbyUserId(Long userId) {
        try {
            // ApiResult apiResult = userService.getUserByUsername(username);
            // if (apiResult.ok == false) {
            //     return apiResult;
            // }
            // Long userId = (Long) apiResult.payload;
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_id", userId);
            List<CardOfPerson> list = cardOfPersonMapper.selectList(wrapper);
            if (list == null) {
                return new ApiResult(false, "database error");
            }
            List<Card> cardList = new Vector<Card>();
            for (var item : list) {
                CardType cardType = getCardType(item.getCardId());
                cardList.add(new Card(item.getCardId(), cardType));
            }
            ApiResult apiResult = new ApiResult(true, "success", cardList);
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }



    public ApiResult getAllCardbyUsername(String username) {
        try {
            ApiResult apiResult = userService.getUserByUsername(username);
            if (apiResult.ok == false) {
                return apiResult;
            }
            Long userId = (Long) apiResult.payload;
            return getAllCardbyUserId(userId);
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult getAllCardbyIdNunber(String id_number) {
        try {
            ApiResult apiResult =  userService.getUserId(id_number);
            if (apiResult.ok == false) {
                return apiResult;
            }
            Long userId = (Long) apiResult.payload;
            return getAllCardbyUserId(userId);
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

}
