package pl.kurs.veterinaryclinic.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "visit")
public class Visit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visit")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private Boolean confirmed = false;

    @Column(nullable = false)
    private Boolean reminderSent = false;

    @Version
    private Integer version;

    public Visit() {
    }

    public Visit(Doctor doctor, Patient patient, LocalDateTime time) {
        this.doctor = doctor;
        this.patient = patient;
        this.time = time;
    }

    public Long getId() {
        return id;
    }


    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime date) {
        this.time = date;
    }

    public Boolean getReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(Boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(id, visit.id) && Objects.equals(doctor, visit.doctor) && Objects.equals(patient, visit.patient) && Objects.equals(time, visit.time) && Objects.equals(confirmed, visit.confirmed) && Objects.equals(reminderSent, visit.reminderSent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor, patient, time, confirmed, reminderSent);
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", date=" + time +
                ", confirmed=" + confirmed +
                ", reminderSent=" + reminderSent +
                '}';
    }
}
