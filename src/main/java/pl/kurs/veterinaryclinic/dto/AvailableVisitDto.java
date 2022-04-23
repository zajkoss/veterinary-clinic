package pl.kurs.veterinaryclinic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class AvailableVisitDto {

    private DoctorNameDto doctor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    public AvailableVisitDto(DoctorNameDto doctor, LocalDateTime date) {
        this.doctor = doctor;
        this.date = date;
    }

    public DoctorNameDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorNameDto doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableVisitDto that = (AvailableVisitDto) o;
        return Objects.equals(doctor, that.doctor) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctor, date);
    }
}
