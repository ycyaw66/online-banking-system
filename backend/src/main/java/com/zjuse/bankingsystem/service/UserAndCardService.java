package com.zjuse.bankingsystem.service;

import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.cardType;
import com.zjuse.bankingsystem.mapper.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.management.Query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.protobuf.Api;
import com.zjuse.bankingsystem.entity.*;;

public class UserAndCardService {
    static UserPrivilegeMapper userprivilegeMapper;
    static CardOfPersonMapper cardOfPersonMapper;
    static BlacklistMapper blacklistMapper;
    static UserMapper userMapper;
    static public ApiResult bindUserAndCard(Long cardId, String identityNumber) {
        try {
            ApiResult apiResult =  UserService.getUserId(identityNumber);
            if (apiResult.ok == false) {
                return apiResult;
            }
            Long userId = (Long) apiResult.payload;

            apiResult = BlacklistService.isInblacklist(userId);
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
            return new ApiResult(false, e.getMessage());
        }
    }

    static public ApiResult getAllCard(Long userId) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("userId", userId);
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

    static public ApiResult getAllCard(String identityNumber) {
        try {
            ApiResult apiResult =  UserService.getUserId(identityNumber);
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

    static public ApiResult consume(Long cardId, BigDecimal amount, String password) {
        try {
            if (amount.doubleValue() < 0) {
                return new ApiResult(false, "No negetive amout");
            }
            // check priviledge? I don't know
            if (getCardType(cardId) == cardType.CREDIT_CARD) {
                ApiResult apiResult = CreditcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                return new ApiResult(true, "success");
            }
            else {
                ApiResult apiResult = DebitcardService.decreaceBalance(cardId, amount, password);
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

    static public ApiResult loss(Long userId, Long cardId, String password) {
        try {

            QueryWrapper wrapper = new QueryWrapper<>();
            wrapper.eq("userId", userId);
            wrapper.eq("cardId",cardId);
            if (cardOfPersonMapper.selectCount(wrapper) == 0) {
                return new ApiResult(false, "card not belong to user");
            }
            if (getCardType(cardId) == cardType.CREDIT_CARD) {
                ApiResult apiResult = CreditcardService.loss(cardId, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                return new ApiResult(true, "success");
            }
            else {
                ApiResult apiResult = DebitcardService.loss(cardId, password);
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

    static public ApiResult history(Long userId) {
        try {
            ApiResult apiResult = getAllCard(userId);
            if (apiResult.ok == false) {
                return apiResult;
            }
            List<Long> cardList = (List<Long>) apiResult.payload;
            List<History> histories = new Vector<History>();
            for(var item : cardList) {
                if (getCardType(item) == cardType.CREDIT_CARD) {
                    apiResult = CreditcardService.getHistory(item);
                    if (apiResult.ok == false) {
                        return apiResult;
                    }
                }
                else {
                    apiResult = DebitcardService.getHistory(item);
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

    static private void Rollback(Long cardId, BigDecimal amount) throws Exception {
        if (getCardType(cardId) == cardType.CREDIT_CARD) {
            ApiResult apiResult = CreditcardService.increaceBalance(cardId, amount);
        }
        else {
            ApiResult apiResult = DebitcardService.increaceBalance(cardId, amount);
        }

    }

    static public ApiResult transfor(Long cardId, Long targetCardId, BigDecimal amount, String password) {
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
                apiResult = CreditcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                isDec = true;
            }
            else {
                apiResult = DebitcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                isDec = true;
            }

            if (getCardType(targetCardId) == cardType.CREDIT_CARD) {
                apiResult = CreditcardService.increaceBalance(cardId, amount);
                if (apiResult.ok == false) {
                    Rollback(cardId, amount);
                    return apiResult;
                }
            }
            else {
                apiResult = DebitcardService.increaceBalance(cardId, amount);
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

    static public cardType getCardType(Long cardId) throws Exception {
        if (cardId > 50000000000000000l) return cardType.CREDIT_CARD;
        else return cardType.DEBIT_CARD;
    }

}
