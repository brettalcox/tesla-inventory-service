package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.DiscordClient;
import com.tesla.teslainventoryservice.client.OfficialTeslaApiClient;
import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.config.TeslaInventoryScheduleConfig;
import com.tesla.teslainventoryservice.model.DiscordPost;
import com.tesla.teslainventoryservice.model.SlackPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
public class TeslaInventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeslaInventoryService.class);

    private final OfficialTeslaApiClient officialTeslaApiClient;

    private final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig;

    private final SlackClient slackClient;

    private final DiscordClient discordClient;

    private final CacheManager cacheManager;

    private final URI errorNotificationUrl;


    public TeslaInventoryService(final OfficialTeslaApiClient officialTeslaApiClient,
                                 final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig,
                                 final SlackClient slackClient,
                                 final DiscordClient discordClient,
                                 final CacheManager cacheManager,
                                 final @Value("${tesla.error-notification-url}") URI errorNotificationUrl) {
        this.officialTeslaApiClient = officialTeslaApiClient;
        this.teslaInventoryScheduleConfig = teslaInventoryScheduleConfig;
        this.slackClient = slackClient;
        this.discordClient = discordClient;
        this.cacheManager = cacheManager;
        this.errorNotificationUrl = errorNotificationUrl;
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void officialTeslaInventoryJob() {
        LOGGER.info("Starting inventory check for 2021 Model 3");
        try {
            officialTeslaApiClient.getOfficialTeslaInventory()
                    .getResults()
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getVin()) == null)
                    .forEach(ti -> {
                        LOGGER.info("{}", ti);
                        final DiscordPost discordPost = new DiscordPost.Builder()
                                .addLine(ti.getUrl())
                                .addLine("*Model:* " + ti.getName())
                                .addLine("*Price:* " + ti.getTotalPrice())
                                .addLine("*OTD Price:* " + ti.getOutTheDoorPrice())
                                .addLine("*Wheels:* " + ti.getWheels())
                                .addLine("*Interior:* " + ti.getInterior())
                                .addLine("*Paint:* " + ti.getPaint())
                                .addLine("*Location:* " + ti.getLocation())
                                .build();
                        discordClient.sendNotification(discordPost, Optional.ofNullable(teslaInventoryScheduleConfig.getNotificationEndpoints().get(ti.getTrim()))
                                .orElseGet(() -> teslaInventoryScheduleConfig.getNotificationEndpoints().get("UNKNOWN")));
                        cacheManager.getCache("inventory").put(ti.getVin(), ti);
                    });
            cacheManager.getCache("error").put("isError", false);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
            cacheManager.getCache("error").put("isError", true);
        }
    }
}
