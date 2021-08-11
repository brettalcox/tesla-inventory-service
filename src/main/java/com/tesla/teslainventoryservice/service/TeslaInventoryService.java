package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.client.TeslaInfoClient;
import com.tesla.teslainventoryservice.config.TeslaInventoryScheduleConfig;
import com.tesla.teslainventoryservice.model.SlackPost;
import com.tesla.teslainventoryservice.model.TeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaModelRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeslaInventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeslaInventoryService.class);

    private final TeslaInfoClient teslaInfoClient;

    private final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig;

    private final SlackClient inventoryNotificationSlackClient;

    private final SlackClient errorSlackClient;

    private final CacheManager cacheManager;

    public TeslaInventoryService(final TeslaInfoClient teslaInfoClient,
                                 final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig,
                                 final SlackClient inventoryNotificationSlackClient,
                                 final SlackClient errorSlackClient,
                                 final CacheManager cacheManager) {
        this.teslaInfoClient = teslaInfoClient;
        this.teslaInventoryScheduleConfig = teslaInventoryScheduleConfig;
        this.inventoryNotificationSlackClient = inventoryNotificationSlackClient;
        this.errorSlackClient = errorSlackClient;
        this.cacheManager = cacheManager;
    }

    public List<TeslaInventory> getTeslaInventory(final TeslaModelRequest teslaModelRequest) {
        return teslaInfoClient.getTeslaInventory(teslaModelRequest);
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void teslaInventoryJob() {
        LOGGER.info("Starting inventory check for {}", teslaInventoryScheduleConfig.getJobTemplates());
        try {
            final List<TeslaInventory> teslaInventories = teslaInventoryScheduleConfig.getJobTemplates()
                    .stream()
                    .map(teslaInfoClient::getTeslaInventory)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            teslaInventories
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getUrl()) == null)
                    .forEach(ti -> inventoryNotificationSlackClient.sendSlackNotification(
                            new SlackPost.Builder()
                                    .addLine(ti.getName())
                                    .addLine(ti.getUrl())
                                    .addLine(ti.getImageUrl())
                                    .build()
                    ));

            teslaInventories
                    .forEach(ti -> cacheManager.getCache("inventory").putIfAbsent(ti.getUrl(), ti));
            cacheManager.getCache("error").put("isError", false);
        } catch (final Exception e) {
            errorSlackClient.sendSlackNotification(new SlackPost(e.toString()));
            cacheManager.getCache("error").put("isError", true);
        }
    }
}
