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
public class VesselCall {

    @AggregateIdentifier
    private String crn;

    Visit visit;
    Waste waste;

    Declaration paDeclaration;
    Declaration wasteDeclaration;

    @CommandHandler
    public VesselCall(CreateVisitCommand createVisitCommand) {
        apply(new VisitCreatedEvent(createVisitCommand.getCrn(), createVisitCommand.getVessel(), createVisitCommand.getPort(), createVisitCommand.getAccessPoint()));
    }

    @CommandHandler
    public void handle(SaveVisitCommand saveVisitCommand) {
        apply(new VisitSavedEvent(saveVisitCommand.getCrn(), saveVisitCommand.getAccessPoint()));
    }

    @CommandHandler
    public Visit handle(GetVisitCommand getVisitCommand) {
        return this.visit;
    }

    @CommandHandler
    public Waste handle(GetWasteCommand command) {
        return this.waste;
    }

    @CommandHandler
    public void handle(AddBerthCommand addBerthCommand) {
        if (visit.hasBerthRegistered(addBerthCommand.getBerth()))
            throw new IllegalStateException("cant add berth twice... ;-)");
        apply(new BerthAddedEvent(addBerthCommand.getCrn(), addBerthCommand.getBerth()));
    }

    @CommandHandler
    public void handle(DeleteBerthCommand deleteBerthCommand) {
        if (!visit.hasBerthRegistered(deleteBerthCommand.getBerth()))
            throw new IllegalStateException("cant delete berth that isnt there... ;-)");
        if (waste.willBeDeliveredAt(deleteBerthCommand.getBerth()))
            throw new IllegalStateException("cant delete berth because it is wasted... ;-)");
        apply(new BerthDeletedEvent(deleteBerthCommand.getCrn(), deleteBerthCommand.getBerth()));
    }

    @CommandHandler
    public void handle(DeclareVisit2PaCommand declareVisitCommand) {
        if (this.paDeclaration == Declaration.DECLARED) {
            throw new IllegalStateException("visit already declared!");
        }
        apply(new Visit2PADeclaredEvent(declareVisitCommand.getCrn(), this.visit.toBuilder().build()));
    }

    @CommandHandler
    public void handle(PaApprovedCommand command) {
        if (this.paDeclaration != Declaration.DECLARED) {
            throw new IllegalStateException("visit already accepted or rejected!");
        }
        apply(new PaApprovedEvent(command.getCrn()));
    }

    @CommandHandler
    public void handle(PaRejectedCommand command) {
        if (this.paDeclaration != Declaration.DECLARED) {
            throw new IllegalStateException("visit already accepted or rejected!");
        }
        apply(new PaRejectedEvent(command.getCrn(), command.getReason()));
    }

    @CommandHandler
    public void handle(Waste2SingleWindowCommand command) {
        if (this.wasteDeclaration== Declaration.DECLARED) {
            throw new IllegalStateException("waste already declared!");
        }
        apply(new Waste2SingleWindowDeclaredEvent(command.getCrn(), this.waste.toBuilder().build()));
    }


    @CommandHandler
    public void handle(CreateWasteCommand command) {
        if (this.waste!= null) {
            throw new IllegalStateException("waste already created!");
        }
        apply(new WasteCreatedEvent(command.getCrn(), command));
    }

    @CommandHandler
    public void handle(SaveWasteCommand command) {
        if (this.waste== null) {
            throw new IllegalStateException("waste not created yet!");
        }
        apply(new WasteSavedEvent(command.getCrn(), command));
    }



    @EventSourcingHandler
    public void handle(VisitCreatedEvent visitCreatedEvent) {
        this.crn = visitCreatedEvent.getCrn();
        this.visit = Visit.builder()
                .crn(visitCreatedEvent.getCrn())
                .port(visitCreatedEvent.getPort())
                .vessel(visitCreatedEvent.getVessel())
                .accessPoint(visitCreatedEvent.getAccessPoint())
                .build();
    }


    @EventSourcingHandler
    public void handle(VisitSavedEvent visitSavedEvent) {
        visit.setAccessPoint(visitSavedEvent.getAccessPoint());
    }


    @EventSourcingHandler
    public void handle(BerthAddedEvent berthAddedEvent) {
        if (visit.getBerths()==null) {
            visit.setBerths(new ArrayList<>());
        }
        visit.getBerths().add(berthAddedEvent.getBerth());
    }

    @EventSourcingHandler
    public void handle(BerthDeletedEvent berthDeletedEvent) {
        visit.getBerths().remove(berthDeletedEvent.getBerth());
    }

    @EventSourcingHandler
    public void handle(Visit2PADeclaredEvent visitDeclaredEvent) {
        paDeclaration = Declaration.DECLARED;
    }

    @EventSourcingHandler
    public void handle(Waste2SingleWindowDeclaredEvent waste2SingleWindowDeclaredEvent) {
        wasteDeclaration = Declaration.DECLARED;
    }

    @EventSourcingHandler
    public void handle(WasteCreatedEvent wasteCreatedEvent) {
        this.waste = Waste.builder().wasteItems(wasteCreatedEvent.getWasteCommand().getWasteItems()).build();
    }



    @EventHandler
    public void handle(PaApprovedEvent paApprovedEvent) {
        paDeclaration = Declaration.ACCEPTED;
    }

    @EventHandler
    public void handle(PaRejectedEvent paRejectedEvent) {
        paDeclaration = Declaration.REJECTED;
    }


}
