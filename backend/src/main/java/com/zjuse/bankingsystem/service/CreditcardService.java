package com.zjuse.bankingsystem.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.google.protobuf.Api;
import com.zjuse.bankingsystem.utils.ApiResult;


@Service
public class CreditcardService {
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
}