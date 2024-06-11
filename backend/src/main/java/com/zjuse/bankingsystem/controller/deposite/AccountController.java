package com.zjuse.bankingsystem.controller.deposite;

import com.zjuse.bankingsystem.entity.deposite.Account;
import com.zjuse.bankingsystem.entity.deposite.DepositeCard;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.deposite.AccountService;
import com.zjuse.bankingsystem.service.deposite.DepositeCardService;
import com.zjuse.bankingsystem.service.deposite.CashierService;
import com.zjuse.bankingsystem.service.deposite.PropertyService;
import com.zjuse.bankingsystem.utils.AccountStatus;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.DepositCardType;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private DepositeCardService cardService;

    @Autowired
    private CashierService cashierService;

    @Autowired 
    private CurrentUserService currentUserService; 

    @PostMapping("/counter/account/add")//添加账户，输入姓名、电话号码、密码、卡片类型、身份证号，返回entity中的Account类
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult addAccount(@RequestParam("name") String name, @RequestParam("phoneNumber")String phoneNumber,@RequestParam("password")String password,@RequestParam("cardType") DepositCardType cardType,@RequestParam("citizenid")String citizenid) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<2){
            return  RespResult.fail("权限不足");
        }
        AccountStatus accountStatus = AccountStatus.Normal;
        ApiResult apiResult = accountService.newAccount(name,phoneNumber,accountStatus,password,cardType,citizenid);
        if(apiResult.ok)
            return RespResult.success(apiResult.payload);
        else
            return  RespResult.fail(apiResult.message);
    }

    @DeleteMapping("/counter/account/delete")//删除账户，输入卡号、密码删除账号，返回信息字符串
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult deleteAccount(@RequestParam("cardid")Long cardid, @RequestParam("password")String password) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<2){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.VerifyPassword(cardid,password);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        Long accountid = (Long)apiResult1.payload;
        ApiResult apiResult = accountService.DeleteAccountByAccountId(accountid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.message);
    }

    @PostMapping("/counter/account/modify/password")//更改密码，输入卡号、旧密码、新密码，返回信息字符串
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult modifyAccountPassword(@RequestParam("cardid")Long cardid,@RequestParam("oldpassword")String oldpassword,@RequestParam("newpassword")String newpassword) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<2){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.VerifyPassword(cardid,oldpassword);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        Long accountid = (Long)apiResult1.payload;
        ApiResult apiResult = accountService.ChangePassword(newpassword,accountid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.message);
    }

    @PostMapping("/counter/account/modify/status/freeze")//冻结账户，输入卡号，返回信息字符串
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult freezeAccount(@RequestParam("cardid")Long cardid) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<3){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.getAccountByCardId(cardid);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        Account account = (Account) apiResult1.payload;
        AccountStatus accountStatus = account.getStatus();
        if(accountStatus==AccountStatus.Frozen)
            return  RespResult.fail("帐户已被冻结");
        Long accountid = account.getId();
        AccountStatus accountStatus1 =AccountStatus.Frozen;
        ApiResult apiResult = accountService.ChangeStatus(accountStatus1,accountid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.message);
    }

    @PostMapping("/counter/account/modify/status/unfreeze")//解冻账户，输入卡号，返回信息字符串
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult unFreezeAccount(@RequestParam("cardid")Long cardid) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<3){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.getAccountByCardId(cardid);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        Account account = (Account) apiResult1.payload;
        AccountStatus accountStatus = account.getStatus();
        if(accountStatus!=AccountStatus.Frozen)
            return  RespResult.fail("帐户未被冻结");
        Long accountid = account.getId();
        AccountStatus accountStatus1 =AccountStatus.Normal;
        ApiResult apiResult = accountService.ChangeStatus(accountStatus1,accountid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.message);
    }

    @PostMapping("/counter/account/modify/status/lost")
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult lostAccount(@RequestParam("cardid")Long cardid,@RequestParam("password")String password) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<2){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.VerifyPassword(cardid,password);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        Long accountid = (Long)apiResult1.payload;
        ApiResult apiReult =accountService.getAccountByCardId(cardid);
        if(!apiReult.ok)
            return  RespResult.fail(apiReult.message);
        if(((Account)apiReult.payload).getStatus()!=AccountStatus.Normal)
            return  RespResult.fail("账户状态异常");
        ApiResult apiResult = accountService.ChangeStatus(AccountStatus.Lost,accountid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.message);
    }

    @PostMapping("/counter/account/modify/status/unlost")
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult unLostAccount(@RequestParam("cardid")Long cardid,@RequestParam String password) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<2){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.VerifyPassword(cardid,password);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        Long accountid = (Long)apiResult1.payload;
        ApiResult apiReult =accountService.getAccountByCardId(cardid);
        if(!apiReult.ok)
            return  RespResult.fail(apiReult.message);
        if(((Account)apiReult.payload).getStatus()!=AccountStatus.Lost)
            return RespResult.fail("账户状态异常");
        ApiResult apiResult = accountService.ChangeStatus(AccountStatus.Normal,accountid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.message);
    }

    @PostMapping("/counter/account/modify/replace")
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult replaceAccount(@RequestParam("cardid")Long cardid,@RequestParam("password")String password,@RequestParam("cardtype")DepositCardType cardType) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<2){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.VerifyPassword(cardid,password);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        Long accountid = (Long)apiResult1.payload;
        ApiResult apiReult =accountService.getAccountByCardId(cardid);
        if(!apiReult.ok)
            return  RespResult.fail(apiReult.message);
        if(((Account)apiReult.payload).getStatus()!=AccountStatus.Lost)
            return RespResult.fail("账户状态异常");
        ApiResult apiResult = cardService.newCard(cardType,accountid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        DepositeCard card = (DepositeCard) apiResult.payload;
        Long newcardid = card.getCardId();
        cardService.bindCard(newcardid,accountid);
        ApiResult deletecard = cardService.DeleteCardByCardId(cardid);
        accountService.ChangeStatus(AccountStatus.Normal,accountid);
        if(!deletecard.ok)
            return  RespResult.fail(deletecard.message);
        else
            return  RespResult.success(newcardid);
    }

    @GetMapping("/account/info")
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult getAccountInfo(@RequestParam("cardid")Long cardid, @RequestParam("password") String password) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<1){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.VerifyPassword(cardid,password);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        ApiResult apiResult = accountService.getAccountByCardId(cardid);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.payload);
    }

    @GetMapping("/account/property")
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult getAccountProperty(@RequestParam("cardid")Long cardid, @RequestParam("password") String password) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<1){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult1 = accountService.VerifyPassword(cardid,password);
        if(!apiResult1.ok)
            return  RespResult.fail(apiResult1.message);
        ApiResult apiResult = propertyService.getPropertyByAccountId((Long)apiResult1.payload);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.payload);
    }

    @GetMapping("/accounnt/list")
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult getAccountList(@RequestParam("citizenid")String citizenid,@RequestParam("name")String name) {
        ApiResult res = currentUserService.getCurrentCashierId(); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long operatorid = (Long) res.payload; 
        //验证权限
        ApiResult verify = cashierService.getAuthority(operatorid);
        if(!verify.ok||(int)verify.payload<2){
            return  RespResult.fail("权限不足");
        }
        ApiResult apiResult = accountService.getAccountByCitizenIdAndName(citizenid,name);
        if(!apiResult.ok)
            return  RespResult.fail(apiResult.message);
        else
            return  RespResult.success(apiResult.payload);
    }
}
