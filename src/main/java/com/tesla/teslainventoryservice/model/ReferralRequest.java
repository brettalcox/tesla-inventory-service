package com.tesla.teslainventoryservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReferralRequest {

    private final String referralId;

    @JsonCreator
    public ReferralRequest(@JsonProperty("referralId") final String referralId) {
        this.referralId = referralId;
    }

    public String getReferralId() {
        return referralId;
    }
}
