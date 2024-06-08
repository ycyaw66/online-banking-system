package com.zjuse.bankingsystem.security.service;

import javax.mail.Authenticator;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.config.MailConfig;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RedisUtils;

import cn.hutool.core.lang.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailValidService {
    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private MailConfig mailConfig; 

    @Data
    @AllArgsConstructor
    private class EmailInfo {
        String email;
        String code;
    };

    public boolean isEmailValid(String email) {
        // TODO add email validation
        return true; 
        // return email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }

    private Session createSession() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		// props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.user", mailConfig.getMailAddress());
		props.put("mail.smtp.pwd", mailConfig.getPassword());
		
		Session session = Session.getInstance(props,null);
		session.setDebug(true);
		return session;
 
	}


    public ApiResult sendEmail(String email, String userUuid) {
        try {
            if (!isEmailValid(email)) {
                return new ApiResult(false, "email invalid");
            }
            String uuid = null;
            if (userUuid == null) {
                uuid = UUID.randomUUID().toString();
            }
            else {
                // System.out.println("### " + redisUtils.getExpire(userUuid));
                if (redisUtils.getExpire(userUuid) > 240) {
                    return new ApiResult(false, "too Frequent");
                }
                uuid = userUuid;
            }

            String code = String.format("%06d",Integer.valueOf((int)(Math.random() * 1000000)));
            Session session = createSession();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getMailAddress()));
            message.setSubject("online-banking-system");
            message.setText("验证码：" + code + "，五分钟有效");

            log.info(email);

            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.163.com", mailConfig.getMailAddress(), mailConfig.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            // System.out.println("### " + uuid.hashCode());
            EmailInfo emailInfo = new EmailInfo(email, code);
            System.out.println("xxx " + emailInfo.toString());
            if (!redisUtils.set(uuid, emailInfo.toString(), 300)) {
                return new ApiResult(false, "redis Error");
            }
            return new ApiResult(true, "success", uuid);
        }
        catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult validCode(String uuid, String email, String code) {
        System.out.println("### " + uuid.hashCode());
        String validInfo = (new EmailInfo(email, code)).toString();
        String realInfo = (String) redisUtils.get(uuid);
        System.out.println("### valid:" + validInfo);
        System.out.println("### real:" + realInfo);

        if (realInfo == null) {
            System.out.println("### 验证码已过期");
            return new ApiResult(false, "验证码已过期");
        }
        if (!realInfo.equals(validInfo)) {
            System.out.println("### Wrong code");
            return new ApiResult(false, "Wrong code");
        }
        redisUtils.del(uuid);
        return new ApiResult(true, "ok");
    }
}
