package com.tesla.teslainventoryservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TeslaInventory {
    private final String name;

    private final String url;

    private final String imageUrl;

    @JsonCreator
    public TeslaInventory(@JsonProperty("name") final String name,
                          @JsonProperty("url") final String url,
                          @JsonProperty("imageUrl") final String imageUrl) {
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

