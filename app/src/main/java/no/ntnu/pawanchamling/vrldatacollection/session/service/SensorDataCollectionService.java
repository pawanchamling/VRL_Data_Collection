package no.ntnu.pawanchamling.vrldatacollection.session.service;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Config;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import no.ntnu.pawanchamling.vrldatacollection.main.MainActivity;

/**
 * Created by Pawan Chamling on 07/04/15.
 */
public class SensorDataCollectionService extends Service implements SensorEventListener {
    static final String LOG_TAG = "SENSORDATACOLLECTIONSERVICE";

   //private LocalBroadcastManager mBroadcaster;
    private boolean samplingStarted = false;
    private String sensorName;

    private ScreenOffBroadcastReceiver screenOffBroadcastReceiver = null;
    private GenerateUserActivityThread generateUserActivityThread = null;
    private SensorManager sensorManager;
    private Sensor ourSensor;

    private PrintWriter captureFile; //The file in which the data would be written
    FileOutputStream fos ;

    private Date samplingStartedTimeStamp;


    private long logCounter = 0;

    private int rate = SensorManager.SENSOR_DELAY_UI;
    private PowerManager.WakeLock sampingInProgressWakeLock;



    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        sensorName = intent.getStringExtra("sensorName");


        screenOffBroadcastReceiver = new ScreenOffBroadcastReceiver();

        IntentFilter screenOffFilter = new IntentFilter();
        screenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        System.out.println("### Starting Sampling");
        System.out.println("### Rate : " + rate);
        startSampling(); //### Start Sampling

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        System.out.println("### onSensorChanged ###");
        ++logCounter;
//        if (!MINIMAL_ENERGY) {
//            if (Sensors.DEBUG) {

                StringBuilder b = new StringBuilder();

                for (int i = 0; i < sensorEvent.values.length; ++i) {
                    if (i > 0) {
                        b.append(" , ");
                    }

                    b.append(Float.toString(sensorEvent.values[i]));
                }

                Log.d(LOG_TAG, "onSensorChanged: " + new Date().toString() + " [" + b + "]");
                System.out.println("### onSensorChanged: " + new Date().toString() + " [" + b + "]");
//            }

            if (captureFile != null) {
                System.out.println("### captureFile is not Null: ");

                captureFile.print(Long.toString(sensorEvent.timestamp));
                for (int i = 0; i < sensorEvent.values.length; ++i) {
                    captureFile.print(",");
                    captureFile.print(Float.toString(sensorEvent.values[i]));

                    System.out.println("### values = " + Float.toString(sensorEvent.values[i]));
                }

                captureFile.println();
            }
//        }
//        else {
//            ++logCounter;
//            if ((logCounter % MINIMAL_ENERGY_LOG_PERIOD) == 0L)
//                Log.d(LOG_TAG, "logCounter: " + logCounter + " at " + new Date().toString());
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onDestroy() {
        super.onDestroy();
//        if (Sensors.DEBUG)
//            Log.d(LOG_TAG, "onDestroy");
        stopSampling();
//        if (KEEPAWAKE_HACK)
//            unregisterReceiver(screenOffBroadcastReceiver);
    }

    //############################################
    private void startSampling() {
        if (samplingStarted) {
            return;
        }

      //  if (sensorName != null) {
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
            ourSensor = null;

            for (int i = 0; i < sensors.size(); ++i) {
                if (sensorName.equals(sensors.get(i).getName())) {
                    ourSensor = sensors.get(i);
                    break;
                }
            }

            if (ourSensor != null) {
//                if (Sensors.DEBUG) {
//                    Log.d(LOG_TAG, "registerListener/SamplingService");
//                }

                sensorManager.registerListener(this, ourSensor, rate);
            }

            samplingStartedTimeStamp = new Date();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

            sampingInProgressWakeLock =
                    pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SamplingInProgress");

            sampingInProgressWakeLock.acquire();

            captureFile = null;

//            if (Sensors.DEBUG) {
//                Log.d(LOG_TAG, "Capture file created");
//            }

            File captureFileName = new File("/sdcard", "vrl_sensor_data.csv");

            try {
               // fos =
                captureFile = new PrintWriter(new FileWriter(captureFileName, false));
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage(), ex);
            }
            samplingStarted = true;
       // }
    }

    //############################################
    private void stopSampling() {
        System.out.println("### Stopping Sampling");

        if (!samplingStarted) {
            return;
        }

        if (generateUserActivityThread != null) {
            generateUserActivityThread.stopThread();
            generateUserActivityThread = null;
        }

        if (sensorManager != null) {
            if (Config.DEBUG)
                Log.d(LOG_TAG, "unregisterListener/SamplingService");
            sensorManager.unregisterListener(this);
        }

        if (captureFile != null) {
            captureFile.close();
            captureFile.flush();
            captureFile = null;
            System.out.println("### Closing the file");
        }

        samplingStarted = false;
        sampingInProgressWakeLock.release();
        sampingInProgressWakeLock = null;

        Date samplingStoppedTimeStamp = new Date();
        long secondsEllapsed = (samplingStoppedTimeStamp.getTime() -
                                samplingStartedTimeStamp.getTime()) / 1000L;
        Log.d(LOG_TAG, "Sampling started: " +
                samplingStartedTimeStamp.toString() +
                "; Sampling stopped: " +
                samplingStoppedTimeStamp.toString() +
                " (" + secondsEllapsed + " seconds) " +
                "; samples collected: " + logCounter);
    }

    //#################################################

    //#############################

    class ScreenOffBroadcastReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = "ScreenOffBroadcastReceiver";

        public void onReceive(Context context, Intent intent) {
//            if (Sensors.DEBUG) {
//                Log.d(LOG_TAG, "onReceive: " + intent);
//            }

            if (sensorManager != null && samplingStarted) {
                if (generateUserActivityThread != null) {
                    generateUserActivityThread.stopThread();
                    generateUserActivityThread = null;
                }
                generateUserActivityThread = new GenerateUserActivityThread();
                generateUserActivityThread.start();
            }
        }
    }


    class GenerateUserActivityThread extends Thread {
        public void run() {
//            if (Sensors.DEBUG) {
//                Log.d(LOG_TAG, "Waiting 2 sec for switching back the screen ...");
//            }

            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException ex) {
            }

//            if (Sensors.DEBUG) {
//                Log.d(LOG_TAG, "User activity generation thread started");
//            }

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

            userActivityWakeLock =
                    pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                            "GenerateUserActivity");
            userActivityWakeLock.acquire();
//            if (Sensors.DEBUG)
//                Log.d(LOG_TAG, "User activity generation thread exiting");
        }

        public void stopThread() {
//            if (Sensors.DEBUG)
//                Log.d(LOG_TAG, "User activity wake lock released");
            userActivityWakeLock.release();
            userActivityWakeLock = null;
        }

        PowerManager.WakeLock userActivityWakeLock;
    }


}
