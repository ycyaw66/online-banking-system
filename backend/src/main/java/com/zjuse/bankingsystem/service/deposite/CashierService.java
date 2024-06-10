package com.zjuse.bankingsystem.service.deposite;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjuse.bankingsystem.entity.deposite.Cashier;
import com.zjuse.bankingsystem.mapper.deposite.CashierMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class CashierService {
    @Autowired
    CashierMapper cashierMapper;

    public ApiResult getAllCashier(){
        try {
            List<Cashier> cashierList = cashierMapper.selectList(null);
            return new ApiResult(true,cashierList);
        }catch (Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    public ApiResult getCashierById(Long id){
        try{
            QueryWrapper<Cashier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<Cashier> cashierList = cashierMapper.selectList(queryWrapper);
            if(cashierList.size()==0){
                return new ApiResult(false,"用户不存在");
            }
            return new ApiResult(true,cashierList.get(0));
        }catch (Exception e){
            return new ApiResult(false,e.getMessage());
        }
    }

    public ApiResult getCashierByUsername(String username) {
        try {
            QueryWrapper<Cashier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username); 
            Cashier cashier = cashierMapper.selectOne(queryWrapper);
            if (Objects.isNull(cashier)) {
                return new ApiResult(false, "用户名不存在");
            }
            return new ApiResult(true, cashier); 
        } catch (Exception e) { 
            return new ApiResult(false, e.getMessage()); 
        }
    }

    public ApiResult getCashierIdByUsername(String username) {
        try {
            QueryWrapper<Cashier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username); 
            Cashier cashier = cashierMapper.selectOne(queryWrapper);
            if (Objects.isNull(cashier)) {
                return new ApiResult(false, "用户名不存在");
            }
            return new ApiResult(true, cashier.getId()); 
        } catch (Exception e) { 
            return new ApiResult(false, e.getMessage()); 
        }
    }

    public ApiResult addCashier(String username,String password,int authority){
        try{
            //加密密码
            Random rand = new Random(3*System.currentTimeMillis());
            String salt= String.valueOf(rand.nextInt(900001)+100000);
            String newpassword = DigestUtil.sha256Hex(password+salt);
            Cashier cashier = new Cashier();
            cashier.setSalt(salt);
            cashier.setPassword(newpassword);
            cashier.setUsername(username);
            cashier.setAuthority(authority);
            cashierMapper.insert(cashier);
            return new ApiResult(true,cashier);
        }catch (Exception e){
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult deleteCashierById(Long id){
        try{
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<Cashier> cashier = cashierMapper.selectList(queryWrapper);
            if(cashier.size()==0){
                return new ApiResult(false,"账号不存在");
            }
            QueryWrapper queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id", id);
            cashierMapper.delete(queryWrapper1);
            return new ApiResult(true,"删除成功");
        }catch (Exception e){
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult verifyCashier(Long id ,String password){
        try{
            QueryWrapper<Cashier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<Cashier> cashiers = cashierMapper.selectList(queryWrapper);
            if(cashiers.size()==0){
                return new ApiResult(false,"账号不存在");
            }
            if(password.equals(cashiers.get(0).getPassword())){
                return new ApiResult(true, cashiers.get(0));
            }else{
                return new ApiResult(false,"密码错误");
            }
        }catch (Exception e){
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult changePassword(Long id,String password){
        try{
            QueryWrapper<Cashier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<Cashier> cashiers = cashierMapper.selectList(queryWrapper);
            if(cashiers.size()==0){
                return new ApiResult(false,"账号不存在");
            }
            UpdateWrapper<Cashier> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            updateWrapper.set("password", password);
            cashierMapper.update(null, updateWrapper);
            return new ApiResult(true,"修改成功");
        }catch (Exception e){
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult changeAuthority(Long id,int authority){
        try{
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<Cashier> cashiers = cashierMapper.selectList(queryWrapper);
            if(cashiers.size()==0){
                return new ApiResult(false,"账号不存在");
            };
            UpdateWrapper<Cashier> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            updateWrapper.set("authority", authority);
            cashierMapper.update(null, updateWrapper);
            return new ApiResult(true,"修改成功");
        }catch (Exception e){
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult getAuthority(Long id){
        try{
            QueryWrapper<Cashier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            List<Cashier> cashiers = cashierMapper.selectList(queryWrapper);
            if(cashiers.size()==0){
                return new ApiResult(false,"账号不存在");
            }
            return new ApiResult(true,cashiers.get(0).getAuthority());
        }catch (Exception e){
            return new ApiResult(false, e.getMessage());
        }
    }
}
