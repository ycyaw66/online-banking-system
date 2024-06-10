package com.zjuse.bankingsystem.service.loan;

//import entity.bank_card;
import com.zjuse.bankingsystem.mapper.loan.CardgetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardgetService {

    @Autowired
    private CardgetMapper bankCardMapper;

    /*public List<String> getUserBankCards(int userId) {
        List<BankCard> cards=bankCardMapper.getUserBankCards(userId);
        List<String> infos;
        for(BankCard card:cards){
            //获取相应的卡后，我们希望为用户展示    66025 - 张三 - 135****（身份证号） 的字符串形式显示在下拉列表中以便用户选卡还款
            //当用户选择后，前端发送还款申请我们会在后端将字符串提取卡号，进而更新卡内金额，详细见RepayController
            infos.add(card.getCard_number()+" - "+card.getCard_name()+" - "+card.getid_number());
        }
        return infos;
    }
    */
    
}
