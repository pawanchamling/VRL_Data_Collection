package no.ntnu.pawanchamling.vrldatacollection.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pawan Chamling on 15/04/15.
 */
public class Settings implements Serializable{

    private String fileTimeStamp;

    private boolean isGPSsensorOn;
    private int GPSdataScheduleTime;
    private boolean isNoiseSensorOn;
    private int noiseDataScheduleTime;
    private boolean isTemperatureSensorOn;

    private int temperatureDataScheduleTime;
    private boolean isOrdinalDataOn;
    private int noOfOridnalValues;
    private ArrayList<String> ordinalValues;

    public Settings() {
        this.fileTimeStamp = "";

        this.isGPSsensorOn = true;
        this.GPSdataScheduleTime = 300;
        this.isNoiseSensorOn = true;
        this.noiseDataScheduleTime = 120;
        this.isTemperatureSensorOn = true;
        this.temperatureDataScheduleTime = 120;
        this.isOrdinalDataOn = true;
        ordinalValues = new ArrayList<String>();


        ordinalValues.add("High");
        ordinalValues.add("Medium");
        ordinalValues.add("Low");



//        ordinalValues.add("High");
//        ordinalValues.add("Medium");
//        ordinalValues.add("Low");
//        ordinalValues.add("lower");
//        ordinalValues.add("Lowest");
//        ordinalValues.add("much lower");
//        ordinalValues.add("such lowest wow incredible");


        this.noOfOridnalValues = ordinalValues.size();


    }

    public String getFileTimeStamp() {
        return fileTimeStamp;
    }

    public void setFileTimeStamp(String fileTimeStamp) {
        this.fileTimeStamp = fileTimeStamp;
    }
    public boolean isGPSsensorOn() {
        return isGPSsensorOn;
    }

    public void setGPSsensorOn(boolean isGPSsensorOn) {
        this.isGPSsensorOn = isGPSsensorOn;
    }

    public int getGPSdataScheduleTime() {
        return GPSdataScheduleTime;
    }

    public void setGPSdataScheduleTime(int GPSdataScheduleTime) {
        this.GPSdataScheduleTime = GPSdataScheduleTime;
    }

    public boolean isNoiseSensorOn() {
        return isNoiseSensorOn;
    }

    public void setNoiseSensorOn(boolean isNoiseSensorOn) {
        this.isNoiseSensorOn = isNoiseSensorOn;
    }

    public int getNoiseDataScheduleTime() {
        return noiseDataScheduleTime;
    }

    public void setNoiseDataScheduleTime(int noiseDataScheduleTime) {
        this.noiseDataScheduleTime = noiseDataScheduleTime;
    }


    public boolean isTemperatureSensorOn() {
        return isTemperatureSensorOn;
    }

    public void setTemperatureSensorOn(boolean isTemperatureSensorOn) {
        this.isTemperatureSensorOn = isTemperatureSensorOn;
    }

    public int getTemperatureDataScheduleTime() {
        return temperatureDataScheduleTime;
    }

    public void setTemperatureDataScheduleTime(int temperatureDataScheduleTime) {
        this.temperatureDataScheduleTime = temperatureDataScheduleTime;
    }

    public boolean isOrdinalDataOn() {
        return isOrdinalDataOn;
    }

    public void setOrdinalDataOn(boolean isOrdinalDataOn) {
        this.isOrdinalDataOn = isOrdinalDataOn;
    }

    public int getNoOfOridnalValues() {
        return noOfOridnalValues;
    }

    public void setNoOfOridnalValues(int noOfOridnalValues) {
        this.noOfOridnalValues = noOfOridnalValues;
    }

    public ArrayList<String> getOrdinalValues() {
        return ordinalValues;
    }

    public void setOrdinalValues(ArrayList<String> ordinalValues) {
        this.ordinalValues = ordinalValues;
    }

}
