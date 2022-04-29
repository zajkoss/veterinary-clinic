package pl.kurs.veterinaryclinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VisitTimeException extends RuntimeException {

    private Long token;
    private LocalDateTime executeTime;
    private final LocalDateTime visitTime;

    public VisitTimeException(String message, Long token, LocalDateTime executeTime, LocalDateTime visitTime) {
        super(message +
                "token=" + token +
                ", executeTime=" + executeTime +
                ", visitTime=" + visitTime);
        this.token = token;
        this.executeTime = executeTime;
        this.visitTime = visitTime;
    }

    public VisitTimeException(String message, LocalDateTime visitTime) {
        super(message + ", visitTime=" + visitTime);
        this.visitTime = visitTime;
    }

    public Long getToken() {
        return token;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }


}
