package pl.kurs.veterinaryclinic.commands;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CreateVisitCommand {


    private Long doctorIdentity;

    private Long patientIdentity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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
}
