package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.client.TeslaInfoClient;
import com.tesla.teslainventoryservice.config.TeslaInventoryScheduleConfig;
import com.tesla.teslainventoryservice.model.TeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaModelRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TeslaInventoryService {

    private final TeslaInfoClient teslaInfoClient;

    private final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig;

    private final SlackClient slackClient;

    public TeslaInventoryService(final TeslaInfoClient teslaInfoClient, TeslaInventoryScheduleConfig teslaInventoryScheduleConfig, SlackClient slackClient) {
        this.teslaInfoClient = teslaInfoClient;
        this.teslaInventoryScheduleConfig = teslaInventoryScheduleConfig;
        this.slackClient = slackClient;
    }

    public List<TeslaInventory> getTeslaInventory(final TeslaModelRequest teslaModelRequest) {
        return teslaInfoClient.getTeslaInventory(teslaModelRequest);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void teslaInventoryJob() {
        teslaInventoryScheduleConfig.getJobTemplates()
                .stream()
                .map(teslaInfoClient::getTeslaInventory)
                .flatMap(Collection::stream)
                .forEach(ti -> slackClient.sendSlackNotification(ti.toString()));
    }
}
