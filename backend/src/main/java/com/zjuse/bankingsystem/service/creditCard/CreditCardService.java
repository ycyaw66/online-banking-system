package com.zjuse.bankingsystem.service.creditCard;

import com.zjuse.bankingsystem.entity.creditCard.*;
import com.zjuse.bankingsystem.mapper.creditCard.CreditCardApplicationMapper;
import com.zjuse.bankingsystem.mapper.creditCard.CreditCardMapper;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.user.CardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.PrimitiveIterator;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Autowired
    private CreditCardApplicationMapper creditCardApplicationMapper; 

    @Autowired
    private CardService cardService;

    @Autowired
    private CurrentUserService currentUserService; 

    public ApiResult checkCreditCardPassword(Long cardId, String password) {
        CreditCard card = creditCardMapper.selectById(cardId);
        if (!password.equals(card.getPassword())) {
            return new ApiResult(false, "密码错误");
        }
        return new ApiResult(true, null);
    }

    public ApiResult getCardsByIdNumber(String idNumber) {
        return new ApiResult(true, null, creditCardMapper.queryCards(idNumber));
    }

    public ApiResult valid(Long cardId, String password) {
        CreditCard creditCard = creditCardMapper.findCreditCard(cardId);
        if (creditCard.getId() == null || !creditCard.getPassword().equals(password) ) {
            return new ApiResult(false, "该信用卡不存在或密码错误");
        }
        if (creditCard.getIsLost() == 1) {
            return new ApiResult(false, "该信用卡已挂失");
        }
        return new ApiResult(true, "验证成功");
    }

    public ApiResult getBalance(Long cardId) {
        CreditCard creditCard = creditCardMapper.findCreditCard(cardId);
        if (creditCard.getId() == null) {
            return new ApiResult(false, "该信用卡不存在");
        }
        return new ApiResult(true, creditCard.getLoan().negate());
    }

    public ApiResult addNewCreditCardRequest(String idNumber, BigDecimal cardLimit, String password) {
        creditCardApplicationMapper.addNewCreditCardRequest(idNumber, cardLimit, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult modifyCreditCardPassword(Long cardId, String password) {
        creditCardMapper.modifyCreditCardPassword(cardId, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult addModifyLimitRequest(String idNumber, Long cardId, BigDecimal limit) {
        creditCardApplicationMapper.addModifyLimitRequest(idNumber, cardId, limit);
        return new ApiResult(true, null, null);
    }

    public ApiResult returnMoney(Long cardId, BigDecimal amount) {
        creditCardMapper.returnMoney(cardId, amount);
        return new ApiResult(true, null, null);
    }

    public ApiResult makeCreditCardLost(Long cardId, String password) {
        CreditCard creditCard = creditCardMapper.findCreditCard(cardId);
        if (creditCard.getId() == null) {
            return new ApiResult(false, "该信用卡不存在");
        }
        if (creditCard.getIsLost() == 1) {
            return new ApiResult(false, "该信用卡已挂失");
        }
        if (!creditCard.getPassword().equals(password)) {
            return new ApiResult(false, "Wrong password");
        }
        creditCardMapper.setCreditCardLost(cardId);
        ApiResult apiResult = cardService.registerCard(CardType.CREDIT_CARD);
        if (!apiResult.ok) return apiResult;
        Long newCardId = (Long) apiResult.payload;
        creditCard.setId(newCardId);
        creditCardMapper.insertCreditCard(creditCard);
        cardService.bindUserAndCard(newCardId, creditCard.getIdNumber());
        return new ApiResult(true, "挂失成功");
    }

    public ApiResult deleteCreditCard(Long cardId) {
        creditCardMapper.deleteCreditCard(cardId);
        return new ApiResult(true, "注销信用卡成功");
    }


    public ApiResult queryRequestsByCustomer(String idNumber) {
        List<CreditCardApplication> creditCardApplications = creditCardApplicationMapper.queryAllRequestsByCustomer(idNumber);
        if (creditCardApplications == null) {
            return new ApiResult(false, "查询失败");
        } else {
            return new ApiResult(true, creditCardApplications);
        }
    }

    public ApiResult bankPay(Long cardId, String password, BigDecimal account, Date date) {
        try {
            CreditCard matchCard = creditCardMapper.findCreditCard(cardId);
            if (matchCard == null || !matchCard.getPassword().equals(password)) {
                return new ApiResult(false, "Credit Card not find Or Wrong password");
            }
            if (matchCard.getIsLost() == 1) {
                return new ApiResult(false, "Credit Card is lost");
            }
            BigDecimal cardLimit = matchCard.getCardLimit();
            BigDecimal loan = matchCard.getLoan();
            BigDecimal add = loan.add(account);
            int result = cardLimit.compareTo(add);
            if (result < 0) {
                return new ApiResult(false, "信用卡可用额度不足");
            }
            String identitNumber = (String) currentUserService.getCurrentUserIdNumber().payload; 
            creditCardMapper.addPayment(cardId, identitNumber, account, date);
            creditCardMapper.updateLoan(cardId, account);
            return new ApiResult(true, null);
        }
        catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult queryBills(Date startDate, Date endDate, String idNumber) {
        List<CreditCardBill> creditCardBills = creditCardMapper.queryBills(startDate, endDate, idNumber);
        return new ApiResult(true, creditCardBills);
    }
}
