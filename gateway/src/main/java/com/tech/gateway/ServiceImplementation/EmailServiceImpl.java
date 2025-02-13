//package com.tech.gateway.ServiceImplementation;
//
//
//import com.tech.gateway.services.EmailService.EmailService;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailServiceImpl implements EmailService
//{
//    private JavaMailSender mailSender;
//
//EmailServiceImpl(JavaMailSender mailSender){
//    this.mailSender = mailSender;
//}
//
//    public void sendEmail(String to, String subject, String text
//    ) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("farazkhanofficial12@gmail.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        mailSender.send(message);
//    }
//}
