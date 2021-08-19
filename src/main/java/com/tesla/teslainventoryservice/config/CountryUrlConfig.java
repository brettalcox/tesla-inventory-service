package com.tesla.teslainventoryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.stream.Stream;

@Component
public class CountryUrlConfig {

    private final URI usUrl;

    private final URI caUrl;

    public CountryUrlConfig(final @Value("${tesla.us-url}") URI usURL,
                            final @Value("${tesla.ca-url}") URI caURL) {
        this.usUrl = usURL;
        this.caUrl = caURL;
    }

    public Stream<URI> getCountryUrls() {
        return Stream.of(usUrl, caUrl);
    }
}
