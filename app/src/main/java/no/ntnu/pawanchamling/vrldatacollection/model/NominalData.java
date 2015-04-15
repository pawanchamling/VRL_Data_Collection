package no.ntnu.pawanchamling.vrldatacollection.model;

import java.util.ArrayList;

/**
 * Created by Pawan Chamling on 04/04/15.
 */
public class NominalData {
    private ArrayList<String> nominalData;
    private ArrayList<String> timeStampData;



    public NominalData() {
        nominalData = new ArrayList<String>();
        timeStampData = new ArrayList<String>();
    }

    public ArrayList<String> getNominalData() {
        return nominalData;
    }

    public void setNominalData(ArrayList<String> nominalData) {
        this.nominalData = nominalData;
    }

    public ArrayList<String> getTimeStampData() {
        return timeStampData;
    }

    public void setTimeStampData(ArrayList<String> timeStampData) {
        this.timeStampData = timeStampData;
    }

    public void addValue(String timeStamp, String value){
        this.timeStampData.add(timeStamp);
        this.nominalData.add(value);
    }


    public int getSize(){
       return nominalData.size();
    }

    public boolean isEmpty(){
        return nominalData.isEmpty();
    }

}
