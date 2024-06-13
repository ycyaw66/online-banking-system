package com.zjuse.bankingsystem.service.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrencyAccount;
import com.zjuse.bankingsystem.entity.foreignCurrency.TradeRecord;
import com.zjuse.bankingsystem.mapper.foreignCurrency.ForeignCurrencyAccountMapper;
import com.zjuse.bankingsystem.mapper.foreignCurrency.TradeRecordMapper;
import com.zjuse.bankingsystem.service.user.UserAndCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TradeService负责处理外汇交易的逻辑，包括买入和卖出交易。
 */
@Service
public class TradeService {


    @Autowired
    private ForeignCurrencyAccountMapper fcAccountMapper;

    @Autowired
    private TradeRecordMapper tradeRecordMapper;

    @Autowired
    private UserAndCardService cardService; 

    /**
     * 执行外汇交易的主方法。
     *
     * @return 交易结果消息
     */
    public RespResult executeTrade(TradeRecord record) {
        // TODO:查询用户人民币账户余额
        Long cardId = Long.valueOf(record.getCredit_card_id()); 
        ApiResult res = cardService.getBalance(cardId);
        if (!res.ok) 
            return RespResult.fail(res.message);
        double userBalance = ((BigDecimal)res.payload).doubleValue();//userAccountMapper.getBalanceByUserId(userId);
        // 查询外币账户余额
        ForeignCurrencyAccount fAccount = fcAccountMapper.findAccountsByCreditCardIdAndFcId(record.getCredit_card_id(), record.getFc_id());
        if(fAccount == null){
            fAccount = new ForeignCurrencyAccount(record.getCredit_card_id(), record.getFc_id(), 0.);
            fcAccountMapper.insertForeignCurrencyAccount(fAccount);
        }

        if (record.getIs_buy_in()) { // 买入外币
            if (userBalance < record.getAmount_cny()) {
                return RespResult.fail("交易失败：人民币余额不足");
            }
            // TODO:更新人民币账户余额
            cardService.consume(cardId, new BigDecimal(record.getAmount_cny()), "", "买卖外币");
            // 更新外币账户余额
            fAccount.setAmount(fAccount.getAmount() + record.getAmount_foreign_currency());
            fcAccountMapper.updateForeignCurrencyAccount(fAccount);
        } else { // 卖出外币
            if (fAccount.getAmount() < record.getAmount_foreign_currency()) {
                return RespResult.fail("交易失败：外币余额不足");
            }
            // 更新外币账户余额
            fAccount.setAmount(fAccount.getAmount() - record.getAmount_foreign_currency());
            fcAccountMapper.updateForeignCurrencyAccount(fAccount);
            // TODO: 更新人民币账户余额
            cardService.income(cardId, new BigDecimal(record.getAmount_cny()), "买卖外币");
        }

        // 设置交易时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);
        String id = (formattedDateTime + record.getCredit_card_id()).substring(0, 20);
        record.setTrade_id(id);
        record.setTrade_time(now);
        tradeRecordMapper.insertTradeRecord(record);

        return RespResult.success(null);
    }
}
