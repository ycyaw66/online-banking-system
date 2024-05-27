package com.zjuse.bankingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.Card;
import com.zjuse.bankingsystem.mapper.CardMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;

@Service
public class CardService {
    @Autowired
    CardMapper cardMapper;

    public ApiResult registerCard(CardType cardType) {
        try {
            Card card = new Card(null, cardType);
            cardMapper.insert(card);
            ApiResult apiResult = new ApiResult(true, "success", card.getCardId());
            return apiResult;
        } catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult getCardType(Long cardId) {
        try {
            QueryWrapper<Card> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("card_id", cardId);
            Card card = cardMapper.selectOne(queryWrapper);
            if (card == null) {
                return new ApiResult(false, "card not found");
            }
            return new ApiResult(true, "success", card.getCardType());
        } catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

}
