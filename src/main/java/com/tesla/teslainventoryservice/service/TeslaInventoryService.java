package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.client.TeslaInfoClient;
import com.tesla.teslainventoryservice.config.TeslaInventoryScheduleConfig;
import com.tesla.teslainventoryservice.model.InventoryNotification;
import com.tesla.teslainventoryservice.model.SlackPost;
import com.tesla.teslainventoryservice.model.TeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaModelRequest;
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
        return teslaInfoClient.getTeslaInventory(teslaModelRequest);
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void teslaInventoryJob() {
        LOGGER.info("Starting inventory check for {}", teslaInventoryScheduleConfig.getJobTemplates());
        try {
            teslaInventoryScheduleConfig.getJobTemplates()
                    .stream()
                    .map(tmr -> new InventoryNotification(teslaInfoClient.getTeslaInventory(tmr), tmr.getNotificationUrl()))
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
