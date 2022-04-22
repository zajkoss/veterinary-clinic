package pl.kurs.veterinaryclinic.events;

import org.springframework.context.ApplicationEvent;
import pl.kurs.veterinaryclinic.model.Visit;

public class OnMakeVisitEvent extends ApplicationEvent {
    private Visit visit;
    private String url;

    public OnMakeVisitEvent(Visit visit, String url) {
        super(visit);
        this.visit = visit;
        this.url = url;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
