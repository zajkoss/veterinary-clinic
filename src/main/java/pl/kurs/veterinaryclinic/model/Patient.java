package pl.kurs.veterinaryclinic.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "patient")
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column
    private String breed;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String ownerSurname;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private Set<Visit> visits = new HashSet<>();

    @Version
    private Integer version;

    public Patient() {
    }

    public Patient(String name, String species, String breed, Integer age, String ownerName, String ownerSurname, String email, Set<Visit> visits) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.email = email;
        this.visits = visits;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSurname() {
        return ownerSurname;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.ownerSurname = ownerSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id) && Objects.equals(name, patient.name) && Objects.equals(species, patient.species) && Objects.equals(breed, patient.breed) && Objects.equals(age, patient.age) && Objects.equals(ownerName, patient.ownerName) && Objects.equals(ownerSurname, patient.ownerSurname) && Objects.equals(email, patient.email) && Objects.equals(visits, patient.visits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, species, breed, age, ownerName, ownerSurname, email, visits);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", ownerName='" + ownerName + '\'' +
                ", ownerSurname='" + ownerSurname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
