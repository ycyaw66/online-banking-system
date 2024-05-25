package com.zjuse.bankingsystem.service;

import java.math.BigDecimal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.utils.ApiResult;

@Service
public class DebitcardService {
    public ApiResult increaceBalance(Long cardId, BigDecimal amount) {
        return new ApiResult(false, "not implemented");
    }
    public ApiResult decreaceBalance(Long cardId, BigDecimal amount,  String password) {
        return new ApiResult(false, "not implemented");
    }
    public ApiResult loss(Long cradId, String password) {
        return new ApiResult(false, "not implemented");
    }
    public ApiResult getHistory(Long cardId) {
        return new ApiResult(false, "not implemented");
    }
    public ApiResult getBalance(Long cardId, String password) {
        return new ApiResult(false, "not implemented");
    }
}
