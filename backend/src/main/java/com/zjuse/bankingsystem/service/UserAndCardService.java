package com.zjuse.bankingsystem.service;

import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.cardType;
import com.zjuse.bankingsystem.mapper.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.management.Query;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.protobuf.Api;
import com.zjuse.bankingsystem.entity.*;;



@Service
public class UserAndCardService {
    @Autowired
    UserPrivilegeMapper userprivilegeMapper;
    
    @Autowired
    CardOfPersonMapper cardOfPersonMapper;
    @Autowired
    BlacklistMapper blacklistMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    CreditcardService creditcardService;
    @Autowired
    DebitcardService debitcardService;
    @Autowired
    BlacklistService blacklistService;
    public ApiResult bindUserAndCard(Long cardId, String id_number) {
        try {
            ApiResult apiResult =  userService.getUserId(id_number);
            if (apiResult.ok == false) {
                System.out.println(apiResult.message);
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

    public ApiResult getAllCard(Long userId) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_id", userId);
            List<CardOfPerson> list = cardOfPersonMapper.selectList(wrapper);
            if (list == null) {
                return new ApiResult(false, "database error");
            }
            List<Long> cardList = new Vector<Long>();
            for (var item : list) {
                cardList.add(item.getCardId());
            }

            ApiResult apiResult = new ApiResult(true, "success");
            apiResult.payload = cardList;
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult getAllCard(String id_number) {
        try {
            ApiResult apiResult =  userService.getUserId(id_number);
            if (apiResult.ok == false) {
                return apiResult;
            }
            Long userId = (Long) apiResult.payload;
            return getAllCard(userId);
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult consume(Long cardId, BigDecimal amount, String password) {
        try {
            if (amount.doubleValue() < 0) {
                return new ApiResult(false, "No negetive amout");
            }
            // check priviledge? I don't know
            if (getCardType(cardId) == cardType.CREDIT_CARD) {
                ApiResult apiResult = creditcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                return new ApiResult(true, "success");
            }
            else {
                ApiResult apiResult = debitcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                return new ApiResult(true, "success");
            }
        }
        
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult loss(Long userId, Long cardId, String password) {
        try {

            QueryWrapper wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.eq("card_id",cardId);
            if (cardOfPersonMapper.selectCount(wrapper) == 0) {
                return new ApiResult(false, "card not belong to user");
            }
            if (getCardType(cardId) == cardType.CREDIT_CARD) {
                ApiResult apiResult = creditcardService.loss(cardId, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                return new ApiResult(true, "success");
            }
            else {
                ApiResult apiResult = debitcardService.loss(cardId, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                return new ApiResult(true, "success");
            }
        }
        
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult history(Long userId) {
        try {
            ApiResult apiResult = getAllCard(userId);
            if (apiResult.ok == false) {
                return apiResult;
            }
            List<Long> cardList = (List<Long>) apiResult.payload;
            List<History> histories = new Vector<History>();
            for(var item : cardList) {
                if (getCardType(item) == cardType.CREDIT_CARD) {
                    apiResult = creditcardService.getHistory(item);
                    if (apiResult.ok == false) {
                        return apiResult;
                    }
                }
                else {
                    apiResult = debitcardService.getHistory(item);
                    if (apiResult.ok == false) {
                        return apiResult;
                    }
                }
                List<History> cardHistories = (List<History>) apiResult.payload;
                histories.addAll(cardHistories);
            }
            apiResult = new ApiResult(true,"success");
            apiResult.payload = histories;
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

    private void Rollback(Long cardId, BigDecimal amount) throws Exception {
        if (getCardType(cardId) == cardType.CREDIT_CARD) {
            ApiResult apiResult = creditcardService.increaceBalance(cardId, amount);
        }
        else {
            ApiResult apiResult = debitcardService.increaceBalance(cardId, amount);
        }

    }

    public ApiResult transfor(Long cardId, Long targetCardId, BigDecimal amount, String password) {
        boolean isDec = false;
        try {
            if (amount.doubleValue() < 0) {
                return new ApiResult(false, "No negetive amout");
            }
            if (cardId == targetCardId) {
                return new ApiResult(false, "can't transfor to same card");
            }
            ApiResult apiResult;
            // check priviledge? I don't know
            if (getCardType(cardId) == cardType.CREDIT_CARD) {
                apiResult = creditcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                isDec = true;
            }
            else {
                apiResult = debitcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                isDec = true;
            }

            if (getCardType(targetCardId) == cardType.CREDIT_CARD) {
                apiResult = creditcardService.increaceBalance(cardId, amount);
                if (apiResult.ok == false) {
                    Rollback(cardId, amount);
                    return apiResult;
                }
            }
            else {
                apiResult = debitcardService.increaceBalance(cardId, amount);
                if (apiResult.ok == false) {
                    Rollback(cardId, amount);
                    return apiResult;
                }

            }
            return new ApiResult(true, "success");
        }
        catch (Exception e) {
            if (isDec) {
                try {
                    Rollback(cardId, amount);
                }
                catch (Exception eInn) {
                    return new ApiResult(false, "Rollback Error");
                }
            }
            return new ApiResult(false, e.getMessage());
        }
    }

    public cardType getCardType(Long cardId) throws Exception {
        if (cardId > 50000000000000000l) return cardType.CREDIT_CARD;
        else return cardType.DEBIT_CARD;
    }

}
