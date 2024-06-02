package com.zjuse.bankingsystem.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.Admin;
import com.zjuse.bankingsystem.entity.creditCard.CreditCard;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardApplication;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardInspector;
import com.zjuse.bankingsystem.mapper.CreditCardApplicationMapper;
import com.zjuse.bankingsystem.mapper.CreditCardMapper;
import com.zjuse.bankingsystem.mapper.InspectorMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;

@Service
public class InspectorService {
    @Autowired
    private InspectorMapper inspectorMapper; 
    @Autowired
    private CreditCardApplicationMapper creditCardApplicationMapper; 
    @Autowired
    private CardService cardService; 
    @Autowired 
    private CreditCardMapper creditCardMapper; 

    public ApiResult loginInspector(String name, String password) {
        CreditCardInspector creditCardInspector = inspectorMapper.loginInspector(name, password);
        if (creditCardInspector == null) {
            return new ApiResult(false, "登录失败");
        } else {
            return new ApiResult(true, creditCardInspector);
        }
    }

    public ApiResult getInspectorByUsername(String username) {
        try {

            CreditCardInspector res = inspectorMapper.selectOne(new QueryWrapper<CreditCardInspector>().eq("name", username));
            if (Objects.isNull(res))
                return new ApiResult(false, "inspector username not found");
            return new ApiResult(true, res);
        } catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult queryRequestsByInspector(Integer permission) {
        if (permission.equals(1)) {
            List<CreditCardApplication> creditCardApplications = creditCardApplicationMapper.queryPartRequestByInspector();
            return new ApiResult(true, creditCardApplications);
        } else {
            List<CreditCardApplication> creditCardApplications = creditCardApplicationMapper.queryAllRequestByInspector();
            return new ApiResult(true, creditCardApplications);
        }
    }

    public ApiResult acceptRequest(Long id) {
        try {
            CreditCardApplication creditCardApplication = creditCardApplicationMapper.selectSingleRequest(id);
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
                creditCardApplicationMapper.acceptRequest(id);
                return new ApiResult(true, null);
            } else {
                return new ApiResult(false, "请求已被处理");
            }
        }
        catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult rejectRequest(Long id) {
        creditCardApplicationMapper.rejectRequest(id);
        return new ApiResult(true, null);
    }
}
