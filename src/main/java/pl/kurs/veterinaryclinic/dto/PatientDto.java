package pl.kurs.veterinaryclinic.dto;

import java.util.Objects;

public class PatientDto {

    private Long id;

    private String name;

    private String species;

    private String breed;

    private Integer age;

    private String ownerName;

    private String ownerSurname;

    private String email;

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
        PatientDto that = (PatientDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(species, that.species) && Objects.equals(breed, that.breed) && Objects.equals(age, that.age) && Objects.equals(ownerName, that.ownerName) && Objects.equals(ownerSurname, that.ownerSurname) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, species, breed, age, ownerName, ownerSurname, email);
    }
}
