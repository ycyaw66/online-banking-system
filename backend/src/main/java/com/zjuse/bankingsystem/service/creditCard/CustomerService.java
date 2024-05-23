package com.zjuse.bankingsystem.service.creditCard;

import com.zjuse.bankingsystem.mapper.creditCard.CustomerMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    public ApiResult getCardsByIdNumber(String idNumber) {
        return new ApiResult(true, null, customerMapper.queryCards(idNumber));
    }

    public ApiResult addNewCreditCard(String idNumber, BigInteger cardLimit, String password) {
        customerMapper.addNewCreditCard(idNumber, cardLimit, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult modifyCreditCardPassword(BigInteger cardId, String password) {
        customerMapper.modifyCreditCardPassword(cardId, password);
        return new ApiResult(true, null, null);
    }

    public ApiResult addModifyLimitRequest(String idNumber, BigInteger cardId, BigInteger limit) {
        customerMapper.addModifyLimitRequest(idNumber, cardId, limit);
        return new ApiResult(true, null, null);
    }

    public ApiResult returnMoney(BigInteger cardId, BigInteger amount){
        customerMapper.returnMoney(cardId,amount);
        return new ApiResult(true,null,null);
    }
}
