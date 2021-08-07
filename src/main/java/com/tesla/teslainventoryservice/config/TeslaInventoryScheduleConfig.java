package com.tesla.teslainventoryservice.config;

import com.tesla.teslainventoryservice.model.TeslaModelRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "tesla")
@EnableScheduling
public class TeslaInventoryScheduleConfig {

    private List<TeslaModelRequest> jobTemplates;

    public List<TeslaModelRequest> getJobTemplates() {
        return jobTemplates;
    }

    public void setJobTemplates(List<TeslaModelRequest> jobTemplates) {
        this.jobTemplates = jobTemplates;
    }
}
