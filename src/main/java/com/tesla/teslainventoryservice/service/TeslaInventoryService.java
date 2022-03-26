package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.DiscordClient;
import com.tesla.teslainventoryservice.client.OfficialTeslaApiClient;
import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.config.CountryUrlConfig;
import com.tesla.teslainventoryservice.config.DiscordModelTrimRoleConfig;
import com.tesla.teslainventoryservice.config.TeslaInventoryScheduleConfig;
import com.tesla.teslainventoryservice.model.CountryModel;
import com.tesla.teslainventoryservice.model.DiscordPost;
import com.tesla.teslainventoryservice.model.OfficialTeslaInventory;
import com.tesla.teslainventoryservice.model.SlackPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

/**
 * The design in this class is kind of gross, but I'm just taking the easy route here. I want the notifications to be
 * as quick as possible, while also leveraging the ease of Spring @Scheduled. Have a separately managed thread pool
 * in the config class. Broke each model out into their own checks so the Discord notifications don't have to wait for
 * the rest of the inventory checks
 */
@Service
public class TeslaInventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeslaInventoryService.class);

    private final OfficialTeslaApiClient officialTeslaApiClient;

    private final ReferralService referralService;

    private final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig;

    private final DiscordModelTrimRoleConfig discordModelTrimRoleConfig;

    private final CountryUrlConfig countryUrlConfig;

    private final SlackClient slackClient;

    private final DiscordClient discordClient;

    private final CacheManager cacheManager;

    private final URI errorNotificationUrl;

    public TeslaInventoryService(final OfficialTeslaApiClient officialTeslaApiClient,
                                 final ReferralService referralService,
                                 final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig,
                                 final DiscordModelTrimRoleConfig discordModelTrimRoleConfig,
                                 final CountryUrlConfig countryUrlConfig,
                                 final SlackClient slackClient,
                                 final DiscordClient discordClient,
                                 final CacheManager cacheManager,
                                 final @Value("${tesla.error-notification-url}") URI errorNotificationUrl) {
        this.officialTeslaApiClient = officialTeslaApiClient;
        this.referralService = referralService;
        this.teslaInventoryScheduleConfig = teslaInventoryScheduleConfig;
        this.discordModelTrimRoleConfig = discordModelTrimRoleConfig;
        this.countryUrlConfig = countryUrlConfig;
        this.slackClient = slackClient;
        this.discordClient = discordClient;
        this.cacheManager = cacheManager;
        this.errorNotificationUrl = errorNotificationUrl;
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void USModel3() {
        checkInventory(CountryModel.US_MODEL3);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void CAModel3() {
        checkInventory(CountryModel.CA_MODEL3);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void USModelY() {
        checkInventory(CountryModel.US_MODELY);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void CAModelY() {
        checkInventory(CountryModel.CA_MODELY);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void USModelS() {
        checkInventory(CountryModel.US_MODELS);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void CAModelS() {
        checkInventory(CountryModel.CA_MODELS);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void USModelX() {
        checkInventory(CountryModel.US_MODELX);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void CAModelX() {
        checkInventory(CountryModel.CA_MODELX);
    }

    private void checkInventory(final CountryModel countryModel) {
        LOGGER.info("Starting inventory check for {}", countryModel);
        try {
            officialTeslaApiClient.getOfficialTeslaInventory(countryUrlConfig.getCountryUrl(countryModel))
                    .getResults()
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getVin()) == null)
                    .forEach(this::handleInventory);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
        }
    }

    private void handleInventory(final OfficialTeslaInventory officialTeslaInventory) {
        LOGGER.info("{}", officialTeslaInventory);
        final DiscordPost discordPost = new DiscordPost.Builder()
                .addLine("**" + officialTeslaInventory.getName() + "**")
                .addLine(officialTeslaInventory.getUrl())
                .addLine("Price", officialTeslaInventory.getTotalPrice())
                .addLine("OTD Price", officialTeslaInventory.getOutTheDoorPrice())
                .addLine("Wheels", officialTeslaInventory.getWheels())
                .addLine("Interior", officialTeslaInventory.getInterior())
                .addLineIfNotNull("Decor", officialTeslaInventory.getDecor())
                .addLine("Paint", officialTeslaInventory.getPaint())
                .addLine("Additional Options", officialTeslaInventory.getAdditionalOptions())
                .addLine("Autopilot/FSD", officialTeslaInventory.getAutopilot())
                .addLineIfNotNull("Cabin Config", officialTeslaInventory.getCabinConfig())
                .addLine("Odometer", officialTeslaInventory.getOdometer())
                .addLine("Range", officialTeslaInventory.getRange())
                .addLine("Demo/Test Drive Vehicle", officialTeslaInventory.getIsDemo())
                .addLine("Location", officialTeslaInventory.getLocation())
                .addLine(discordModelTrimRoleConfig.getRoleByCountryModelTrim(officialTeslaInventory.getCountryCode(), officialTeslaInventory.getModelTrim()))
                .addLine(" ________________________________")
                .quote(true)
                .build();
        discordClient.sendNotification(discordPost, Optional.ofNullable(teslaInventoryScheduleConfig
                .getNotificationEndpoints()
                .get(officialTeslaInventory.getCountryCode())
                .get(officialTeslaInventory.getModelTrim()))
                .orElseGet(() -> teslaInventoryScheduleConfig.getNotificationEndpoints().get(officialTeslaInventory.getCountryCode()).get("UNKNOWN")));
        LOGGER.info("{}", discordPost);
        cacheManager.getCache("inventory").put(officialTeslaInventory.getVin(), officialTeslaInventory);
    }
}
