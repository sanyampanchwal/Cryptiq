package com.sanyam.CryptoTrading.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public void sendVerificationOtpMail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

        String subject = "Verify OTP";
        String text = "Your verification OTP is: " + otp;
        helper.setSubject(subject);
        helper.setText(text);
        helper.setTo(email);

        try{
            javaMailSender.send(mimeMessage);
        }
        catch(MailException me){
            throw new MailSendException(me.getMessage());
        }
    }
}
