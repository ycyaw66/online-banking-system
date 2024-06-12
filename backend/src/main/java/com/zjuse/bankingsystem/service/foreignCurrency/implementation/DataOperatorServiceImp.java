package com.zjuse.bankingsystem.service.foreignCurrency.implementation;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;
import com.zjuse.bankingsystem.mapper.foreignCurrency.DataOperatorMapper;
import com.zjuse.bankingsystem.model.DataOperatorInfo;
import com.zjuse.bankingsystem.model.UpdateInfo;
import com.zjuse.bankingsystem.service.foreignCurrency.DataOperatorService;

@Service
public class DataOperatorServiceImp implements DataOperatorService {

    @Autowired
    private DataOperatorMapper dataOperatorMapper;

    @Override
    public DataOperatorInfo selectDataOperatorByUsernameAndPassword(String username, String password) throws Exception {
        DataOperator dt = dataOperatorMapper.selectDataOperatorByUsername(username);
        if(dt == null)
            throw new Exception("用户名不存在");
        if(!dt.getPassword().equals(password))
            throw new Exception("密码错误");
        return new DataOperatorInfo(dt);
    }

    @Override
    public DataOperator selectDataOperatorByUsername(String username) throws Exception {
        DataOperator dt = dataOperatorMapper.selectDataOperatorByUsername(username);
        if(dt == null)
            throw new Exception("用户名不存在");
        return dt; 
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(DataOperator entity) throws Exception {
        if (entity.getUsername() == null || entity.getPassword() == null || entity.getEmail() == null || entity.getPhone_number() == null) {
            throw new Exception("不可为空");
        }
        if (entity.getUsername().length() > 20 || entity.getPassword().length() > 20 || entity.getEmail().length() > 20 || entity.getPhone_number().length() > 20) {
            throw new Exception("长度不可超过20");
        }

        if (dataOperatorMapper.selectDataOperatorByUsername(entity.getUsername()) != null) {
            throw new Exception("用户名已存在");
        }
        String id = LocalDateTime.now().toString();
        String[] strs = id.split("T");
        id = strs[0] + strs[1];
        strs = id.split(":");
        id = "";
        for(int i = 0; i < strs.length; i++)
            id += strs[i];
        strs = id.split("-");
        id = "";
        for(int i = 0; i < strs.length; i++)
            id += strs[i];
        System.out.println(id);
        if(id.length() > 20)
            id = id.substring(0, 20); 
        entity.setData_operator_id(id);
        if(dataOperatorMapper.insertDataOperator(entity)!=1){
            throw new Exception("注册失败，数据库插入失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void detailedInformation(DataOperator entity) throws Exception{
        if (entity.getUsername() == null || entity.getPassword() == null || entity.getEmail() == null || entity.getPhone_number() == null) {
            throw new Exception("不可为空");
        }
        if (entity.getUsername().length() > 20 || entity.getPassword().length() > 20 || entity.getEmail().length() > 20 || entity.getPhone_number().length() > 20) {
            throw new Exception("长度不可超过20");
        }
        if(dataOperatorMapper.selectDataOperatorByUsername(entity.getUsername()) == null)
            throw new Exception("用户名不存在");
        if(dataOperatorMapper.updateDataOperatorDetail(entity) != 1){
            throw new Exception("更新失败，数据库更新失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDataOperatorPassword(String data_operator_id, String new_password, String old_password)  throws Exception{
        DataOperator dt = dataOperatorMapper.selectDataOperatorById(data_operator_id);
        if(dt == null)
            throw new Exception("no dataoperator");
        if(!dt.getPassword().equals(old_password))
            throw new Exception("密码错误");
        if(1!=dataOperatorMapper.updateDataOperatorPassword(data_operator_id, new_password))
            throw new Exception("更新失败，数据库更新失败");
    }

    @Override
    public void deleteDataOperator(String data_operator_id) throws Exception {
        if(dataOperatorMapper.selectDataOperatorById(data_operator_id) == null)
            throw new Exception("no dataoperator");
        dataOperatorMapper.deleteDataOperator(data_operator_id);
    }

    @Override
    public DataOperatorInfo selectDataOperatorById(String id) throws Exception {
        DataOperator dt = dataOperatorMapper.selectDataOperatorById(id);
        if(dt == null)
            throw new Exception("no dataoperator");
        return new DataOperatorInfo(dt);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInfomation(UpdateInfo updateInfo) throws Exception {
        DataOperator dto = dataOperatorMapper.selectDataOperatorByUsername(updateInfo.data_operator_id);
        if(dto.getPassword().equals(updateInfo.password)){
            dto.setEmail(updateInfo.email);
            dto.setPhone_number(updateInfo.phone_number);
            dataOperatorMapper.updateDataOperatorDetail(dto);
            return;
        }
        throw new Exception("密码错误");
    }

}
