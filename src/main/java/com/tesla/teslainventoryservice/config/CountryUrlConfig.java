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

    // spring is dum and handles the urls differently when running under a @ConfigurationProperties injection (how i'd prefer to do it)
    // so here we are manually injecting each url into the constructor
    public CountryUrlConfig(final @Value("${tesla.m3-us-url}") URI m3UsUrl,
                            final @Value("${tesla.m3-ca-url}") URI m3CaUrl,
                            final @Value("${tesla.my-us-url}") URI myUsUrl,
                            final @Value("${tesla.my-ca-url}") URI myCaUrl,
                            final @Value("${tesla.ms-us-url}") URI msUsUrl,
                            final @Value("${tesla.ms-ca-url}") URI msCaUrl,
                            final @Value("${tesla.mx-us-url}") URI mxUsUrl,
                            final @Value("${tesla.mx-ca-url}") URI mxCaUrl) {
        countryUrls.put(CountryModel.US_MODEL3, m3UsUrl);
        countryUrls.put(CountryModel.CA_MODEL3, m3CaUrl);
        countryUrls.put(CountryModel.US_MODELY, myUsUrl);
        countryUrls.put(CountryModel.CA_MODELY, myCaUrl);
        countryUrls.put(CountryModel.US_MODELS, msUsUrl);
        countryUrls.put(CountryModel.CA_MODELS, msCaUrl);
        countryUrls.put(CountryModel.US_MODELX, mxUsUrl);
        countryUrls.put(CountryModel.CA_MODELX, mxCaUrl);
    }

    public URI getCountryUrl(final CountryModel countryModel) {
        return countryUrls.get(countryModel);
    }

    public Stream<URI> getCountryUrls() {
        return countryUrls.values().stream();
    }
}
