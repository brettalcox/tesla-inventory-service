package com.tesla.teslainventoryservice.model;

import java.net.URI;
import java.util.List;

public class InventoryNotification {
    private final List<TeslaInventory> teslaInventories;

    private final URI notificationUrl;

    public InventoryNotification(List<TeslaInventory> teslaInventories, URI notificationUrl) {
        this.teslaInventories = teslaInventories;
        this.notificationUrl = notificationUrl;
    }

    public List<TeslaInventory> getTeslaInventories() {
        return teslaInventories;
    }

    public URI getNotificationUrl() {
        return notificationUrl;
    }
}
