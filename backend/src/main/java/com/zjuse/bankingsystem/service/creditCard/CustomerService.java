package com.zjuse.bankingsystem.service.creditCard;

import com.zjuse.bankingsystem.mapper.creditCard.CustomerMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    public ApiResult getCardsByIdNumber(String idNumber){
        return new ApiResult(true,null,customerMapper.queryCards(idNumber));
    }
}
