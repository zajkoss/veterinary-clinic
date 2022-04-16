package pl.kurs.veterinaryclinic.commands;

import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.validators.EnumsValidator;
import pl.kurs.veterinaryclinic.validators.NIP;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateDoctorCommand {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Positive
    private BigDecimal salary;

    @NIP
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
}
