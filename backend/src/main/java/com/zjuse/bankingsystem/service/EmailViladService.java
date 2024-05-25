package com.zjuse.bankingsystem.service;

import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.JavaMailUtil;
import com.zjuse.bankingsystem.utils.RedisUtils;

@Service
public class EmailViladService {
    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RedisUtils user;
    static final String serviceEmail = "zju_11111111@163.com" ;
    static final String password = "CUYRWKBTMCEIYACA";


    public boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }

    private Session createSession() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		// props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.user", serviceEmail);
		props.put("mail.smtp.pwd", password);
		
		Session session = Session.getInstance(props,null);
		session.setDebug(true);
		return session;
 
	}


    public ApiResult sendEmail(String email, Long userId) {
        try {
            if (!isEmailValid(email)) {
                return new ApiResult(false, "email invalid");
            }
            if (user.get(userId.toString()) != null) {
                return new ApiResult(false, "too frequent operation");
            }
            String code = String.format("%06d",Integer.valueOf((int)(Math.random() * 1000000)));
            Session session = createSession();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(serviceEmail));
            // message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("online-banking-system");
            message.setText("：" + code + "，");

            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.163.com", serviceEmail, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            redisUtils.set(email, code, 300);
            user.set(userId.toString(), 1, 60);
            return new ApiResult(true, "success", code);
        }
        catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult validCode(String email, String code) {
        String realCode = (String) redisUtils.get(email);
        if (realCode == null) {
            return new ApiResult(false, "验证码已过期");
        }
        if (realCode.equals(code)) {
            return new ApiResult(true, "success");
        }
        return new ApiResult(false, "验证码错误");
    }
}
