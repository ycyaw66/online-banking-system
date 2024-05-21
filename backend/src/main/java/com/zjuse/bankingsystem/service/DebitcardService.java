package com.zjuse.bankingsystem.service;

import java.math.BigDecimal;

import com.zjuse.bankingsystem.utils.ApiResult;

public class DebitcardService {
    static ApiResult increaceBalance(Long cardId, BigDecimal amount) {
        return new ApiResult(false, "not implemented");
    }
    static ApiResult decreaceBalance(Long cardId, BigDecimal amount,  String password) {
        return new ApiResult(false, "not implemented");
    }
    static ApiResult loss(Long cradId, String password) {
        return new ApiResult(false, "not implemented");
    }
    static ApiResult getHistory(Long cardId) {
        return new ApiResult(false, "not implemented");
    }
}
