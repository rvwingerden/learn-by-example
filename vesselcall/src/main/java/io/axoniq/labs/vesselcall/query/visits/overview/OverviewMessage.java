package io.axoniq.labs.vesselcall.query.visits.overview;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OverviewMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String crn;
    private String port;
    private String vessel;

    public OverviewMessage() {
    }

    public OverviewMessage(String crn, String vessel, String port) {
        this.crn = crn;
        this.port = port;
        this.vessel = vessel;
    }

    public String getCrn() {
        return crn;
    }

    public String getPort() {
        return port;
    }

    public String getVessel() {
        return vessel;
    }
}
