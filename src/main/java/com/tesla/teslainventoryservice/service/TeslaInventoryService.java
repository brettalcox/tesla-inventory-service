package com.tesla.teslainventoryservice.service;

import com.tesla.teslainventoryservice.client.DiscordClient;
import com.tesla.teslainventoryservice.client.OfficialTeslaApiClient;
import com.tesla.teslainventoryservice.client.SlackClient;
import com.tesla.teslainventoryservice.config.CountryUrlConfig;
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

    private final CountryUrlConfig countryUrlConfig;

    private final SlackClient slackClient;

    private final DiscordClient discordClient;

    private final CacheManager cacheManager;

    private final URI errorNotificationUrl;

    public TeslaInventoryService(final OfficialTeslaApiClient officialTeslaApiClient,
                                 final ReferralService referralService,
                                 final TeslaInventoryScheduleConfig teslaInventoryScheduleConfig,
                                 final CountryUrlConfig countryUrlConfig,
                                 final SlackClient slackClient,
                                 final DiscordClient discordClient,
                                 final CacheManager cacheManager,
                                 final @Value("${tesla.error-notification-url}") URI errorNotificationUrl) {
        this.officialTeslaApiClient = officialTeslaApiClient;
        this.referralService = referralService;
        this.teslaInventoryScheduleConfig = teslaInventoryScheduleConfig;
        this.countryUrlConfig = countryUrlConfig;
        this.slackClient = slackClient;
        this.discordClient = discordClient;
        this.cacheManager = cacheManager;
        this.errorNotificationUrl = errorNotificationUrl;
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void USModel3() {
        LOGGER.info("Starting inventory check for US 2021 Model 3");
        try {
            officialTeslaApiClient.getOfficialTeslaInventory(countryUrlConfig.getCountryUrl(CountryModel.US_MODEL3))
                    .getResults()
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getVin()) == null)
                    .forEach(this::handleInventory);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void CAModel3() {
        LOGGER.info("Starting inventory check for CA 2021 Model 3");
        try {
            officialTeslaApiClient.getOfficialTeslaInventory(countryUrlConfig.getCountryUrl(CountryModel.CA_MODEL3))
                    .getResults()
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getVin()) == null)
                    .forEach(this::handleInventory);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void USModelY() {
        LOGGER.info("Starting inventory check for US 2021 Model Y");
        try {
            officialTeslaApiClient.getOfficialTeslaInventory(countryUrlConfig.getCountryUrl(CountryModel.US_MODELY))
                    .getResults()
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getVin()) == null)
                    .forEach(this::handleInventory);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void CAModelY() {
        LOGGER.info("Starting inventory check for CA 2021 Model Y");
        try {
            officialTeslaApiClient.getOfficialTeslaInventory(countryUrlConfig.getCountryUrl(CountryModel.CA_MODELY))
                    .getResults()
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getVin()) == null)
                    .forEach(this::handleInventory);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void USModelS() {
        LOGGER.info("Starting inventory check for US 2021 Model S");
        try {
            officialTeslaApiClient.getOfficialTeslaInventory(countryUrlConfig.getCountryUrl(CountryModel.US_MODELS))
                    .getResults()
                    .stream()
                    .filter(ti -> cacheManager.getCache("inventory").get(ti.getVin()) == null)
                    .forEach(this::handleInventory);
        } catch (final Exception e) {
            slackClient.sendSlackNotification(new SlackPost(e.toString()), errorNotificationUrl);
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void CAModelS() {
        LOGGER.info("Starting inventory check for CA 2021 Model S");
        try {
            officialTeslaApiClient.getOfficialTeslaInventory(countryUrlConfig.getCountryUrl(CountryModel.CA_MODELS))
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
                .addLine(":warning: Tesla has halted all transports for remainder of the quarter. Please ensure this is a location you intend to pickup from or drive/fly to. :warning:")
                .addLine(" ________________________________")
                .quote(true)
                .build();
        discordClient.sendNotification(discordPost, Optional.ofNullable(teslaInventoryScheduleConfig
                .getNotificationEndpoints()
                .get(officialTeslaInventory.getCountryCode())
                .get(officialTeslaInventory.getModel().toUpperCase().concat(officialTeslaInventory.getTrim())))
                .orElseGet(() -> teslaInventoryScheduleConfig.getNotificationEndpoints().get(officialTeslaInventory.getCountryCode()).get("UNKNOWN")));
        LOGGER.info("{}", discordPost);
        cacheManager.getCache("inventory").put(officialTeslaInventory.getVin(), officialTeslaInventory);
    }
}
