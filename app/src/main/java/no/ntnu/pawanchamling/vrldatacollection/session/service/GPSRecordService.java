package no.ntnu.pawanchamling.vrldatacollection.session.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import no.ntnu.pawanchamling.vrldatacollection.model.Settings;
import no.ntnu.pawanchamling.vrldatacollection.session.sensor.GPSListener;

/**
 * Created by Pawan Chamling on 14/04/15.
 */
public class GPSRecordService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 5 * 1000; // 5 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

   // private String theMainTimeStamp = "";

    private ArrayList<String> timeStampData;
    private ArrayList<Double> latitudeData;
    private ArrayList<Double> longitudeData;
    private int index = 0;

    private LocationManager mLocationManager = null;
    private GPSListener mGPSListener;

    //The Settings object
    private Settings settings;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        super.onStart(intent, startId);
        Bundle id = intent.getExtras();
        settings = (Settings)id.getSerializable("settings");
        //this.theMainTimeStamp = id.getString("timestamp");


        Log.i("!!!GPSRecordService", "Serializable object received");
        Log.i("!!!GPSRecordService", "no. of ordinal values = " + settings.getNoOfOridnalValues());
        // Log.i("!!!GPSRecordService", "Inside the onStartCommand ");
        Log.i("!!!GPSRecordService", "The Main Timestamp = " + settings.getFileTimeStamp());
        //Toast.makeText(this, "!!! Service Started " + value, Toast.LENGTH_LONG).show();
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        // cancel if already existed
        //Log.i("!!!GPSRecordService", "Inside the GPSRecordService");
        if(mTimer != null) {
            mTimer.cancel();
        } else {

            // For the GPS
            mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            mGPSListener = new GPSListener();

            timeStampData = new ArrayList<String>();
            latitudeData = new ArrayList<Double>();
            longitudeData = new ArrayList<Double>();


            // recreate new Timer
            mTimer = new Timer();


        }

        // schedule task
        mTimer.scheduleAtFixedRate(new ScheduledTimerTask(), 0, NOTIFY_INTERVAL);



    }


    @Override
    public void onDestroy(){

        Log.i("!!!GPSRecordService", "Service about to get destroyed");

        mTimer.cancel(); //Stopping the scheduled task

        saveDataToTheFile(); //Save the data to the file

        super.onDestroy();
    }


    private void saveDataToTheFile(){
        Log.i("!!!GPSRecordService", "Saving data to the file");

        //double max = 0, min = timeStampData.get(0);

        String jsonData = "";

        for(int i = 0; i < timeStampData.size(); i++){
            jsonData += "{'timestamp':'" + timeStampData.get(i) + "',";
            jsonData += "'latitude':'" + new Double(latitudeData.get(i)).toString() + "',";
            jsonData += "'longitude':'" + new Double(longitudeData.get(i)).toString() + "'}";

            //if not the last value
            if(i != timeStampData.size() - 1){
                jsonData += ",";
            }
        }

        jsonData += "]}";

        String jsonHeaderString = "{'name':'GPS Data', 'Source':'Android Mobile','type':'3',";
        jsonHeaderString += "'valueInfo':{},"; //'max':'" + new Double(max).toString() + "',";
        //jsonHeaderString += "'min':'" + new Double(min).toString() + "',";
        //jsonHeaderString += "'threshold':'" + new Integer(mSoundThreshold).toString() + "'},";
        jsonHeaderString += "'values':[";

        //Log.i("!!!GPSRecordService", "Max: " + max + " & Min: " + min );

        jsonData = jsonHeaderString + jsonData;

        // add-write text into file
        try {
            //This will get the SD Card directory and create a folder named MyFiles in it.
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File (sdCard.getAbsolutePath() + "/VRL_Data");
            directory.mkdirs();

            //Now create the file in the above directory and write the contents into it
            File file = new File(directory, settings.getFileTimeStamp() +"_GPS_data.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOutputStream);

            outputWriter.write(jsonData);
            outputWriter.flush();
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Data files saved successfully!", Toast.LENGTH_SHORT).show();
            Log.i("!!!GPSRecordService", "Data Saved Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    class ScheduledTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    // display toast
                    Toast.makeText(getApplicationContext(), getDateTime(), Toast.LENGTH_SHORT).show();

                    Log.i("!!!GPSRecordService", "5 Seconds gone");

                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mGPSListener);

                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (GPSListener.latitude > 0) {
                            Log.i("!!!GPSRecordService", "Latitude:- " + GPSListener.latitude + '\n');
                            Log.i("!!!GPSRecordService", "Longitude:- " + GPSListener.longitude + '\n');

                            timeStampData.add(getFullTimeStamp());
                            latitudeData.add(new Double(GPSListener.latitude));
                            longitudeData.add(new Double(GPSListener.longitude));

                        } else {
                            Log.i("!!!GPSRecordService", "Wait");
                            Log.i("!!!GPSRecordService", "GPS in progress, please wait.");
                        }
                    } else {
                        Log.i("!!!GPSRecordService", "GPS is not turned on...");
                    }

                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

        private String getFullTimeStamp() {
            String fullTimeStampFormat = "yyyy-MM-dd-HHmmss.SS";

            Date currentDateTime = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat(fullTimeStampFormat);

            return sdf.format(currentDateTime);

        }
    }
}
