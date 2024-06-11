package com.zjuse.bankingsystem.service.loan;

import com.zjuse.bankingsystem.entity.loan.Officer;
import com.zjuse.bankingsystem.mapper.loan.OfficerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficerLoginService {

    @Autowired
    private OfficerMapper officerMapper;

    public Officer findOfficerByUsername(String username) {
        return officerMapper.findByUsername(username);
    }
}
