package com.zjuse.bankingsystem.service.loan;

import com.zjuse.bankingsystem.entity.loan.Form;
import com.zjuse.bankingsystem.mapper.loan.FormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormInsertService {

    @Autowired
    private FormMapper formMapper;

    public int insertform(Form form){
        return  formMapper.insert(form);
    }
}