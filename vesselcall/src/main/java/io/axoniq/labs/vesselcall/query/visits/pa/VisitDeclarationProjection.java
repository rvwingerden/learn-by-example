package io.axoniq.labs.vesselcall.query.visits.pa;

import io.axoniq.labs.vesselcall.coreapi.PaApprovedCommand;
import io.axoniq.labs.vesselcall.coreapi.PaRejectedCommand;
import io.axoniq.labs.vesselcall.coreapi.Visit;
import io.axoniq.labs.vesselcall.coreapi.Visit2PADeclaredEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/visitdeclarations")
public class VisitDeclarationProjection {

    private List<VisitDeclaration> overview = new ArrayList<>();

    private final CommandGateway commandGateway;

    public VisitDeclarationProjection(@SuppressWarnings("SpringJavaAutowiringInspection") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @GetMapping
    public Page<VisitDeclaration> visitoverview(@RequestParam(defaultValue = "0") int pageId,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return new PageImpl(overview);
    }

    @GetMapping("/visitdeclarations/{crn}/approve")
    public Future<String> approve(@PathVariable String crn) {
        return commandGateway.send(new PaApprovedCommand(crn));
    }

    @GetMapping("/visitdeclarations/{crn}/reject")
    public Future<String> reject(@PathVariable String crn, String reason) {
        return commandGateway.send(new PaRejectedCommand(crn, reason));
    }


    @EventHandler
    public void handle(Visit2PADeclaredEvent visit2PADeclaredEvent) {
        Visit visit = visit2PADeclaredEvent.getVisit();
        VisitDeclaration visitDeclaration = new VisitDeclaration(visit2PADeclaredEvent.getCrn(), visit.getPort(), visit.getVessel(), visit.getAccessPoint(), visit.getBerths());
        overview.add(visitDeclaration);
    }

}
