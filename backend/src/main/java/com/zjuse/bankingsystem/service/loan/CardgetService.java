package com.zjuse.bankingsystem.service.loan;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.entity.user.Card;
import com.zjuse.bankingsystem.entity.user.User;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.user.CardService;
import com.zjuse.bankingsystem.service.user.UserAndCardService;

@Service
public class CardgetService {

    @Autowired
    private CardService cardService;
    @Autowired
    private CurrentUserService currentUserService;

    public List<String> getUserBankCards(Long userId) {
        List<Card> cards = (List<Card>)cardService.getAllCardbyUserId(userId).payload;
        User user = (User)currentUserService.getCurrentUser().payload;
        List<String> infos = new ArrayList<>();
        for(Card card:cards){
            //获取相应的卡后，我们希望为用户展示    66025 - 张三 - 135****（身份证号） 的字符串形式显示在下拉列表中以便用户选卡还款
            //当用户选择后，前端发送还款申请我们会在后端将字符串提取卡号，进而更新卡内金额，详细见RepayController
            infos.add(card.getCardId()+" - "+user.getUsername()+" - "+user.getIdNumber());
        }
        return infos;
    }


}
