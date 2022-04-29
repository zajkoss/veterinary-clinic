package pl.kurs.veterinaryclinic.commands;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.kurs.veterinaryclinic.validators.VisitTime;

import java.time.LocalDateTime;

public class CreateVisitCommand {


    private Long doctorIdentity;

    private Long patientIdentity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @VisitTime
    private LocalDateTime time;


    public Long getDoctorIdentity() {
        return doctorIdentity;
    }

    public Long getPatientIdentity() {
        return patientIdentity;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setDoctorIdentity(Long doctorIdentity) {
        this.doctorIdentity = doctorIdentity;
    }

    public void setPatientIdentity(Long patientIdentity) {
        this.patientIdentity = patientIdentity;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CreateVisitCommand{" +
                "doctorIdentity=" + doctorIdentity +
                ", patientIdentity=" + patientIdentity +
                ", time=" + time +
                '}';
    }
}
