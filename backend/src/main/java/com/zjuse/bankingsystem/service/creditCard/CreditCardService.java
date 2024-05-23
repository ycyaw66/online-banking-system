package com.zjuse.bankingsystem.service.creditCard;

import com.zjuse.bankingsystem.entity.creditCard.CreditCard;
import com.zjuse.bankingsystem.mapper.creditCard.CreditCardMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardMapper creditCardMapper;

    public ApiResult getCardsByIdNumber(String idNumber) {
        return new ApiResult(true, null, creditCardMapper.queryCards(idNumber));
    }

    public ApiResult addNewCreditCardRequest(String idNumber, BigInteger cardLimit, String password) {
        creditCardMapper.addNewCreditCardRequest(idNumber, cardLimit, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult modifyCreditCardPassword(BigInteger cardId, String password) {
        creditCardMapper.modifyCreditCardPassword(cardId, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult addModifyLimitRequest(String idNumber, BigInteger cardId, BigInteger limit) {
        creditCardMapper.addModifyLimitRequest(idNumber, cardId, limit);
        return new ApiResult(true, null, null);
    }

    public ApiResult returnMoney(BigInteger cardId, BigInteger amount){
        creditCardMapper.returnMoney(cardId,amount);
        return new ApiResult(true,null,null);
    }

    public ApiResult makeCreditCardLost(BigInteger cardId){
        CreditCard creditCard = creditCardMapper.findCreditCard(cardId);
        if(creditCard.getId() == null){
            return new ApiResult(false,"该信用卡不存在");
        }
        creditCardMapper.setCreditCardLost(cardId);
        creditCardMapper.insertCreditCard(creditCard);
        return new ApiResult(true,"挂失成功");
    }

    public ApiResult deleteCreditCard(BigInteger cardId){
        creditCardMapper.deleteCreditCard(cardId);
        return new ApiResult(true,"注销信用卡成功");
    }
}
