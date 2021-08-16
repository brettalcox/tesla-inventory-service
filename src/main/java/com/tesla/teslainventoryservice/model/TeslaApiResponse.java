package com.tesla.teslainventoryservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TeslaApiResponse {

    @JsonProperty("results")
    private List<OfficialTeslaInventory> results;

    @JsonProperty("total_matches_found")
    private String totalMatchesFound;

    public List<OfficialTeslaInventory> getResults() {
        return results;
    }

    public void setResults(List<OfficialTeslaInventory> results) {
        this.results = results;
    }

    public String getTotalMatchesFound() {
        return totalMatchesFound;
    }

    public void setTotalMatchesFound(String totalMatchesFound) {
        this.totalMatchesFound = totalMatchesFound;
    }
}
