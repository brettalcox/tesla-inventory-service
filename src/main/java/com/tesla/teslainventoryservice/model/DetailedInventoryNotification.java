package com.tesla.teslainventoryservice.model;

import java.net.URI;
import java.util.List;

public class DetailedInventoryNotification {
    private final List<DetailedTeslaInventory> teslaInventories;

    private final URI notificationUrl;

    public DetailedInventoryNotification(List<DetailedTeslaInventory> teslaInventories, URI notificationUrl) {
        this.teslaInventories = teslaInventories;
        this.notificationUrl = notificationUrl;
    }

    public List<DetailedTeslaInventory> getTeslaInventories() {
        return teslaInventories;
    }

    public URI getNotificationUrl() {
        return notificationUrl;
    }
}
