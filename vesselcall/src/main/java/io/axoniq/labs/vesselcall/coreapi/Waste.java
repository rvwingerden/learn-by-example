package io.axoniq.labs.vesselcall.coreapi;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@Builder(toBuilder = true)
public class Waste {
    List<WasteItem> wasteItems;

    public boolean willBeDeliveredAt(String berth) {
        return wasteItems.stream().anyMatch(wasteItem -> wasteItem.getBerth().equals(berth));
    }
}
