package com.tesla.teslainventoryservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DetailedTeslaInventory {
    private final String url;

    private final String name;

    private final Map<String, String> attributes;

    @JsonCreator
    public DetailedTeslaInventory(@JsonProperty("url") final String url, @JsonProperty("name") final String name, @JsonProperty("attributes") final Map<String, String> attributes) {
        this.url = url;
        this.name = name;
        this.attributes = attributes;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
