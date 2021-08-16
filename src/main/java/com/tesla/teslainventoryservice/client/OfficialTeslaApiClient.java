package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.TeslaApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class OfficialTeslaApiClient {

    private final RestTemplate restTemplate;

    private final URI officialApiUrl;

    public OfficialTeslaApiClient(final RestTemplate restTemplate,
                                  @Value("${tesla.official-api-url}") final URI officialApiUrl) {
        this.restTemplate = restTemplate;
        this.officialApiUrl = officialApiUrl;
    }

    public TeslaApiResponse getOfficialTeslaInventory() {
        return restTemplate.getForObject(officialApiUrl, TeslaApiResponse.class);
    }
}
