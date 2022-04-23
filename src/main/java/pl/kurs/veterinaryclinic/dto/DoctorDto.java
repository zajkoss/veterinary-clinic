package pl.kurs.veterinaryclinic.dto;

import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;

import java.math.BigDecimal;
import java.util.Objects;

public class DoctorDto {

    private Long id;

    private String name;

    private String surname;

    private BigDecimal salary;

    private String nip;

    private Boolean isActive;

    private DoctorType type;

    private AnimalType animalType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public DoctorType getType() {
        return type;
    }

    public void setType(DoctorType type) {
        this.type = type;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorDto doctorDto = (DoctorDto) o;
        return Objects.equals(id, doctorDto.id) && Objects.equals(name, doctorDto.name) && Objects.equals(surname, doctorDto.surname) && Objects.equals(salary, doctorDto.salary) && Objects.equals(nip, doctorDto.nip) && Objects.equals(isActive, doctorDto.isActive) && type == doctorDto.type && animalType == doctorDto.animalType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, salary, nip, isActive, type, animalType);
    }

    @Override
    public String toString() {
        return "DoctorDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", salary=" + salary +
                ", nip='" + nip + '\'' +
                ", isActive=" + isActive +
                ", type=" + type +
                ", animalType=" + animalType +
                '}';
    }
}
