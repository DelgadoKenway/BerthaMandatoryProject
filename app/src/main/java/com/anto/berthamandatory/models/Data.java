package com.anto.berthamandatory.models;

import java.io.Serializable;

public class Data  implements Serializable {
    private int deviceID;
    private double pm25;
    private double pm10;
    private int co2;
    private int o3;
    private double pressure;
    private double temperature;
    private double humidity;


    public Data(){

    }
    public Data(int id, double pm2, double pm1, int co, int oo3, double pres, double temp,
                         double hum){
        deviceID=id; pm25 = pm2; pm10 = pm1; co2 = co; o3 = oo3;
        pressure = pres; temperature = temp; humidity = hum;
    }


    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
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
}

