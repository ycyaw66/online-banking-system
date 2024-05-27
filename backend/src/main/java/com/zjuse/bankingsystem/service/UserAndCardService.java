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
    HistoryMapper historyMapper;
    @Autowired
    UserService userService;
    @Autowired
    CreditCardService creditcardService;
    @Autowired
    DebitcardService debitcardService;
    @Autowired
    BlacklistService blacklistService;

    @Autowired
    CardService cardService;
    

    public ApiResult consume(Long cardId, BigDecimal amount, String password, String remark) {
        try {
            if (amount.compareTo(new BigDecimal(0)) < 0) {
                return new ApiResult(false, "No negetive amout");
            }
            if (!cardService.existCard(cardId)) {
                return new ApiResult(false, "card not exist");
            }
            if (!cardService.checkPayment(cardId)) {
                return new ApiResult(false, "permission denied");
            }
            // check priviledge? I don't know
            ApiResult apiResult = null;
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                Date date = new Date();
                apiResult = creditcardService.bankPay(cardId, password, amount, date);
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
            Date date = new Date();
            History history = new History(null, cardId, 1L, amount, date.getTime(), remark);
            historyMapper.insert(history);
            return apiResult;
        }
        
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult loss(Long cardId, String password) {
        try {
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                ApiResult apiResult = creditcardService.makeCreditCardLost(cardId);
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
                // apiResult = creditcardService.getBalance(cardId, password);
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
            ApiResult apiResult = creditcardService.returnMoney(cardId, amount);
        }
        else {
            ApiResult apiResult = debitcardService.increaceBalance(cardId, amount);
        }
    }

    public ApiResult transfor(Long cardId, Long targetCardId, BigDecimal amount, String password, String remark) {
        boolean isDec = false;
        try {
            if (amount.compareTo(new BigDecimal(0)) < 0) {
                return new ApiResult(false, "No negetive amout");
            }
            if (cardId == targetCardId) {
                return new ApiResult(false, "can't transfor to same card");
            }
            if (!cardService.existCard(cardId)) {
                return new ApiResult(false, "card not exist");
            }
            if (!cardService.existCard(targetCardId)) {
                return new ApiResult(false, "target card not exist");
            }

            
            if (!cardService.checkTransfer(cardId)) {
                return new ApiResult(false, "permission denied");
            }

            if (!cardService.checkReceive(targetCardId)) {
                return new ApiResult(false, "target card permission denied");
            }
            
            ApiResult apiResult;
            // check priviledge? I don't know
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                Date date = new Date();
                apiResult = creditcardService.bankPay(cardId, password, amount, date);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                System.out.println("### ok1" + cardId);
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
                apiResult = creditcardService.returnMoney(targetCardId, amount);
                if (apiResult.ok == false) {
                    Rollback(cardId, amount);
                    return apiResult;
                }
                System.out.println("### ok2" + targetCardId);
            }
            else {
                apiResult = debitcardService.increaceBalance(cardId, amount);
                if (apiResult.ok == false) {
                    Rollback(cardId, amount);
                    return apiResult;
                }
            }

            
            Date date = new Date();
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
