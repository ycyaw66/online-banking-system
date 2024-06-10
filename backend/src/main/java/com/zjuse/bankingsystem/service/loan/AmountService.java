package com.zjuse.bankingsystem.service.loan;
import com.zjuse.bankingsystem.service.user.UserAndCardService;
import com.zjuse.bankingsystem.utils.ApiResult;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmountService {

    @Autowired
    private UserAndCardService userAndCardService;

    public BigDecimal getamount(Long card_id, String password){
        return  (BigDecimal)userAndCardService.getBalance(card_id, password).payload;
    }
    public void addAmount(Long card_id, BigDecimal amount) {
        userAndCardService.income(card_id, amount, "贷款");
    }
    public int subAmount(Long card_id,BigDecimal amount, String password){
        ApiResult res = userAndCardService.consume(card_id, amount, password, "贷款还款");
        if (!res.ok) return 0;
        else return 1;
    }
}