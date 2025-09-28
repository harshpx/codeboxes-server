package com.codeboxes.server.Services;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OTPService {
  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String hostEmail;

  public void sendOTP(String email) throws MessagingException {
    String otp = String.valueOf((int) (Math.random() * 900000) + 100000); // Generate a 6-digit OTP
    String key = "otp:" + email;
    redisTemplate.opsForValue().set(key, otp, 10, TimeUnit.MINUTES);
    emailOTP(email, otp);
  }

  public boolean verifyOTP(String email, String otp) {
    String key = "otp:" + email;
    String trueOtp = redisTemplate.opsForValue().get(key);
    if (trueOtp != null) {
      return trueOtp.equals(otp);
    }
    return false;
  }

  private void emailOTP(String email, String otp) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper template = new MimeMessageHelper(mimeMessage, "utf-8");
    String htmlMsg = "<p style=\"font-size:16px;\">Your one-time password for Codeboxes Email Verification is: <b style=\"color:#06b6d4\">"
        + otp
        + "</b></p>"
        + "<p>It is valid for 10 minutes.</p>"
        + "<br/><p style=\"line-height:8px;\">Regards,</p><p style=\"line-height:8px;\">Harsh Priye (Codeboxes Team)</p>";
    template.setText(htmlMsg, true);
    template.setTo(email);
    template.setSubject("Codeboxes Email Verification (Do Not Reply)");
    template.setFrom(hostEmail);
    mailSender.send(mimeMessage);
  }
}
