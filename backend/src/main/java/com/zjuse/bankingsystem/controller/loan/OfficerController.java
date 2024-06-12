package com.zjuse.bankingsystem.controller.loan;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjuse.bankingsystem.service.loan.OfficerLoginService;
import com.zjuse.bankingsystem.service.loan.OfficerService;

import lombok.extern.slf4j.Slf4j;

import com.zjuse.bankingsystem.entity.loan.Officer;
import com.zjuse.bankingsystem.security.service.CurrentUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@PreAuthorize("@roleCheck.isRole('OFFICER')")
public class OfficerController {

    @Autowired
    private OfficerService officerService;
    @Autowired
    private OfficerLoginService loginService;
    @Autowired
    private CurrentUserService currentUserService; 

    @PutMapping("/officer-main/update-officer-password-by-officer")
    public String updateOfficerPasswordByOfficer(@RequestParam String currentPassword, @RequestParam String newPassword) {
        String officerUsername;
        officerUsername = (String)currentUserService.getCurrentUsername().payload;
        Officer officer = loginService.findOfficerByUsername(officerUsername);
        int officer_id = officer.getOfficerId();

        UpdateWrapper<Officer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.and(wrapper->wrapper.eq("officer_id",officer_id).eq("Password", currentPassword)).set("password", newPassword);

        int result = officerService.updatePassword(updateWrapper);
        if (result > 0) {
            return "Password updated successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current password is not correct");
        }
    }


}

