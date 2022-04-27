package pl.kurs.veterinaryclinic.commands;

import org.springframework.beans.factory.annotation.Value;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.validators.DoctorPatientAvailable;
import pl.kurs.veterinaryclinic.validators.EnumsValidator;
import pl.kurs.veterinaryclinic.validators.UniqueNIPNumber;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


public class CreateDoctorCommand {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Positive
    private BigDecimal salary;

    @Pattern(regexp = "\\d{10}",message = "{nip.message}")
    @UniqueNIPNumber
    private String nip;

    @EnumsValidator(enumClass = DoctorType.class,message = "Invalid value for: type field")
    private String type;

    @EnumsValidator(enumClass = AnimalType.class,message = "Invalid value for: animalType field")
    private String animalType;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getNip() {
        return nip;
    }

    public String  getType() {
        return type;
    }

    public String  getAnimalType() {
        return animalType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }
}
