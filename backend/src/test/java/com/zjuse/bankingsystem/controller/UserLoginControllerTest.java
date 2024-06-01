package com.zjuse.bankingsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerTest {
    @Autowired
    private MockMvc mockMvc; 

    private String tryLogin() throws Exception {
        String user = "{\"username\":\"zxy\", \"password\":\"8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92\"}";
        RequestBuilder request = post("/user/login")
            .content(user)
            .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andDo(print())
            .andReturn(); 
        String jsonString = result.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(jsonString);
        return obj.getJSONObject("payload").getStr("token");
    }

    private boolean tryProfile(String token) throws Exception {
        RequestBuilder request = get("/user/profile")
            .header("Authorization", token);
        MvcResult result = mockMvc.perform(request)
            .andReturn(); 
        if (result.getResponse().getStatus() != 200) return false; 
        String jsonString = result.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(jsonString);
        log.info(obj.getJSONObject("payload").getStr("username"));
        return true; 
    }

    @Test
    void userLoginTest() throws Exception {
        String token = tryLogin();
        log.info(token); 
    }

    @Test
    void userProfileTest() throws Exception {
        String token = tryLogin();
        boolean result = tryProfile(token);
        assert result != false; 
    }

    @Test 
    void userLogoutTest() throws Exception {
        String token = tryLogin();
        boolean result = tryProfile(token);
        assert result != false; 
        RequestBuilder request = delete("/user/logout")
            .header("Authorization", token);
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andDo(print());
        result = tryProfile(token);
        assert result == false; 
    }

    @Test
    void updateUserProfileTest() throws Exception {
        String token = tryLogin(); 
        
        Map<String, String> map = new HashMap<>();
        map.put("username", "zxy"); 
        map.put("password", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        map.put("new_password", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c96");

        JSONObject json = new JSONObject(map);
        String jsonString = json.toString();

        RequestBuilder request = post("/user/profile/update")
            .header("Authorization", token)
            .content(jsonString)
            .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andDo(print());

    }
}
