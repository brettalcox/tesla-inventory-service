package com.tesla.teslainventoryservice.client;

import com.tesla.teslainventoryservice.model.SlackPost;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class SlackClient {

    private final RestTemplate restTemplate;

    public SlackClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSlackNotification(final SlackPost slackPost, final URI uri) {
        restTemplate.postForEntity(uri, slackPost, Void.class);
    }
}
