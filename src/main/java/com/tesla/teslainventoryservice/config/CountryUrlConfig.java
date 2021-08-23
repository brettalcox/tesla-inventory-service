package com.tesla.teslainventoryservice.config;

import com.tesla.teslainventoryservice.model.CountryModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Component
public class CountryUrlConfig {

    private final Map<CountryModel, URI> countryUrls = new ConcurrentHashMap<>();

    public CountryUrlConfig(final @Value("${tesla.m3-us-url}") URI m3UsUrl,
                            final @Value("${tesla.m3-ca-url}") URI m3CaUrl,
                            final @Value("${tesla.my-us-url}") URI myUsUrl) {
        countryUrls.put(CountryModel.US_MODEL3, m3UsUrl);
        countryUrls.put(CountryModel.CA_MODEL3, m3CaUrl);
        countryUrls.put(CountryModel.US_MODELY, myUsUrl);
    }

    public URI getCountryUrl(final CountryModel countryModel) {
        return countryUrls.get(countryModel);
    }

    public Stream<URI> getCountryUrls() {
        return countryUrls.values().stream();
    }
}
