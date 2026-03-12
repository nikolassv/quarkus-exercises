package com.example.context;

import jakarta.enterprise.context.RequestScoped;

// ShipContext holds the current starship name and the officer on duty for each transmission request.
@RequestScoped
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
