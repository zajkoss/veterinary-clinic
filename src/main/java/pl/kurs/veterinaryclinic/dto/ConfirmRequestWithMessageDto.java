package pl.kurs.veterinaryclinic.dto;

public class ConfirmRequestWithMessageDto {
    private String message;

    public ConfirmRequestWithMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
