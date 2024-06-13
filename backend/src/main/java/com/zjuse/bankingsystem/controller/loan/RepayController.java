package com.zjuse.bankingsystem.controller.loan;

import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.loan.AmountService;
import com.zjuse.bankingsystem.service.loan.CardgetService;
import com.zjuse.bankingsystem.service.loan.LoanQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class RepayController {

    @Autowired
    private AmountService amountService;
    @Autowired
    private LoanQueryService loanQueryService;
    @Autowired
    private CardgetService bankCardService;
    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping("/bank-cards")
    public List<String> getUserBankCards() {
        // 假设这里有一个方法能够获取当前登录用户的银行卡列表
        // 假设已经获取到当前登录用户的用户ID，这里假设用户ID为1
        Long userId = (Long)currentUserService.getCurrentUserId().payload;
        return bankCardService.getUserBankCards(userId);
    }

    @PostMapping("/confirm-repayment")
    public Map<String, Object> Repayment(@RequestBody Map<String, String> request) {
        int loan_id = Integer.parseInt(request.get("loan_id"));
        String info = request.get("info");
        String password = request.get("password");

        Long card_id = 0L;
        System.out.println("aaa");
        String[] parts = info.split(" - ");
        if (parts.length > 0) {
            try {
                // 将提取的字符串转换为整数
                int number = Integer.parseInt(parts[0]);
                System.out.println("id" + number); // 输出：66025
                card_id = (long) number;
            } catch (NumberFormatException e) {
                System.out.println("未找到匹配的数字部分");
            }
        } else {
            System.out.println("未找到匹配的数字部分");
        }

        double loan_amount = loanQueryService.getamount(loan_id);

        double amount = amountService.getamount(card_id, password).doubleValue();

        double repay_amount = loan_amount * (1 + loanQueryService.getrate(loan_id) * 0.01);

        // System.out.println("repay_amount:" + repay_amount);
        int result = amountService.subAmount(card_id, BigDecimal.valueOf(repay_amount), password);

        loanQueryService.updatestatus(loan_id);

        Map<String, Object> response = new HashMap<>();

        if (amount > repay_amount) {
            if (result > 0) response.put("message", "Repayment successfully!");
            else response.put("message", "fail to repay!");
        } else response.put("message", "Lack of balance!");

        return response;
    }
}