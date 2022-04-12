package pl.kurs.veterinaryclinic.commands;

import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;

import java.math.BigDecimal;

public class CreateDoctorCommand {

    private String name;

    private String surname;

    private BigDecimal salary;

    private String nip;

    private Boolean isActive;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public DoctorType getType() {
        return type;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }
}
