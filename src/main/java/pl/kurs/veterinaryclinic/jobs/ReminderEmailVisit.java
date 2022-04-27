package pl.kurs.veterinaryclinic.jobs;

import freemarker.template.TemplateException;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.kurs.veterinaryclinic.service.EmailService;
import pl.kurs.veterinaryclinic.service.IEmailService;
import pl.kurs.veterinaryclinic.service.IVisitService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReminderEmailVisit {

    private static final String emailSubject = "Reminder visit";
    private static final String emailMessage = "We kindly inform you about your appointment tomorrow at: ";
    private Logger logger = LoggerFactory.getLogger(ReminderEmailVisit.class);

    private IVisitService visitService;

    private IEmailService emailService;

    public ReminderEmailVisit(IVisitService visitService, IEmailService emailService) {
        this.visitService = visitService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 31 20 * * ?", zone = "Europe/Warsaw")
    @SchedulerLock(name = "ReminderEmailVisit_sendReminders", lockAtLeastFor = "PT5M", lockAtMostFor = "PT20M")
    public void sendReminders() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        visitService.findAllVisitForNextDayWithoutSendReminder().forEach(visit -> {
            String content = emailMessage + visit.getTime().format(dateTimeFormatter);
            try {
                emailService.sendMessageWithHTMLContent(visit.getPatient().getEmail(), emailSubject, emailService.getEmailContentForRemindVisit(content));
            } catch (MessagingException | IOException | TemplateException exception) {
                emailService.sendMessage(visit.getPatient().getEmail(), emailSubject, content);
                logger.error("Error occurred for sending reminder email with template", exception);
            }
            visit.setReminderSent(true);
            visitService.setReminderOfVisit(visit);
        });
    }


}
