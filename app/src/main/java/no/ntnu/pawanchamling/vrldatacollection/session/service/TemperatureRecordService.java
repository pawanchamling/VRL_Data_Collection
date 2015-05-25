package no.ntnu.pawanchamling.vrldatacollection.session.service;

import android.app.Service;
import android.content.Intent;
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
import no.ntnu.pawanchamling.vrldatacollection.session.sensor.TemperatureListener;

/**
 * Created by Pawan Chamling on 13/05/15.
 */
public class TemperatureRecordService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 3 * 1000; // 3 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    // private String theMainTimeStamp = "";


    private ArrayList<String> timeStampData;
    private ArrayList<Double> sensorData;
    private int index = 0;


    private TemperatureListener tempListener;
    public boolean isSensorPresent = false;

    /* sound data source */
    //private SoundMeter mSoundSensor;
    /** config state **/
    private int mSoundThreshold = 8;


    private long scheduledTime = 5;

    //private SensorManager mSensorManager = null; //LocationManager
   // private Sensor tempSense; //GPSListener

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

        Log.i("!!!TemperatureRecordService", "############## Here we are ###############");

        Bundle id = intent.getExtras();
        settings = (Settings)id.getSerializable("settings");
        // this.theMainTimeStamp = id.getString("timestamp");

        Log.i("!!!TemperatureRecordService", "Serializable object received");
        Log.i("!!!TemperatureRecordService", "no. of ordinal values = " + settings.getNoOfOridnalValues());
        // Log.i("!!!TemperatureRecordService", "Inside the onStartCommand ");
        Log.i("!!!TemperatureRecordService", "The Main Timestamp = " + settings.getFileTimeStamp());
        //Toast.makeText(this, "!!! Service Started " + value, Toast.LENGTH_LONG).show();
        // If we get killed, after returning from here, restart


        scheduledTime = settings.getTemperatureDataScheduleTime();
        Log.i("!!!TemperatureRecordService", "scheduledTime = " + scheduledTime);
        //### schedule task
        mTimer.scheduleAtFixedRate(new ScheduledTimerTask(), 0, scheduledTime * 1000);


        tempListener = new TemperatureListener();

        isSensorPresent = tempListener.start(this);

        if(!isSensorPresent) {
            Log.i("!!!TemperatureRecordService", "Ambient Temperature Sensor not found");
        }
       // mSensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
       // tempSense = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        Log.i("!!!TemperatureRecordService", "Inside the TemperatureRecordService");

        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // Used to record voice
           // mSoundSensor = new SoundMeter();

            // recreate new Timer
            mTimer = new Timer();

            //
            sensorData = new ArrayList<Double>();
            timeStampData = new ArrayList<String>();

            //Start sound recording
            //mSoundSensor.start();


        }




    }

    @Override
    public void onDestroy(){

        Log.i("!!!TemperatureRecordService", "Service about to get destroyed");
        //mSoundSensor.stop();  //stopping the SoundMeter

        mTimer.cancel(); //Stopping the scheduled task

        tempListener.stop();

        saveDataToTheFile(); //Save the data to the file

        super.onDestroy();
    }


    private void saveDataToTheFile() {
        Log.i("!!!TemperatureRecordService", "Saving data to the file");

        double max = 0, min = sensorData.get(0);

        String jsonData = "";

        for(int i = 0; i < sensorData.size(); i++){
            jsonData += "{\"timestamp\":\"" + timeStampData.get(i) + "\",";
            jsonData += "\"value\":\"" + new Double(sensorData.get(i)).toString() + "\"}";

            //if not the last value
            if(i != sensorData.size() - 1){
                jsonData += ",";
            }

            if(sensorData.get(i) > max){
                max = sensorData.get(i);
            }
            if(sensorData.get(i) < min){
                min = sensorData.get(i);
            }

        }
        jsonData += "]}";

        String jsonHeaderString = "{\"name\":\"Temperature Data\", \"Source\":\"Android Mobile\",\"type\":\"2\",";
        jsonHeaderString += "\"valueInfo\":{\"max\":\"" + new Double(max).toString() + "\",";
        jsonHeaderString += "\"min\":\"" + new Double(min).toString() + "\",";
        jsonHeaderString += "\"unit\":\"Celcius\",";
        jsonHeaderString += "\"threshold\":\"" + new Integer(mSoundThreshold).toString() + "\"},";
        jsonHeaderString += "\"values\":[";

        Log.i("!!!TemperatureRecordService", "Max: " + max + " & Min: " + min );

        jsonData = jsonHeaderString + jsonData;

        // add-write text into file
        try {
            //This will get the SD Card directory and create a folder named MyFiles in it.
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File (sdCard.getAbsolutePath() + "/VRL_Data");
            directory.mkdirs();

            //Now create the file in the above directory and write the contents into it
            File file = new File(directory, settings.getFileTimeStamp() +"_Temperature_data.json");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOutputStream);

            outputWriter.write(jsonData);
            outputWriter.flush();
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Data files saved successfully!", Toast.LENGTH_SHORT).show();
            Log.i("!!!TemperatureRecordService", "Data Saved Successfully");

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
                    // Toast.makeText(getApplicationContext(), getDateTime(), Toast.LENGTH_SHORT).show();

                    Log.i("!!!TemperatureRecordService", scheduledTime + " Seconds gone");
                    //double amp = mSoundSensor.getAmplitude();
                    //tempSense.

                    double tempValue = tempListener.getSensorValue();

                    if(index == 0 && tempValue == 0) {
                        Log.i("!!!TemperatureRecordService", "index = 0 and amp = 0");
                    }
                    else {
                        //Log.i("Noise", "runnable mPollTask");
                        //updateDisplay("Monitoring Voice...", amp);

                        if ((tempValue > mSoundThreshold)) {
                           // Log.i("!!!TemperatureRecordService", "Threshhold Crossed @@@");
                            //Log.i("Noise", "==== onCreate ===");
                        }

                        // Runnable(mPollTask) will again execute after POLL_INTERVAL
                        //mHandler.postDelayed(mPollTask, POLL_INTERVAL);

                        Log.i("!!!TemperatureRecordService", "[" + ++index + "] :" + getFullTimeStamp() + " tempValue: " + tempValue);

                        timeStampData.add(String.valueOf(new Date().getTime()));//getFullTimeStamp());
                        sensorData.add(new Double(tempValue));
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
