package com.codeboxes.server.Services;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
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
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

    // HTML version
    String htmlMsg = "<html>"
        + "<body style='font-family:Arial,sans-serif;'>"
        + "<p style='font-size:16px;'>Your one-time password for Codeboxes Email Verification is: "
        + "<b style='color:#06b6d4;'>" + otp + "</b></p>"
        + "<p>It is valid for 10 minutes.</p>"
        + "<br/>"
        + "<p style='line-height:10px;'><i>Regards,</i></p>"
        + "<p style='line-height:10px;'><i>Harsh Priye (Codeboxes Team)</i></p>"
        + "</body>"
        + "</html>";

    // Plain text version (fallback for clients that don't render HTML)
    String textMsg = "Your one-time password for Codeboxes Email Verification is: " + otp + "\n"
        + "It is valid for 10 minutes.\n\n"
        + "Regards,\nCodeboxes Team";

    helper.setText(textMsg, htmlMsg); // first param = plain text, second = HTML
    helper.setTo(email);
    helper.setSubject("Codeboxes Email Verification (Do Not Reply)");
    helper.setFrom("onboarding@codeboxes.in");
    helper.setReplyTo("no-reply@codeboxes.in");

    mailSender.send(mimeMessage);
  }

}
