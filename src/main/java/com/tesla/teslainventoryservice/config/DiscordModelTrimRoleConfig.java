package com.tesla.teslainventoryservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "discord")
public class DiscordModelTrimRoleConfig {
    private Map<String, Map<String, String>> roles;

    public String getRoleByCountryModelTrim(final String country, final String modelTrim) {
        return Optional.ofNullable(roles)
                .map(r -> r.get(country))
                .map(r -> r.get(modelTrim))
                .orElse(null);
    }

    public Map<String, Map<String, String>> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Map<String, String>> roles) {
        this.roles = roles;
    }
}
