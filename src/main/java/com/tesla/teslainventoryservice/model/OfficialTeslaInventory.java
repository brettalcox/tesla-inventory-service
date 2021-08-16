package com.tesla.teslainventoryservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class OfficialTeslaInventory {

    @JsonProperty("VIN")
    private String vin;

    @JsonProperty("TrimName")
    private String trimName;

    @JsonProperty("TotalPrice")
    private long totalPrice;

    @JsonProperty("MonroneyPrice")
    private long outTheDoorPrice;

    @JsonProperty("PAINT")
    private List<String> paint;

    @JsonProperty("WHEELS")
    private List<String> wheels;

    @JsonProperty("INTERIOR")
    private List<String> interior;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("City")
    private String city;

    @JsonProperty("StateProvince")
    private String state;

    @JsonProperty("TitleStatus")
    private String titleStatus;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTrimName() {
        return trimName;
    }

    public void setTrimName(String trimName) {
        this.trimName = trimName;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getOutTheDoorPrice() {
        return outTheDoorPrice;
    }

    public void setOutTheDoorPrice(long outTheDoorPrice) {
        this.outTheDoorPrice = outTheDoorPrice;
    }

    public List<String> getPaint() {
        return paint;
    }

    public void setPaint(List<String> paint) {
        this.paint = paint;
    }

    public List<String> getWheels() {
        return wheels;
    }

    public void setWheels(List<String> wheels) {
        this.wheels = wheels;
    }

    public List<String> getInterior() {
        return interior;
    }

    public void setInterior(List<String> interior) {
        this.interior = interior;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitleStatus() {
        return titleStatus;
    }

    public void setTitleStatus(String titleStatus) {
        this.titleStatus = titleStatus;
    }

    public String getUrl() {
        return String.format("https://www.tesla.com/m3/order/%s", getVin());
    }

    public String getName() {
        return String.format("%s %s %s", getTitleStatus(), getYear(), getTrimName());
    }

    public String getLocation() {
        return String.format("%s, %s", getCity(), getState());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficialTeslaInventory that = (OfficialTeslaInventory) o;
        return Objects.equals(vin, that.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }

    @Override
    public String toString() {
        return "OfficialTeslaInventory{" +
                "vin='" + vin + '\'' +
                ", trimName='" + trimName + '\'' +
                ", totalPrice=" + totalPrice +
                ", outTheDoorPrice=" + outTheDoorPrice +
                ", paint=" + paint +
                ", wheels=" + wheels +
                ", interior=" + interior +
                ", year='" + year + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
