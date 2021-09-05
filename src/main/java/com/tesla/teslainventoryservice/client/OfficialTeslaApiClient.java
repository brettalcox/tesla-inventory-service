package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.TeslaApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class OfficialTeslaApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficialTeslaApiClient.class);

    private final RestTemplate restTemplate;

    public OfficialTeslaApiClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(backoff = @Backoff(delay = 100))
    public TeslaApiResponse getOfficialTeslaInventory(final URI inventoryURI) {
        LOGGER.debug("Calling {}", inventoryURI);
        return restTemplate.getForObject(inventoryURI, TeslaApiResponse.class);
    }
}
