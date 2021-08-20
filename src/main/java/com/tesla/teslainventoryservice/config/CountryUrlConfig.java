package com.tesla.teslainventoryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.stream.Stream;

@Component
public class CountryUrlConfig {

    private final URI m3UsUrl;

    private final URI m3CaUrl;

    private final URI myUsUrl;

//    private final URI myCaUrl;

    public CountryUrlConfig(final @Value("${tesla.m3-us-url}") URI m3UsUrl,
                            final @Value("${tesla.m3-ca-url}") URI m3CaUrl,
                            final @Value("${tesla.my-us-url}") URI myUsUrl) {
        this.m3UsUrl = m3UsUrl;
        this.m3CaUrl = m3CaUrl;
        this.myUsUrl = myUsUrl;
    }

    public Stream<URI> getCountryUrls() {
        return Stream.of(m3UsUrl, m3CaUrl, myUsUrl);
    }
}
