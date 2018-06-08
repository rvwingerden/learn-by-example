package io.axoniq.labs.vesselcall.query.visits.overview;

import io.axoniq.labs.vesselcall.coreapi.VisitCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/visits/{port}/overview")
public class OverviewMessageProjection {

    private Map<String, List<OverviewMessage>> overview = new HashMap<>();

    public OverviewMessageProjection() {}

    @GetMapping
    public Page<OverviewMessage> visitoverview(@PathVariable String port,
                                                @RequestParam(defaultValue = "0") int pageId,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return new PageImpl(overview.get(port));
    }


    @EventHandler
    public void handle(VisitCreatedEvent visitCreatedEvent) {
        OverviewMessage overviewMessage = new OverviewMessage(visitCreatedEvent.getCrn(), visitCreatedEvent.getVessel(), visitCreatedEvent.getPort());
        List<OverviewMessage> overviewMessages = Optional.ofNullable(overview.get(visitCreatedEvent.getPort())).orElse(new ArrayList<>());
        overviewMessages.add(overviewMessage);
        overview.put(visitCreatedEvent.getPort(), overviewMessages);
    }

}
