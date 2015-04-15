package no.ntnu.pawanchamling.vrldatacollection.main;

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
    private boolean isOrdinalDataOn;
    private int noOfOridnalValues;
    private ArrayList<String> ordinalValues;

    public Settings(){
        this.fileTimeStamp = "";

        this.isGPSsensorOn = true;
        this.GPSdataScheduleTime = 5;
        this.isNoiseSensorOn = true;
        this.noiseDataScheduleTime = 3;
        this.isOrdinalDataOn = true;
        this.noOfOridnalValues = 3;
        ordinalValues = new ArrayList<String>();
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
