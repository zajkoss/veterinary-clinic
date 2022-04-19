package pl.kurs.veterinaryclinic.service;

public interface IEmailService {

    void sendMessage(String address,String subject,String text);

}
