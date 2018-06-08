package io.axoniq.labs.vesselcall.coreapi;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WasteItem {
    String type;
    String description;
    String qtyDelivered;
    String berth;
}
