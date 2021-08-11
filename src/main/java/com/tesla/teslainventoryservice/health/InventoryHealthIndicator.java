package com.tesla.teslainventoryservice.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InventoryHealthIndicator extends AbstractHealthIndicator {

    private final CacheManager cacheManager;

    public InventoryHealthIndicator(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Optional.ofNullable(cacheManager.getCache("error"))
                .map(c -> c.get("isError", Boolean.class))
                .ifPresentOrElse(b -> {
                    if (b) {
                        builder.down();
                    } else {
                        builder.up();
                    }
                }, builder::unknown);
    }
}
