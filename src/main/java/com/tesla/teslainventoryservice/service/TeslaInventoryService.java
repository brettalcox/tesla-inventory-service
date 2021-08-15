package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.client.TeslaInfoClient;
import com.tesla.teslainventoryservice.config.TeslaInventoryScheduleConfig;
import com.tesla.teslainventoryservice.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class TeslaInventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeslaInventoryService.class);

    private final TeslaInfoClient teslaInfoClient;

    private final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig;

    private final SlackClient slackClient;

    private final CacheManager cacheManager;

    private final URI errorNotificationUrl;

    public TeslaInventoryService(final TeslaInfoClient teslaInfoClient,
                                 final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig,
                                 final SlackClient slackClient,
                                 final CacheManager cacheManager,
                                 @Value("${tesla.error-notification-url}") URI errorNotificationUrl) {
        this.teslaInfoClient = teslaInfoClient;
        this.teslaInventoryScheduleConfig = teslaInventoryScheduleConfig;
        this.slackClient = slackClient;
        this.cacheManager = cacheManager;
        this.errorNotificationUrl = errorNotificationUrl;
    }

    public List<TeslaInventory> getTeslaInventory(final TeslaModelRequest teslaModelRequest) {
        return teslaInfoClient.getTeslaInventoryWithPictures(teslaModelRequest);
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void detailedTeslaInventoryJob() {
        LOGGER.info("Starting inventory check for {}", teslaInventoryScheduleConfig.getJobTemplates());
        try {
            teslaInventoryScheduleConfig.getJobTemplates()
                    .stream()
                    .map(tmr -> new DetailedInventoryNotification(teslaInfoClient.getDetailedTeslaInventory(tmr), tmr.getNotificationUrl()))
                    .forEach(in -> {
                        in.getTeslaInventories()
                                .stream()
                                .filter(ti -> cacheManager.getCache("inventory").get(ti.getUrl()) == null)
                                .forEach(ti -> {
                                    final SlackPost slackPost = new SlackPost.Builder()
                                            .addLine(ti.getUrl())
                                            .addLine("*Model:* " + ti.getName())
                                            .addLine("*Year:* " + ti.getAttributes().get("Year"))
                                            .addLine("*Price:* " + ti.getAttributes().get("Price"))
                                            .addLine("*Wheels:* " + ti.getAttributes().get("Wheels"))
                                            .addLine("*Interior:* " + ti.getAttributes().get("Interior"))
                                            .addLine("*Colour:* " + ti.getAttributes().get("Colour"))
                                            .addLine("*State:* " + ti.getAttributes().get("State"))
                                            .addLine("*Miles:* " + ti.getAttributes().get("Miles"))
                                            .build();
                                    slackClient.sendSlackNotification(
                                            slackPost,
                                            in.getNotificationUrl()
                                    );
                                    cacheManager.getCache("inventory").put(ti.getUrl(), ti);
                                });
                    });
            cacheManager.getCache("error").put("isError", false);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
            cacheManager.getCache("error").put("isError", true);
        }
    }

    public void teslaInventoryWithPicturesJob() {
        LOGGER.info("Starting inventory check for {}", teslaInventoryScheduleConfig.getJobTemplates());
        try {
            teslaInventoryScheduleConfig.getJobTemplates()
                    .stream()
                    .map(tmr -> new InventoryNotification(teslaInfoClient.getTeslaInventoryWithPictures(tmr), tmr.getNotificationUrl()))
                    .forEach(in -> {
                        in.getTeslaInventories()
                            .stream()
                            .filter(ti -> cacheManager.getCache("inventory").get(ti.getUrl()) == null)
                            .forEach(ti -> {
                                slackClient.sendSlackNotification(
                                        new SlackPost.Builder()
                                                .addLine(ti.getName())
                                                .addLine(ti.getUrl())
                                                .addLine(ti.getImageUrl())
                                                .build(),
                                        in.getNotificationUrl()
                                );
                                cacheManager.getCache("inventory").put(ti.getUrl(), ti);
                            });
                    });
            cacheManager.getCache("error").put("isError", false);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
            cacheManager.getCache("error").put("isError", true);
        }
    }
}
