package com.zjuse.bankingsystem.service;

import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;
import com.zjuse.bankingsystem.mapper.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.management.Query;

import org.apache.tomcat.util.buf.HexUtils;
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
    HistoryMapper historyMapper;
    Date date = new Date();
    @Autowired
    UserService userService;
    @Autowired
    CreditcardService creditcardService;
    @Autowired
    DebitcardService debitcardService;
    @Autowired
    BlacklistService blacklistService;

    @Autowired
    CardService cardService;
    

    public ApiResult consume(Long cardId, BigDecimal amount, String password, String remark) {
        try {
            if (amount.doubleValue() < 0) {
                return new ApiResult(false, "No negetive amout");
            }
            // check priviledge? I don't know
            ApiResult apiResult = null;
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                apiResult = creditcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                apiResult = new ApiResult(true, "success");
            }
            else {
                apiResult = debitcardService.decreaceBalance(cardId, amount, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                apiResult =  new ApiResult(true, "success");
            }
            History history = new History(null, cardId, 0L, amount, date.getTime(), remark);
            historyMapper.insert(history);
            return apiResult;
        }
        
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult loss(Long cardId, String password) {
        try {

            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
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

    public ApiResult history(Long cardId) {
        try {
            QueryWrapper<History> wrapper = new QueryWrapper<>();
            wrapper.eq("card_id", cardId);
            wrapper.or().eq("target_card", cardId);
            List<History> list = historyMapper.selectList(wrapper);
            if (list == null) {
                return new ApiResult(false, "database error");
            }
            ApiResult apiResult = new ApiResult(true, "success");
            apiResult.payload = list;
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }
    public ApiResult getBalance(Long cardId, String password) {
        try {
            ApiResult apiResult = null;
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                apiResult = creditcardService.getBalance(cardId, password);
            }
            else {
                apiResult = debitcardService.getBalance(cardId, password);
            }
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }



    private void Rollback(Long cardId, BigDecimal amount) throws Exception {
        if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
            ApiResult apiResult = creditcardService.increaceBalance(cardId, amount);
        }
        else {
            ApiResult apiResult = debitcardService.increaceBalance(cardId, amount);
        }

    }

    public ApiResult transfor(Long cardId, Long targetCardId, BigDecimal amount, String password, String remark) {
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
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
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

            if (cardService.getCardType(targetCardId) == CardType.CREDIT_CARD) {
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
            History history = new History(null, cardId, targetCardId, amount, date.getTime(), remark);
            historyMapper.insert(history);
            return new ApiResult(true, "success");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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


}
