package com.zjuse.bankingsystem.controller.creditCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.service.creditCard.InspectorService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/credit-card/inspector")
public class InspectorController {
    @Autowired
    private InspectorService inspectorService;

    @PostMapping("/request")
    public RespResult queryRequestsByInspector(@RequestParam Integer permission) {
        ApiResult apiResult = inspectorService.queryRequestsByInspector(permission);
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/request/accept")
    public RespResult acceptRequest(@RequestParam Long id) {
        ApiResult apiResult = inspectorService.acceptRequest(id);
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/request/reject")
    public RespResult rejectRequest(@RequestParam Long id) {
        ApiResult apiResult = inspectorService.rejectRequest(id);
        return RespResult.success(apiResult.payload);
    }
}
