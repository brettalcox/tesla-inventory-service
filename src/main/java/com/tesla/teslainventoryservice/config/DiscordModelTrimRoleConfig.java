package com.tesla.teslainventoryservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "discord")
public class DiscordModelTrimRoleConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordModelTrimRoleConfig.class);
    private Map<String, Map<String, String>> countryRoles;
    private Map<String, String> roles;

    public String getRoleByCountryModelTrim(final String country, final String modelTrim) {
        return Optional.ofNullable(countryRoles)
                .map(r -> r.get(country))
                .map(r -> r.get(modelTrim))
                .orElse(null);
    }

    public String getRoleByTrimCombo(final String model, final String trim, final String cabinConfig, final String exteriorColor, final String interiorColor) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(model.toUpperCase());
        stringBuilder.append("_");
        stringBuilder.append(trim.toUpperCase());
        stringBuilder.append("_");
        stringBuilder.append(exteriorColor);
        stringBuilder.append("_");
        stringBuilder.append(interiorColor);
        if (cabinConfig != null) {
            stringBuilder.append("_");
            stringBuilder.append(cabinConfig.toUpperCase());
        }
        LOGGER.info("Generated trim combo role lookup of {}", stringBuilder);
        final String role = roles.get(stringBuilder.toString());
        if (role == null) {
            LOGGER.warn("Unable to find trim combo role with lookup {}", stringBuilder);
        }
        return role;
    }

    public Map<String, Map<String, String>> getCountryRoles() {
        return countryRoles;
    }

    public void setCountryRoles(Map<String, Map<String, String>> roles) {
        this.countryRoles = roles;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }
}
