package com.example.context;

import jakarta.inject.Singleton;

// ShipContext holds the current starship name and the officer on duty for each transmission request.
// TODO: The current scope causes all concurrent requests to share the same instance, which means
//       a transmission from the USS Enterprise would overwrite the context of a simultaneous
//       transmission from the USS Voyager.
@Singleton
public class ShipContext {

    private String shipName;
    private String officerOnDuty;

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getOfficerOnDuty() {
        return officerOnDuty;
    }

    public void setOfficerOnDuty(String officerOnDuty) {
        this.officerOnDuty = officerOnDuty;
    }
}
