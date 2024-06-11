package com.zjuse.bankingsystem.controller.loan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjuse.bankingsystem.service.loan.FormService;
import com.zjuse.bankingsystem.service.loan.FormInsertService;
import com.zjuse.bankingsystem.entity.loan.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FormController {

    @Autowired
    private FormInsertService formInsertService;
    @Autowired
    private FormService formService;

    @PostMapping("/add-form")
    public Map<String, Object> insertForm(@RequestBody Form form) {
        int result = formInsertService.insertform(form);
        Map<String, Object> response = new HashMap<>();
        if (result > 0) {
            response.put("message", "Form created successfully!");
            response.put("id", form.getForm_id()); // 返回生成的表单ID
        } else {
            response.put("message", "Failed to create form.");
        }
        return response;
    }

    @GetMapping("/get-forms/{form_id}")
    public Form getFormDetails(@PathVariable("form_id") int form_id) {
        return formService.getForm(form_id);
    }

    @GetMapping("/search-forms/{id_number}")
    public IPage searchForms(@PathVariable("id_number") String id_number, @RequestParam int page, @RequestParam int pageSize) {
        Page<Form> formPage = new Page<>(page, pageSize);
        return formService.searchForm(formPage, id_number);
    }
}