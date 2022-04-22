package pl.kurs.veterinaryclinic.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.veterinaryclinic.config.Passes;



@Service
@Transactional
public class EmailService implements IEmailService{

    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMessage(String address, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Passes.emailUserName);
        message.setTo(address);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
