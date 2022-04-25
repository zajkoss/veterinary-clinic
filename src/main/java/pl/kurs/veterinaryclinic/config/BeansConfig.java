package pl.kurs.veterinaryclinic.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
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
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
//            Doctor doctor3 = new Doctor("Arkadiusz", "Małysz", new BigDecimal("2220.0"), "1234567892", true, DoctorType.CARDIOLOGIST, AnimalType.CAT);
//            Doctor doctor4 = new Doctor("Tomasz", "Kubica", new BigDecimal("30.0"), "1234567893", true, DoctorType.EYE_DOCTOR, AnimalType.DOG);
//            Doctor doctor5 = new Doctor("Katarzyna", "Lewandowska", new BigDecimal("56.0"), "1234567894", true, DoctorType.DENTIST, AnimalType.HORSE);
//            Doctor doctor6 = new Doctor("Anna", "Kowalczyk", new BigDecimal("44.0"), "1234567895", true, DoctorType.SURGEON, AnimalType.DOG);
//            doctorRepository.saveAll(Arrays.asList(doctor1, doctor2));
//            doctorRepository.saveAll(Arrays.asList(doctor1, doctor2, doctor3, doctor4, doctor5, doctor6));
//            Patient patient1 = new Patient("Szarik", "Pies", "Owczarek", 1, "Tomasz", "Paluch", "lukz1184@gmail.com");
//            Patient patient2 = new Patient("Filemon", "Kot", "Dachowiec", 5, "Dorota", "Mieszko", "lukz1184@gmail.com");
//            Patient patient3 = new Patient("Felix", "Kon", "Czystej krwi arabskiej", 7, "Janina", "Dudu", "lukz1184@gmail.com");
//            Patient patient4 = new Patient("Grizzli", "Pies", "Sznaucer", 3, "Marek", "Kowal", "lukz1184@gmail.com");
//            Patient patient5 = new Patient("Łapka", "Pies", "Maltańczyk", 4, "Marek", "Kowal", "lukz1184@gmail.com");
//            patientRepository.saveAll(Arrays.asList(patient1, patient2, patient3, patient4, patient5));
//            Visit visit1 = new Visit(doctor1, patient1, LocalDateTime.of(2022, 5, 10, 10, 0));
//            Visit visit2 = new Visit(doctor1, patient2, LocalDateTime.of(2022, 5, 10, 11, 0));
//            Visit visit3 = new Visit(doctor1,patient3, LocalDateTime.of(2022,5,10,12,0));
//            Visit visit4 = new Visit(doctor1,patient4, LocalDateTime.of(2022,5,11,13,0));
//            Visit visit5 = new Visit(doctor1,patient5, LocalDateTime.of(2022,5,11,14,0));
//            Visit visit6 = new Visit(doctor2,patient4, LocalDateTime.of(2022,5,10,10,0));
//            Visit visit7 = new Visit(doctor2,patient5, LocalDateTime.of(2022,5,10,11,0));
//            Visit visit8 = new Visit(doctor2,patient1, LocalDateTime.of(2022,5,11,12,0));
//            Visit visit9 = new Visit(doctor2,patient2, LocalDateTime.of(2022,5,11,13,0));
//            Visit visit10 = new Visit(doctor2,patient3, LocalDateTime.of(2022,5,11,14,0));
//            visitRepository.saveAll(Arrays.asList(visit1, visit2));
//            visitRepository.saveAll(Arrays.asList(visit1, visit2, visit3, visit4, visit5, visit6, visit7, visit8, visit9, visit10));
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


    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster;
        eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(s -> !s.endsWith("/error"))
                .build();
    }

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }

}
