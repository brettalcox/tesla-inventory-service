package com.tesla.teslainventoryservice.model;

public class TeslaInventory {
    private String name;

    private String url;

    private String imageUrl;

    public TeslaInventory(final String name, final String url, final String imageUrl) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "TeslaInventory{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

