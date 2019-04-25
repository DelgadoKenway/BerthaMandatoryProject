package com.anto.berthamandatory.models;

import java.io.Serializable;

public class PostData  implements Serializable {
    private int deviceId;
    private double pm25;
    private double pm10;
    private int co2;
    private int o3;
    private double pressure;
    private double temperature;


    @Override
    public String toString() {
        return "Device: "+ deviceId+" pm25: "+pm25+" pm10: "+pm10+" co2: "+co2
                +" o3: " + o3 + " pressure: " + pressure + " temp: " + temperature
                +" humidity: " + humidity + " userID: "+ userID;
    }

    private double humidity;
    private String utc;
    private double latitude;
    private double longtitude;
    private double noise;
    private String userID;


    public PostData(){
        noise = 0;
    }
    public PostData(int id, double pm2, double pm1, int co, int oo3, double pres, double temp,
                double hum){
        deviceId=id; pm25 = pm2; pm10 = pm1; co2 = co; o3 = oo3;
        pressure = pres; temperature = temp; humidity = hum; noise = 0;
    }


    public int getDeviceID() {
        return deviceId;
    }

    public void setDeviceID(int deviceID) {
        this.deviceId = deviceID;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }
}

