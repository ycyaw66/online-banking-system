package com.zjuse.bankingsystem.service;

import com.google.protobuf.Api;
import com.zjuse.bankingsystem.entity.creditCard.*;
import com.zjuse.bankingsystem.mapper.CreditCardMapper;
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
    private CardService cardService;

    public ApiResult getCardsByIdNumber(String idNumber) {
        return new ApiResult(true, null, creditCardMapper.queryCards(idNumber));
    }

    public ApiResult addNewCreditCardRequest(String idNumber, BigDecimal cardLimit, String password) {
        creditCardMapper.addNewCreditCardRequest(idNumber, cardLimit, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult modifyCreditCardPassword(Long cardId, String password) {
        creditCardMapper.modifyCreditCardPassword(cardId, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult addModifyLimitRequest(String idNumber, Long cardId, BigDecimal limit) {
        creditCardMapper.addModifyLimitRequest(idNumber, cardId, limit);
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
        if (!creditCard.getPassword().equals(password)) {
            return new ApiResult(false, "Wrong password");
        }
        creditCardMapper.setCreditCardLost(cardId);
        return new ApiResult(true, "挂失成功");
    }

    public ApiResult deleteCreditCard(Long cardId) {
        creditCardMapper.deleteCreditCard(cardId);
        return new ApiResult(true, "注销信用卡成功");
    }

    public ApiResult loginAdmin(String name, String password) {
        CreditCardAdmin creditCardAdmin = creditCardMapper.loginAdmin(name, password);
        if (creditCardAdmin == null) {
            return new ApiResult(false, "登录失败");
        } else {
            return new ApiResult(true, "登录成功");
        }
    }

    public ApiResult queryInspectors() {
        List<CreditCardInspector> creditCardInspectors = creditCardMapper.queryInspectors();
        return new ApiResult(true, creditCardInspectors);
    }

    public ApiResult modifyInspectorPassword(Integer id, String password) {
        creditCardMapper.modifyInspectorPassword(id, password);
        return new ApiResult(true, "修改成功");
    }

    public ApiResult modifyInspectorLevel(Integer id, Integer permission) {
        creditCardMapper.modifyInspectorLevel(id, permission);
        return new ApiResult(true, "修改成功");
    }

    public ApiResult deleteInspector(Integer id) {
        creditCardMapper.deleteInspector(id);
        return new ApiResult(true, "删除成功");
    }

    public ApiResult addNewInspector(String name, String password, Integer permission) {
        creditCardMapper.addNewInspector(name, password, permission);
        return new ApiResult(true, "添加成功");
    }

    public ApiResult loginInspector(String name, String password) {
        CreditCardInspector creditCardInspector = creditCardMapper.loginInspector(name, password);
        if (creditCardInspector == null) {
            return new ApiResult(false, "登录失败");
        } else {
            return new ApiResult(true, creditCardInspector);
        }
    }

    public ApiResult queryRequestsByInspector(Integer permission) {
        if (permission.equals(1)) {
            List<CreditCardApplication> creditCardApplications = creditCardMapper.queryPartRequestByInspector();
            return new ApiResult(true, creditCardApplications);
        } else {
            List<CreditCardApplication> creditCardApplications = creditCardMapper.queryAllRequestByInspector();
            return new ApiResult(true, creditCardApplications);
        }
    }

    public ApiResult acceptRequest(Long id) {
        try {
            CreditCardApplication creditCardApplication = creditCardMapper.selectSingleRequest(id);
            Integer type = creditCardApplication.getType();
            Integer status = creditCardApplication.getStatus();
            if (status.equals(1)) {
                if (type.equals(1)) {
                    ApiResult apiResult = cardService.registerCard(CardType.CREDIT_CARD);
                    if (!apiResult.ok) return apiResult;
                    Long cardId = (Long) apiResult.payload;
                    CreditCard creditCard = new CreditCard();
                    creditCard.setId(cardId);
                    creditCard.setCardLimit(creditCardApplication.getAmount());
                    creditCard.setPassword(creditCardApplication.getPassword());
                    creditCard.setIdNumber(creditCardApplication.getIdNumber());
                    creditCard.setLoan(BigDecimal.valueOf(0));
                    creditCardMapper.insertCreditCard(creditCard);
                    cardService.bindUserAndCard(cardId, creditCardApplication.getIdNumber());
                } else {
                    creditCardMapper.updateCardLimit(creditCardApplication.getAmount(), creditCardApplication.getCreditCardId());
                }
                creditCardMapper.acceptRequest(id);
                return new ApiResult(true, null);
            } else {
                return new ApiResult(false, "请求已被处理");
            }
        }
        catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult rejectRequest(Integer id) {
        creditCardMapper.rejectRequest(id);
        return new ApiResult(true, null);
    }

    public ApiResult queryRequestsByCustomer(String idNumber) {
        List<CreditCardApplication> creditCardApplications = creditCardMapper.queryAllRequestsByCustomer(idNumber);
        if (creditCardApplications == null) {
            return new ApiResult(false, "查询失败");
        } else {
            return new ApiResult(true, creditCardApplications);
        }
    }

    public ApiResult bankPay(Long cardId, String password, BigDecimal account, Date date) {
        try {
            CreditCard matchCard = creditCardMapper.findCreditCard(cardId);
            if (matchCard == null) {
                return new ApiResult(false, "Credit Card not find");
            }
            if (!matchCard.getPassword().equals(password)) {
                return new ApiResult(false, "Wrong password");
            }
            BigDecimal cardLimit = matchCard.getCardLimit();
            BigDecimal loan = matchCard.getLoan();
            BigDecimal add = loan.add(account);
            int result = cardLimit.compareTo(add);
            if (result < 0) {
                return new ApiResult(false, "信用卡可用额度不足");
            }
            creditCardMapper.addPayment(cardId, account, date);
            creditCardMapper.updateLoan(cardId, account);
            return new ApiResult(true, null);
        }
        catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult queryBills(Date startDate, Date endDate, Long cardId) {
        List<CreditCardBill> creditCardBills = creditCardMapper.queryBills(startDate, endDate, cardId);
        return new ApiResult(true, creditCardBills);
    }
}
