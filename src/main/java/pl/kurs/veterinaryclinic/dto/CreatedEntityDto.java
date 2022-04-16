package pl.kurs.veterinaryclinic.dto;

public class CreatedEntityDto {
    private Long id;

    public CreatedEntityDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
