package pl.kurs.veterinaryclinic.model;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long id;

    private String token;

    @OneToOne(targetEntity = Visit.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "visit_id")
    private Visit visit;

    private LocalDateTime expiryDate;

    public ConfirmationToken() {}

    public ConfirmationToken(String token, Visit visit) {
        this.token = token;
        this.visit = visit;
        this.expiryDate = LocalDateTime.now().plusDays(1);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }


}
