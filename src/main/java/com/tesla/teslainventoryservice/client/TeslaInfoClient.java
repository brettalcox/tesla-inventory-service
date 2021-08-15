package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.DetailedTeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaModelRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TeslaInfoClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeslaInfoClient.class);

    private final RestTemplate restTemplate;

    private final URI teslaInventoryWithPicturesUrl;

    private final URI detailedInventoryUrl;

    public TeslaInfoClient(final RestTemplate restTemplate,
                           @Value("${tesla.inventory-with-pictures-url}") final URI teslaInventoryWithPicturesUrl,
                           @Value("${tesla.detailed-inventory-url}") final URI detailedInventoryUrl) {
        this.restTemplate = restTemplate;
        this.teslaInventoryWithPicturesUrl = teslaInventoryWithPicturesUrl;
        this.detailedInventoryUrl = detailedInventoryUrl;
    }

    public List<TeslaInventory> getTeslaInventoryWithPictures(final TeslaModelRequest teslaModelRequest) {
        final Document document = Jsoup.parse(getTeslaInventory(teslaInventoryWithPicturesUrl, teslaModelRequest));
        final Elements elements = document.getElementsByClass("list4");
        return elements
                .stream()
                .map(e -> new TeslaInventory(e.selectFirst("h2").text(), e.selectFirst("a").attr("href"), e.selectFirst("img").attr("src")))
                .collect(Collectors.toList());
    }

    public List<DetailedTeslaInventory> getDetailedTeslaInventory(final TeslaModelRequest teslaModelRequest) {
        final Document document = Jsoup.parse(getTeslaInventory(detailedInventoryUrl, teslaModelRequest));
        final Elements elements = Optional.of(document.getElementsByClass("tablelist"))
                .filter(e -> e.size() == 1)
                .map(e -> e.get(0))
                .map(Element::children)
                .orElse(new Elements());

        final String[] headers = elements
                .stream()
                .flatMap(e -> e.getAllElements().stream())
                .filter(e -> e.is("b"))
                .map(Element::text)
                .toArray(String[]::new);

        final List<DetailedTeslaInventory> detailedTeslaInventories = new ArrayList<>();
        if (elements.size() > headers.length) {
            for (int i = 12; i < elements.size(); i+=12) {
                final Map<String, String> attributes = new HashMap<>();
                for (int j = i; j < i + 12; j++) {
                    attributes.put(headers[j - i], elements.get(j).text());
                }
                final Element inventoryAnchorElement = elements.get(i).selectFirst("a");
                detailedTeslaInventories.add(new DetailedTeslaInventory(inventoryAnchorElement.attr("href"), inventoryAnchorElement.text(), attributes));
            }
        }

        return detailedTeslaInventories;
    }

    private String getTeslaInventory(final URI inventoryUri, final TeslaModelRequest teslaModelRequest) {
        final URI uri = UriComponentsBuilder.fromUri(inventoryUri)
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
        return restTemplate.getForObject(uri, String.class);
    }
}
