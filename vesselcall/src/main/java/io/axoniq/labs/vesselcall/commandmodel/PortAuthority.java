package io.axoniq.labs.vesselcall.commandmodel;

import io.axoniq.labs.vesselcall.coreapi.*;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
public class PortAuthority {

    @AggregateIdentifier
    private String portId;

    @CommandHandler
    public PortAuthority(CreatePortAuthorityCommand cmd) {
        apply(new PortAuthorityCreatedEvent(cmd.getPortId()));
    }

    @EventSourcingHandler
    public void handle(PortAuthorityCreatedEvent portAuthorityCreatedEvent) {
        this.portId = portAuthorityCreatedEvent.getPortId();
    }
}
