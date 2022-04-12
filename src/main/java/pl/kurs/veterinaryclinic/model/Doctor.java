package pl.kurs.veterinaryclinic.model;

import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "doctors")
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false,length = 10)
    private String nip;

    @Column(nullable = false)
    private Boolean isActive;

    private DoctorType type;

    private AnimalType animalType;

    public Doctor() {}

    public Doctor(String name, String surname, BigDecimal salary, String nip, Boolean isActive, DoctorType type, AnimalType animalType) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.nip = nip;
        this.isActive = isActive;
        this.type = type;
        this.animalType = animalType;
    }

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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
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
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id) && Objects.equals(name, doctor.name) && Objects.equals(surname, doctor.surname) && Objects.equals(salary, doctor.salary) && Objects.equals(nip, doctor.nip) && Objects.equals(isActive, doctor.isActive) && type == doctor.type && animalType == doctor.animalType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, salary, nip, isActive, type, animalType);
    }

    @Override
    public String toString() {
        return "Doctor{" +
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
