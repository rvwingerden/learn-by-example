package io.axoniq.labs.vesselcall.query.visits.pa;

import java.util.List;

public class VisitDeclaration {
    String crn;
    String port;
    String vessel;
    String entryPoint;
    List<String> ligplaatsen;

    public VisitDeclaration() {
    }

    public VisitDeclaration(String crn, String port, String vessel, String entryPoint, List<String> ligplaatsen) {
        this.crn = crn;
        this.port = port;
        this.vessel = vessel;
        this.entryPoint = entryPoint;
        this.ligplaatsen = ligplaatsen;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public List<String> getLigplaatsen() {
        return ligplaatsen;
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
