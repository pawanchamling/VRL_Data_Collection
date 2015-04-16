package no.ntnu.pawanchamling.vrldatacollection.session;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import no.ntnu.pawanchamling.vrldatacollection.model.NominalData;

/**
 * Created by Pawan Chamling on 04/04/15.
 */
public class SessionPresenter {

    private SessionView sessionView;

    private String theMainTimeStamp;

    private final NominalData nominalData;

    public SessionPresenter(SessionView sessionView, String theMainTimeStamp){
        this.sessionView = sessionView;
        this.theMainTimeStamp = theMainTimeStamp;

        this.nominalData = new NominalData();

    }

    public void startANoteDialog(){
        sessionView.startEnterANoteDialog();
    }

    public void addNominalNote(String timeStamp, String note) {
        this.nominalData.addValue(timeStamp, note);
    }

    public void stopSession(){

        ArrayList<String> data = this.nominalData.getNominalData();
        ArrayList<String> timeStampData = this.nominalData.getTimeStampData();

        Log.i("###SessionPresenter", "No. of items in the list: " + data.size());
        Log.i("###SessionPresenter", "=" + data.toString() + "=");
        for(String value: data){
            System.out.println(value);
        }
        Log.i("###SessionPresenter", "--------------------");

        Log.i("###SessionPresenter", "Saving data to the file");

        String jsonData = "";

        for(int i = 0; i < timeStampData.size(); i++){
            jsonData += "{'timestamp':'" + timeStampData.get(i) + "',";
            jsonData += "'value':'" + data.get(i) + "'}";

            //if not the last value
            if(i != timeStampData.size() - 1){
                jsonData += ",";
            }
        }

        jsonData += "]}";

        String jsonHeaderString = "{'name':'User Notes', 'Source':'Android Mobile','type':'0',";
        jsonHeaderString += "'valueInfo':{},"; //'max':'" + new Double(max).toString() + "',";
        //jsonHeaderString += "'min':'" + new Double(min).toString() + "',";
        //jsonHeaderString += "'threshold':'" + new Integer(mSoundThreshold).toString() + "'},";
        jsonHeaderString += "'values':[";


        jsonData = jsonHeaderString + jsonData;

        // add-write text into file
        try {
            //This will get the SD Card directory and create a folder named MyFiles in it.
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File (sdCard.getAbsolutePath() + "/VRL_Data");
            directory.mkdirs();

            //Now create the file in the above directory and write the contents into it
            File file = new File(directory, theMainTimeStamp +"_Nominal_data.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOutputStream);

            outputWriter.write(jsonData);
            outputWriter.flush();
            outputWriter.close();

            //display file saved message
           // Toast.makeText(getBaseContext(), "Data files saved successfully!", Toast.LENGTH_SHORT).show();
            Log.i("###SessionPresenter", "Data Saved Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }



    }



    public void appendMessagePanel(String message){

        //long time = System.currentTimeMillis();
        //android.util.Log.i("Time Class ", " Time value in millisecinds "+time);
        String fullTimeStampFormat = "yyyy-MM-dd-HHmmss.SS";
        String shortTimeStampFormat = "HH:mm:ss";

        Date currentDateTime = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat(fullTimeStampFormat);
        String fullTimeStamp = sdf.format(currentDateTime);
        //Log.i("###SessionPresenter", "Timestamp: " + fullTimeStamp);
        sdf.applyPattern(shortTimeStampFormat);
        String shortTimeStamp =sdf.format(currentDateTime);


        Log.i("###SessionPresenter", "TimeStamp: " + fullTimeStamp);
        sessionView.appendMessagePanel("[" + shortTimeStamp + "] : " + message + "\n");
    }

}
