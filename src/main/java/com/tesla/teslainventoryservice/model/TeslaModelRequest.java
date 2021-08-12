package com.tesla.teslainventoryservice.model;

import java.net.URI;
import java.util.Objects;

public class TeslaModelRequest {
    private String sale = "All";

    private String model = "All";

    private String variant = "All";

    private String minYear = "2008";

    private String maxYear = "2021";

    private String country = "US";

    private String state;

    private String sort = "PA";

    private String min = "0";

    private String max = "99999999";

    private URI notificationUrl;

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getMinYear() {
        return minYear;
    }

    public void setMinYear(String minYear) {
        this.minYear = minYear;
    }

    public String getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(String maxYear) {
        this.maxYear = maxYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public URI getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(URI notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeslaModelRequest that = (TeslaModelRequest) o;
        return Objects.equals(sale, that.sale) && Objects.equals(model, that.model) && Objects.equals(variant, that.variant) && Objects.equals(minYear, that.minYear) && Objects.equals(maxYear, that.maxYear) && Objects.equals(country, that.country) && Objects.equals(state, that.state) && Objects.equals(sort, that.sort) && Objects.equals(min, that.min) && Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sale, model, variant, minYear, maxYear, country, state, sort, min, max);
    }

    @Override
    public String toString() {
        return "TeslaModelRequest{" +
                "sale='" + sale + '\'' +
                ", model='" + model + '\'' +
                ", variant='" + variant + '\'' +
                ", minYear='" + minYear + '\'' +
                ", maxYear='" + maxYear + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
