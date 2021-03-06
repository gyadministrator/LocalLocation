package com.android.custom.locationlib.bean;

import java.util.ArrayList;
import java.util.List;

public class Latlng {

    double latitude;
    double longitude;
    String provider;

    float bearing;
    float speed;

    String country;
    String countryCode;
    // 市
    String city;
    // 区
    String sublocality;
    String cityCode;
    String address;
    String name;

    List<String> addressLines = new ArrayList<>();

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    @Override
    public String toString() {
        return "Latlng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", provider='" + provider + '\'' +
                ", bearing=" + bearing +
                ", speed=" + speed +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", sublocality='" + sublocality + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", addressLines=" + addressLines +
                '}';
    }
}
