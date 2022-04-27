package pl.kurs.veterinaryclinic.commands;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.validators.EnumsValidator;

import java.time.LocalDateTime;

public class QueryAvailableVisitCommand {

    @EnumsValidator(enumClass = DoctorType.class,message = "Invalid value for: type")
    String type;

    @EnumsValidator(enumClass = AnimalType.class,message = "Invalid value for: animal")
    String animal;

    LocalDateTime from;

    LocalDateTime to;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
