package com.zjuse.bankingsystem.service.loan;
import com.zjuse.bankingsystem.mapper.loan.AmountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmountService {

    @Autowired
    private AmountMapper amountMapper;

    public double getamount(int card_id){
        return  amountMapper.getAmount(card_id);
    }
    public int changeamount(int card_id,double amount){
        return amountMapper.updateAmount(card_id,amount);
    }
}