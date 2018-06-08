package io.axoniq.labs.vesselcall.coreapi;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
@Builder(toBuilder = true)
public class Visit {
    String crn;
    String vessel;
    String port;
    String accessPoint;
    List<String> berths;

    public boolean hasBerthRegistered(String berth) {
        return berths!=null && berths.contains(berth);
    }
}
