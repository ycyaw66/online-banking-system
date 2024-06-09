package com.zjuse.bankingsystem.service.deposite;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjuse.bankingsystem.entity.deposite.Account;
import com.zjuse.bankingsystem.entity.deposite.DepositeCard;
import com.zjuse.bankingsystem.entity.deposite.Property;
import com.zjuse.bankingsystem.mapper.deposite.AccountMapper;
import com.zjuse.bankingsystem.mapper.deposite.DepositeCardMapper;
import com.zjuse.bankingsystem.mapper.deposite.PropertyMapper;
import com.zjuse.bankingsystem.service.user.CardService;
import com.zjuse.bankingsystem.utils.AccountStatus;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.CardType;
import com.zjuse.bankingsystem.utils.DepositCardType;
import com.zjuse.bankingsystem.utils.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.crypto.digest.DigestUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    CardService cardService; 
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    DepositeCardMapper cardMapper;
    @Autowired
    PropertyMapper propertyMapper;
    @Autowired
    DemandDepositService demandDepositService;

    /*
     * 新建储蓄卡账号
     */
    public ApiResult newAccount(String name, String phoneNumber, AccountStatus status, String password,DepositCardType cardtype,String citizenid){
        try{
            //加密密码
            Random rand = new Random(Long.parseLong(phoneNumber)+ System.currentTimeMillis());
            String salt= String.valueOf(rand.nextInt(900001)+100000);
            String newpassword = DigestUtil.sha256Hex(password+salt);
            //创建账户实例
            Account account = new Account();
            account.setName(name);
            account.setCardId(0L);
            account.setSalt(salt);
            account.setPassword(newpassword);
            account.setStatus(status);
            account.setPhonenumber(phoneNumber);
            account.setCitizenid(citizenid);
            //插入账户并获取id
            accountMapper.insert(account);
            Long accountid = account.getId();
            // 用户注册卡片
            cardService.registerCard(CardType.DEBIT_CARD);
            cardService.bindUserAndCard(accountid, citizenid); 

            //创建卡片实例
            DepositeCard card = new DepositeCard();
            card.setType(cardtype);
            card.setAccountid(accountid);
            //插入卡片并获取id
            cardMapper.insert(card);
            Long cardid =card.getCardId();
            account.setCardId(cardid);
            //新建活期
            Property property = new Property();
            property.setAccountid(account.getId());
            property.setType(PropertyType.demand);
            propertyMapper.insert(property);
            demandDepositService.newDemandDeposit(accountid, property.getId());
            //更新账户中卡片id
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", accountid);
            updateWrapper.set("card_id", cardid);
            accountMapper.update(null, updateWrapper);
            return new ApiResult(true, account);
        }catch (Exception e){
            return  new ApiResult(false,e.getMessage());
        }
    }

    /*
     * 删除储蓄卡账号
     */
    public ApiResult DeleteAccountByAccountId(Long accountid){
        try{
            QueryWrapper<Property> propertywrapper = new QueryWrapper<>();
            propertywrapper.eq("accountid",accountid);
            List<Property> property = propertyMapper.selectList(propertywrapper);
            if(property.size()>1|| !((BigDecimal)demandDepositService.showAmount(accountid).payload).equals(BigDecimal.ZERO)) {
               return new ApiResult(false,"账户内有资产");
            }
            QueryWrapper<Account> accountwrapper = new QueryWrapper<>();
            accountwrapper.eq("id",accountid);
            accountMapper.delete(accountwrapper);
            return new ApiResult(true,"删除成功");
        }catch(Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    /*
     * 验证密码
     */
    public ApiResult VerifyPassword(Long cardid,String password){
        try{
            QueryWrapper<Account> wrapper = new QueryWrapper<>();
            wrapper.eq("card_id",cardid);
            List<Account> account = accountMapper.selectList(wrapper);
            if(account.size()==0) {
                return new ApiResult(false,"卡片不存在");
            }
            String salt = account.get(0).getSalt();
            String newpassword = DigestUtil.sha256Hex(password+salt);
            if(newpassword.equals(account.get(0).getPassword())) {
                return  new ApiResult(true,account.get(0).getId());
            }else{
                return new ApiResult(false,"密码不匹配");
            }
        }catch(Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    public ApiResult getAccountByCardId(Long cardid){
        try{
            QueryWrapper<Account> wrapper = new QueryWrapper<>();
            wrapper.eq("card_id",cardid);
            List<Account> account = accountMapper.selectList(wrapper);
            if(account.size()==0) {
                return new ApiResult(false, "账户不存在");
            }else
                return new ApiResult(true,account.get(0));
        }catch (Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    public ApiResult getAccountByAccountId(Long accountid){
        try {
            QueryWrapper<Account> wrapper = new QueryWrapper<>();
            wrapper.eq("id",accountid);
            List<Account> account = accountMapper.selectList(wrapper);
            if(account.size()==0) {
                return new ApiResult(false, "账户不存在");
            }else
                return new ApiResult(true,account.get(0));
        }catch (Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    public  ApiResult ChangeStatus(AccountStatus status,Long accountid){
        try{
            QueryWrapper<Account> wrapper = new QueryWrapper<>();
            wrapper.eq("id",accountid);
            List<Account> account = accountMapper.selectList(wrapper);
            if(account.size()==0) {
                return new ApiResult(false,"账户不存在");
            }
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",accountid);
            updateWrapper.set("status",status.getvalue());
            accountMapper.update(null,updateWrapper);
            return new ApiResult(true,"修改成功");
        }catch (Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    public ApiResult ChangePassword(String password,Long accountid){
        try{
            QueryWrapper<Account> wrapper = new QueryWrapper<>();
            wrapper.eq("id",accountid);
            List<Account> account = accountMapper.selectList(wrapper);
            if(account.size()==0) {
                return new ApiResult(false,"账户不存在");
            }
            String salt = account.get(0).getSalt();
            String newpassword = DigestUtil.sha256Hex(password+salt);
            UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",accountid);
            updateWrapper.set("password",newpassword);
            accountMapper.update(null,updateWrapper);
            return new ApiResult(true,"修改成功");
        }catch (Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    public ApiResult getAccountByCitizenIdAndName(String citizenId,String name){
        try {
            QueryWrapper wrapper = new QueryWrapper<>();
            wrapper.eq("citizenid",citizenId);
            wrapper.eq("name",name);
            List<Account> account = accountMapper.selectList(wrapper);
            if(account.size()==0) {
                return new ApiResult(false, "账户不存在");
            }else
                return new ApiResult(true,account);
        }catch (Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }
}
