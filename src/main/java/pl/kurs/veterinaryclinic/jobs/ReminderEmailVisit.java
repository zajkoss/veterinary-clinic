package pl.kurs.veterinaryclinic.jobs;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.kurs.veterinaryclinic.service.IEmailService;
import pl.kurs.veterinaryclinic.service.IVisitService;

@Service
public class ReminderEmailVisit {

    private static final String emailSubject = "Reminder visit";
    private static final String emailMessage = "We kindly inform you about your appointment tomorrow at: ";

    private IVisitService visitService;

    private IEmailService emailService;

    public ReminderEmailVisit(IVisitService visitService, IEmailService emailService) {
        this.visitService = visitService;
        this.emailService = emailService;
    }

//    @Scheduled(fixedDelay = 2000)
    @Scheduled(cron = "0 00 23 * * ?",zone="Europe/Warsaw")
    public void sendReminders() {
        System.out.println("Send reminder");
        visitService.findAllVisitForNextDayWithoutSendReminder().forEach(visit -> {
            System.out.println(visit);
            emailService.sendMessage(visit.getPatient().getEmail(), emailSubject, emailMessage + visit.getTime());
            visit.setReminderSent(true);
            visitService.setReminderOfVisit(visit);
        });
    }

}
