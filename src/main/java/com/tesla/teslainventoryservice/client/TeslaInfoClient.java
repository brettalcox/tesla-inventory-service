package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.TeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaModelRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeslaInfoClient {

    private final RestTemplate restTemplate;

    private final URI teslaUrl;

    public TeslaInfoClient(final RestTemplate restTemplate, @Value("${tesla.url}") URI teslaUrl) {
        this.restTemplate = restTemplate;
        this.teslaUrl = teslaUrl;
    }

    public List<TeslaInventory> getTeslaInventory(final TeslaModelRequest teslaModelRequest) {
        final URI uri = UriComponentsBuilder.fromUri(teslaUrl)
                .queryParam("country", teslaModelRequest.getCountry())
                .queryParam("state", teslaModelRequest.getState())
                .queryParam("sale", teslaModelRequest.getSale())
                .queryParam("sort", teslaModelRequest.getSort())
                .queryParam("min", teslaModelRequest.getMin())
                .queryParam("max", teslaModelRequest.getMax())
                .queryParam("model", teslaModelRequest.getModel())
                .queryParam("variant", teslaModelRequest.getVariant())
                .queryParam("minYear", teslaModelRequest.getMinYear())
                .queryParam("maxYear", teslaModelRequest.getMaxYear())
                .build()
                .toUri();

        try {
            final String response = restTemplate.getForObject(uri, String.class);
            final Document document = Jsoup.parse(response);
            final Elements elements = document.getElementsByClass("list4");
            return elements
                    .stream()
                    .map(e -> new TeslaInventory(e.selectFirst("h2").text(), e.selectFirst("a").attr("href"), e.selectFirst("img").attr("src")))
                    .collect(Collectors.toList());
        } catch (final Exception e) {
            throw new RuntimeException("FUCK!", e);
        }
    }
}
