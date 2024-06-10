package com.zjuse.bankingsystem.service.loan;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjuse.bankingsystem.entity.loan.Form;
import com.zjuse.bankingsystem.mapper.loan.FormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormService {

    @Autowired
    FormMapper formMapper;

    public Form getForm(int form_id) { return formMapper.selectById(form_id); }

    public IPage searchForm(Page<Form> formPage,String id_number) {
        return formMapper.selectPage(formPage, new QueryWrapper<Form>().eq("id_number", id_number));
    }
}
