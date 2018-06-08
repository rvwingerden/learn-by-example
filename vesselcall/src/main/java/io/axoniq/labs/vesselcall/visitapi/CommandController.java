package io.axoniq.labs.vesselcall.visitapi;

import io.axoniq.labs.vesselcall.coreapi.*;
import io.axoniq.labs.vesselcall.query.visits.overview.OverviewMessage;
import lombok.Data;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Random;
import java.util.concurrent.Future;

import static java.util.Arrays.asList;

@RestController
public class CommandController {

    private final CommandGateway commandGateway;

    public CommandController(@SuppressWarnings("SpringJavaAutowiringInspection") CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/visits")
    public Future<String> createVisit(@RequestBody @Valid VisitRequest req) {
        String crn = req.getPort() + new Random().nextInt(6);
        return commandGateway.send(new CreateVisitCommand(crn, req.getVessel(), req.getPort(), req.getAccessPoint()));
    }

    @PutMapping("/visits/{crn}")
    public Future<String> updateVisit(@PathVariable @Valid String crn, @RequestBody @Valid SaveVisitRequest req) {
        return commandGateway.send(new SaveVisitCommand(crn, req.getAccessPoint()));
    }

    @GetMapping("/visits/{crn}")
    public Visit updateVisit(@PathVariable @Valid String crn) {
        return commandGateway.sendAndWait(new GetVisitCommand(crn));
    }

    @PutMapping("/visits/{crn}/berths")
    public Future<String> addBerth(@PathVariable @Valid String crn, @RequestBody @Valid AddBerthRequest req) {
        return commandGateway.send(new AddBerthCommand(crn, req.getBerth()));
    }

    @DeleteMapping("/visits/{crn}/berths")
    public Future<String> deleteBerth(@PathVariable @Valid String crn, @RequestBody @Valid DeleteBerthRequest req) {
        return commandGateway.send(new DeleteBerthCommand(crn, req.getBerth()));
    }

    @GetMapping("/visits/{crn}/send2pa")
    public Future<String> send2pa(@PathVariable @Valid String crn) {
        return commandGateway.send(new DeclareVisit2PaCommand(crn));
    }

    @PostMapping("/waste/{crn}")
    public Future<String> createWaste(@PathVariable @Valid String crn) {
        return commandGateway.send(new CreateWasteCommand(crn, asList(WasteItem.builder().type("sludge").description("schmutzig").qtyDelivered("1000").berth("a").build())));
    }

    @PutMapping("/waste/{crn}")
    public Future<String> updateWaste(@PathVariable @Valid String crn) {
        return commandGateway.send(new SaveWasteCommand(crn, asList(WasteItem.builder().type("sludge").description("schmutzig").qtyDelivered("1000").berth("b").build())));
    }


    @GetMapping("/waste/{crn}")
    public Waste getWaste(@PathVariable @Valid String crn) {
        return commandGateway.sendAndWait(new GetWasteCommand(crn));
    }


    public static class VisitRequest {
        @NotEmpty
        private String vessel;

        @NotEmpty
        private String port;

        @NotEmpty
        private String accessPoint;

        public String getVessel() {
            return vessel;
        }

        public String getPort() {
            return port;
        }

        public String getAccessPoint() {
            return accessPoint;
        }
    }

    public static class SaveVisitRequest {
        @NotEmpty
        private String accessPoint;

        public String getAccessPoint() {
            return accessPoint;
        }
    }

    public static class AddBerthRequest {
        @NotEmpty
        private String berth;

        public String getBerth() {
            return berth;
        }
    }

    public static class DeleteBerthRequest {
        @NotEmpty
        private String berth;

        public String getBerth() {
            return berth;
        }
    }
}
