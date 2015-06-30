package no.ntnu.pawanchamling.vrldatacollection.session.service;

import android.app.Service;
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
import no.ntnu.pawanchamling.vrldatacollection.session.sensor.SoundMeter;

/**
 * Created by Pawan Chamling on 10/04/15.
 */
public class SoundRecordService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 3 * 1000; // 3 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private Timer mTimerStart = null;
    private Timer mTimerStop = null;

   // private String theMainTimeStamp = "";


    private ArrayList<String> timeStampData;
    private ArrayList<Double> sensorData;
    private int index = 0;

    /* sound data source */
    private SoundMeter mSoundSensor;
    /** config state **/
    private int mSoundThreshold = 8;


    private long scheduledTime = 5;
    private long scheduledSoundRecordStartTime = 5;

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
       // this.theMainTimeStamp = id.getString("timestamp");

        scheduledTime = settings.getNoiseDataScheduleTime();
        scheduledSoundRecordStartTime = settings.getNoiseDataScheduleTime();
        Log.i("!!!SoundRecordService", "scheduledTime = " + scheduledTime);
        //### schedule task
        mTimer.scheduleAtFixedRate(new ScheduledTimerTask(), 10000, scheduledTime * 1000);
        //mTimerStart.scheduleAtFixedRate(new StartSoundMeterTask(), 0, scheduledSoundRecordStartTime * 1000);
        //mTimerStop.scheduleAtFixedRate(new StopSoundMeterTask(), 20000, scheduledSoundRecordStartTime * 1000);

        Log.i("!!!SoundRecordService", "Serializable object received");
        Log.i("!!!SoundRecordService", "no. of ordinal values = " + settings.getNoOfOridnalValues());
       // Log.i("!!!SoundRecordService", "Inside the onStartCommand ");
        Log.i("!!!SoundRecordService", "The Main Timestamp = " + settings.getFileTimeStamp());
        //Toast.makeText(this, "!!! Service Started " + value, Toast.LENGTH_LONG).show();
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        Log.i("!!!SoundRecordService", "Inside the SoundRecordService");

        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // Used to record voice
            mSoundSensor = new SoundMeter();

            // recreate new Timer
            mTimer = new Timer();
            mTimerStart = new Timer();
            mTimerStop = new Timer();

            //
            sensorData = new ArrayList<Double>();
            timeStampData = new ArrayList<String>();

            //Start sound recording
            mSoundSensor.start();
        }




    }

    @Override
    public void onDestroy(){

        Log.i("!!!SoundRecordService", "Service about to get destroyed");
        mSoundSensor.stop();  //stopping the SoundMeter

        mTimer.cancel(); //Stopping the scheduled task

        saveDataToTheFile(); //Save the data to the file

        super.onDestroy();
    }


    private void saveDataToTheFile(){
        Log.i("!!!SoundRecordService", "Saving data to the file");

        if(sensorData.size() != 0){


            double max = 0, min = sensorData.get(0);

            String jsonData = "";

            for (int i = 0; i < sensorData.size(); i++) {
                jsonData += "{\"timestamp\":\"" + timeStampData.get(i) + "\",";
                jsonData += "\"value\":\"" + new Double(sensorData.get(i)).toString() + "\"}";

                //if not the last value
                if (i != sensorData.size() - 1) {
                    jsonData += ",";
                }

                if (sensorData.get(i) > max) {
                    max = sensorData.get(i);
                }
                if (sensorData.get(i) < min) {
                    min = sensorData.get(i);
                }

            }
            jsonData += "]}";

            String jsonHeaderString = "{\"name\":\"Noise Data\", \"Source\":\"Android Mobile\",\"type\":\"2\",";
            jsonHeaderString += "\"valueInfo\":{\"max\":\"" + new Double(max).toString() + "\",";
            jsonHeaderString += "\"min\":\"" + new Double(min).toString() + "\",";
            jsonHeaderString += "\"unit\":\"Amp\",";
            jsonHeaderString += "\"threshold\":\"" + new Integer(mSoundThreshold).toString() + "\"},";
            jsonHeaderString += "\"values\":[";

            Log.i("!!!SoundRecordService", "Max: " + max + " & Min: " + min);

            jsonData = jsonHeaderString + jsonData;

            // add-write text into file
            try {
                //This will get the SD Card directory and create a folder named MyFiles in it.
                File sdCard = Environment.getExternalStorageDirectory();
                File directory = new File(sdCard.getAbsolutePath() + "/VRL_Data");
                directory.mkdirs();

                //Now create the file in the above directory and write the contents into it
                File file = new File(directory, settings.getFileTimeStamp() + "_Noise_data.json");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileOutputStream);

                outputWriter.write(jsonData);
                outputWriter.flush();
                outputWriter.close();

                //display file saved message
                Toast.makeText(getBaseContext(), "Data files saved successfully!", Toast.LENGTH_SHORT).show();
                Log.i("!!!SoundRecordService", "Data Saved Successfully");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class StartSoundMeterTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    mSoundSensor.start();
                }
            });

        }
    }

    class StopSoundMeterTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    mSoundSensor.stop();
                }
            });

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
                   // Toast.makeText(getApplicationContext(), getDateTime(), Toast.LENGTH_SHORT).show();

                    Log.i("!!!SoundRecordService", scheduledTime + " Seconds gone");
                    //Start sound recording
                   // mSoundSensor.start();
                    double amp = mSoundSensor.getAmplitude();
                   // mSoundSensor.stop();  //stopping the SoundMeter

                    if(index == 0 && amp == 0) {
                        Log.i("!!!SoundRecordService", "index = 0 and amp = 0");
                    }
                    else {
                        //Log.i("Noise", "runnable mPollTask");
                        //updateDisplay("Monitoring Voice...", amp);

                        if ((amp > mSoundThreshold)) {
                            Log.i("!!!SoundRecordService", "Threshhold Crossed @@@");
                            //Log.i("Noise", "==== onCreate ===");
                        }

                        // Runnable(mPollTask) will again execute after POLL_INTERVAL
                        //mHandler.postDelayed(mPollTask, POLL_INTERVAL);

                        Log.i("!!!SoundRecordService", "[" + ++index + "] :" + getFullTimeStamp() + " Amp: " + amp);

                        timeStampData.add(String.valueOf(new Date().getTime()));//getFullTimeStamp());
                        sensorData.add(new Double(amp));
                    }

                   // mSoundSensor.stop();  //stopping the SoundMeter
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
