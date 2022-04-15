package pl.kurs.veterinaryclinic.commands;

import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
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

    private DoctorType type;

    private AnimalType animalType;

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

    public DoctorType getType() {
        return type;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }
}
