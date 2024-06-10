package com.zjuse.bankingsystem.service.loan;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjuse.bankingsystem.entity.loan.Manager;
import com.zjuse.bankingsystem.entity.loan.Officer;
import com.zjuse.bankingsystem.mapper.loan.OfficerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OfficerService {

    @Autowired
    OfficerMapper officerMapper;

    public int updatePassword(UpdateWrapper<Officer> updateWrapper) { return officerMapper.update(updateWrapper); }


    public String insertOfficer(Officer officer) {
        int result = officerMapper.insert(officer);
        if (result > 0) {
            return "Officer inserted successfully!";
        } else {
            return "Failed to insert officer.";
        }
    }

    public String updateOfficerPassword(String username, String newPassword) {
        UpdateWrapper<Officer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username).set("password", newPassword);
        int result = officerMapper.update(null, updateWrapper);
        if (result > 0) {
            return "Password updated successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Officer not found");
        }
    }

    public String updateOfficerPermission(String username, String newPermission) {
        UpdateWrapper<Officer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username).set("permissions", newPermission);
        int result = officerMapper.update(null, updateWrapper);
        if (result > 0) {
            return "Permission updated successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Officer not found");
        }
    }

    public IPage<Officer> getOfficers(int page, int pageSize) {
        Page<Officer> officerPage = new Page<>(page, pageSize);
        return officerMapper.selectPage(officerPage, null);
    }

    public String deleteOfficer(int officerId) {
        int result = officerMapper.deleteById(officerId);
        if (result > 0) {
            return "Officer deleted successfully!";
        } else {
            return "Failed to delete officer.";
        }
    }
}
