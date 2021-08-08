package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.client.TeslaInfoClient;
import com.tesla.teslainventoryservice.config.TeslaInventoryScheduleConfig;
import com.tesla.teslainventoryservice.model.TeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaModelRequest;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeslaInventoryService {

    private final TeslaInfoClient teslaInfoClient;

    private final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig;

    private final SlackClient slackClient;

    private final CacheManager cacheManager;

    public TeslaInventoryService(final TeslaInfoClient teslaInfoClient,
                                 final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig,
                                 final SlackClient slackClient,
                                 final CacheManager cacheManager) {
        this.teslaInfoClient = teslaInfoClient;
        this.teslaInventoryScheduleConfig = teslaInventoryScheduleConfig;
        this.slackClient = slackClient;
        this.cacheManager = cacheManager;
    }

    public List<TeslaInventory> getTeslaInventory(final TeslaModelRequest teslaModelRequest) {
        return teslaInfoClient.getTeslaInventory(teslaModelRequest);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void teslaInventoryJob() {
        final List<TeslaInventory> teslaInventories = teslaInventoryScheduleConfig.getJobTemplates()
                .stream()
                .map(teslaInfoClient::getTeslaInventory)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        teslaInventories
                .stream()
                .filter(ti -> cacheManager.getCache("inventory").get(ti.getUrl()) == null)
                .forEach(ti -> slackClient.sendSlackNotification(ti.toString()));

        teslaInventories
                .stream()
                .forEach(ti -> cacheManager.getCache("inventory").putIfAbsent(ti.getUrl(), ti));
    }
}
