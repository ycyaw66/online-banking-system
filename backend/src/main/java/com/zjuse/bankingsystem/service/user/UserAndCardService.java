package com.zjuse.bankingsystem.service.user;

import com.zjuse.bankingsystem.utils.AccountStatus;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;
import com.zjuse.bankingsystem.mapper.user.HistoryMapper;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.creditCard.CreditCardService;
import com.zjuse.bankingsystem.service.deposite.AccountService;
import com.zjuse.bankingsystem.service.deposite.DemandDepositService;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import javax.management.Query;

import org.apache.tomcat.util.buf.HexUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.user.Card;
import com.zjuse.bankingsystem.entity.user.History;
import com.zjuse.bankingsystem.entity.user.HistoryCondition;;



@Service
public class UserAndCardService {
    @Autowired
    HistoryMapper historyMapper;
    @Autowired
    UserService userService;
    @Autowired
    CreditCardService creditcardService;
    @Autowired
    DemandDepositService demandDepositService;

    @Autowired
    AccountService accountService;
    @Autowired
    BlacklistService blacklistService;
    @Autowired 
    CurrentUserService currentUserService; 

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
                Date date = new Date(System.currentTimeMillis());
                apiResult = creditcardService.bankPay(cardId, password, amount, date);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                apiResult = new ApiResult(true, "success");
            }
            else {
                apiResult = accountService.VerifyPassword(cardId, password);
                if (!apiResult.ok) {
                    return apiResult;
                }
                apiResult = demandDepositService.changeAmount(cardId, amount.negate());
                if (apiResult.ok == false) {
                    return apiResult;
                }
                apiResult =  new ApiResult(true, "success");
            }
            Date date = new Date(System.currentTimeMillis());
            History history = new History(null, cardId, 1L, amount, date, remark);
            historyMapper.insert(history);
            return apiResult;
        }
        
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());

        }
    }

    public ApiResult income(Long cardId, BigDecimal amount, String remark) {
        return new ApiResult(false, "not implemented yet");
    }

    // 获得用户流水
    public ApiResult getMoneyStream(Long userId) {
        return new ApiResult(false, "not implemented yet", new BigDecimal(0));
    }

    public ApiResult loss(Long cardId, String password) {
        try {
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                ApiResult apiResult = creditcardService.makeCreditCardLost(cardId, password);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                return new ApiResult(true, "success");
            }
            else {
                ApiResult apiResult = accountService.ChangeStatus(AccountStatus.Lost, cardId);
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

    public ApiResult history(HistoryCondition condition) {
        try {
            QueryWrapper<History> wrapper = new QueryWrapper<>();
            if (condition == null) {
                List<History> list = historyMapper.selectList(wrapper);
                if (list == null) {
                    return new ApiResult(false, "database error");
                }
                ApiResult apiResult = new ApiResult(true, "success");
                apiResult.payload = list;
                return apiResult;
            }
            // QueryWrapper<History> idWrapper = new QueryWrapper<>();
            if (condition.getCardId() == null) {
                return new ApiResult(false, "card id can't be null");
            }
            if (condition.getTargetCardId() != null && condition.getTransferCardId() != null) {
                return new ApiResult(false, "target card and transfer card can't be set at the same time");
            }
            if (condition.getTargetCardId() == null && condition.getTransferCardId() == null) {
                wrapper.and(w -> w.eq("card_id", condition.getCardId()).or().eq("target_card", condition.getCardId()));
            }
            else if (condition.getTargetCardId() != null) {
                wrapper.eq("card_id", condition.getCardId());
                wrapper.eq("target_card", condition.getTargetCardId());
            }
            else {
                wrapper.eq("card_id", condition.getTransferCardId());
                wrapper.eq("target_card", condition.getCardId());

            }

            if (condition.getLeastAmount() != null) {
                wrapper.ge("amount", condition.getLeastAmount());
            }
            if (condition.getMostAmount() != null) {
                wrapper.le("amount", condition.getMostAmount());
            }
            if (condition.getStartTime() != null) {
                wrapper.ge("time", condition.getStartTime());
            }
            if (condition.getEndTime() != null) {
                wrapper.le("time", condition.getEndTime());
            }
            if (condition.getRemark() != null) {
                wrapper.like("remark", condition.getRemark());
            }
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

    public ApiResult historyAdmin(HistoryCondition condition) {
        try {
            QueryWrapper<History> wrapper = new QueryWrapper<>();
            // QueryWrapper<History> idWrapper = new QueryWrapper<>();
            if (condition.getTargetCardId() != null && condition.getTransferCardId() != null) {
                return new ApiResult(false, "target card and transfer card can't be set at the same time");
            }
            if (condition.getTargetCardId() != null) {
                wrapper.eq("target_card", condition.getTargetCardId());
            }
            if (condition.getTransferCardId() != null) {
                wrapper.eq("card_id", condition.getTransferCardId());
            }
            if (condition.getLeastAmount() != null) {
                wrapper.ge("amount", condition.getLeastAmount());
            }
            if (condition.getMostAmount() != null) {
                wrapper.le("amount", condition.getMostAmount());
            }
            if (condition.getStartTime() != null) {
                wrapper.ge("time", condition.getStartTime());
            }
            if (condition.getEndTime() != null) {
                wrapper.le("time", condition.getEndTime());
            }
            if (condition.getRemark() != null) {
                wrapper.like("remark", condition.getRemark());
            }
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


    public ApiResult loanHistory(Long userId) {
        try {
            ApiResult apiResult = cardService.getAllCardbyUserId(userId);
            if (!apiResult.ok) {
                return apiResult;
            }
            List<Card> cards = (List<Card>) apiResult.payload;
            List<History> histories = new Vector<>();
            for(Card card : cards) {
                HistoryCondition historyCondition = new HistoryCondition();
                historyCondition.setTargetCardId(card.getCardId());
                Date date = new Date(System.currentTimeMillis()); // 获取90天前date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, -90);
                historyCondition.setStartTime(date);
                apiResult = historyAdmin(historyCondition);
                if (!apiResult.ok) {
                    return apiResult;
                }
                apiResult = historyAdmin(historyCondition);
                if (!apiResult.ok) {
                    return apiResult;
                }
                List<History> histories_ = (List<History>) apiResult.payload;
                histories.addAll(histories_);
            }
            return new ApiResult(true, "success", histories);
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult getBalance(Long cardId) {
        try {
            ApiResult apiResult = null;
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                apiResult = creditcardService.getBalance(cardId);
            }
            else {
                apiResult = demandDepositService.showAmount(cardId);
            }
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }



    public ApiResult valid(Long cardId, String password) {
        try {
            if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
                ApiResult apiResult = creditcardService.valid(cardId, password);
                return apiResult;
            }
            else {
                // ApiResult apiResult = debitcardService.valid(cardId, password);
                return new ApiResult(false, "not implemented");
            }
        }
        catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }


    private void Rollback(Long cardId, BigDecimal amount) throws Exception {
        if (cardService.getCardType(cardId) == CardType.CREDIT_CARD) {
            ApiResult apiResult = creditcardService.returnMoney(cardId, amount);
        }
        else {
            ApiResult apiResult = demandDepositService.changeAmount(cardId, amount);
        }
    }

    public ApiResult transfer(Long cardId, Long targetCardId, BigDecimal amount, String password, String remark) {
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
                Date date = new Date(System.currentTimeMillis());
                apiResult = creditcardService.bankPay(cardId, password, amount, date);
                if (apiResult.ok == false) {
                    return apiResult;
                }
                isDec = true;
            } else {
                
                apiResult = accountService.VerifyPassword(cardId, password);
                if (!apiResult.ok) {
                    return apiResult;
                }
                apiResult = demandDepositService.changeAmount(cardId, amount.negate());
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
                apiResult = demandDepositService.changeAmount(cardId, amount);
                if (apiResult.ok == false) {
                    Rollback(cardId, amount);
                    return apiResult;
                }
            }

            
            Date date = new Date(System.currentTimeMillis());
            History history = new History(null, cardId, targetCardId, amount, date, remark);
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
