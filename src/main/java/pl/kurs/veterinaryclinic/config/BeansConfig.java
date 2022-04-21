package pl.kurs.veterinaryclinic.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.Patient;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.repository.DoctorRepository;
import pl.kurs.veterinaryclinic.repository.PatientRepository;
import pl.kurs.veterinaryclinic.repository.VisitRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }


//    @Bean
//    public ApplicationRunner dataInitializer(DoctorRepository doctorRepository, PatientRepository patientRepository, VisitRepository visitRepository) {
//        return args -> {
//            Doctor doctor1 = new Doctor("Jan", "Kowalski", new BigDecimal("100.0"), "1234567890", true, DoctorType.EYE_DOCTOR, AnimalType.CAT);
//            Doctor doctor2 = new Doctor("Mirosław", "Janko", new BigDecimal("110.0"), "1234567891", true, DoctorType.CARDIOLOGIST, AnimalType.DOG);
////            Doctor doctor3 = new Doctor("Arkadiusz", "Małysz", new BigDecimal("2220.0"), "1234567892", true, DoctorType.CARDIOLOGIST, AnimalType.CAT);
////            Doctor doctor4 = new Doctor("Tomasz", "Kubica", new BigDecimal("30.0"), "1234567893", true, DoctorType.EYE_DOCTOR, AnimalType.DOG);
////            Doctor doctor5 = new Doctor("Katarzyna", "Lewandowska", new BigDecimal("56.0"), "1234567894", true, DoctorType.DENTIST, AnimalType.HORSE);
////            Doctor doctor6 = new Doctor("Anna", "Kowalczyk", new BigDecimal("44.0"), "1234567895", true, DoctorType.SURGEON, AnimalType.DOG);
//            doctorRepository.saveAll(Arrays.asList(doctor1, doctor2));
////            doctorRepository.saveAll(Arrays.asList(doctor1, doctor2, doctor3, doctor4, doctor5, doctor6));
//            Patient patient1 = new Patient("Szarik", "Pies", "Owczarek", 1, "Tomasz", "Paluch", "a@a.com");
//            Patient patient2 = new Patient("Filemon", "Kot", "Dachowiec", 5, "Dorota", "Mieszko", "b@a.com");
//            Patient patient3 = new Patient("Felix", "Kon", "Czystej krwi arabskiej", 7, "Janina", "Dudu", "c@a.com");
//            Patient patient4 = new Patient("Grizzli", "Pies", "Sznaucer", 3, "Marek", "Kowal", "a@a.com");
//            Patient patient5 = new Patient("Łapka", "Pies", "Maltańczyk", 4, "Marek", "Kowal", "a@a.com");
//            patientRepository.saveAll(Arrays.asList(patient1, patient2, patient3, patient4, patient5));
//            Visit visit1 = new Visit(doctor1,patient1, LocalDateTime.of(2022,5,10,10,0));
//            Visit visit2 = new Visit(doctor1,patient2, LocalDateTime.of(2022,5,10,11,0));
////            Visit visit3 = new Visit(doctor1,patient3, LocalDateTime.of(2022,5,10,12,0));
////            Visit visit4 = new Visit(doctor1,patient4, LocalDateTime.of(2022,5,11,13,0));
////            Visit visit5 = new Visit(doctor1,patient5, LocalDateTime.of(2022,5,11,14,0));
////            Visit visit6 = new Visit(doctor2,patient4, LocalDateTime.of(2022,5,10,10,0));
////            Visit visit7 = new Visit(doctor2,patient5, LocalDateTime.of(2022,5,10,11,0));
////            Visit visit8 = new Visit(doctor2,patient1, LocalDateTime.of(2022,5,11,12,0));
////            Visit visit9 = new Visit(doctor2,patient2, LocalDateTime.of(2022,5,11,13,0));
////            Visit visit10 = new Visit(doctor2,patient3, LocalDateTime.of(2022,5,11,14,0));
//            visitRepository.saveAll(Arrays.asList(visit1, visit2));
////            visitRepository.saveAll(Arrays.asList(visit1, visit2, visit3, visit4, visit5, visit6, visit7, visit8, visit9, visit10));
//        };
//    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(Passes.emailUserName);
        mailSender.setPassword(Passes.emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
