package pl.kurs.veterinaryclinic.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.kurs.veterinaryclinic.events.OnMakeVisitEvent;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.service.EmailService;
import pl.kurs.veterinaryclinic.service.IVisitService;

import java.util.UUID;

@Component
public class MakeVisitListener implements ApplicationListener<OnMakeVisitEvent> {

    private static final String emailSubject = "Visit confirmation";
    private static final String confirmationUrlAddress = "/visit/confirm/";
    private static final String cancelUrlAddress = "/visit/cancel/";


    @Autowired
    private IVisitService visitService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(OnMakeVisitEvent event) {
        this.makeVisitConfirmationToken(event);
    }

    private void makeVisitConfirmationToken(OnMakeVisitEvent event) {
        Visit visit = event.getVisit();
        String generatedToken  = UUID.randomUUID().toString();
        visitService.createConfirmationToken(visit,generatedToken);

        String patientEmail = visit.getPatient().getEmail();
        String confirmationUrl = event.getUrl() + confirmationUrlAddress + generatedToken;
        String cancelUrl = event.getUrl() + cancelUrlAddress + generatedToken;
        String emailMessage = "To confirm your visit click: "  + confirmationUrl + " (link will expire in 24h)" +
                "\nTo cancel your visit click: " + cancelUrl;
        emailService.sendMessage(patientEmail,emailSubject,emailMessage);
    }
}
