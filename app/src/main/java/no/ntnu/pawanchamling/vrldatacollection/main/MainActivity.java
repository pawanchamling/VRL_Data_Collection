package no.ntnu.pawanchamling.vrldatacollection.main;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import no.ntnu.pawanchamling.vrldatacollection.AppSettings.SettingActivity;
import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;
import no.ntnu.pawanchamling.vrldatacollection.session.SessionActivity;
import no.ntnu.pawanchamling.vrldatacollection.session.service.GPSRecordService;
import no.ntnu.pawanchamling.vrldatacollection.session.service.SoundRecordService;
import no.ntnu.pawanchamling.vrldatacollection.session.service.TemperatureRecordService;


public class MainActivity extends ActionBarActivity {
   // protected SensorDataCollectionService mServiceIntent;
    //private ResponseBroadcastReceiver receiver;
    private boolean isSessionActive = false;

    private static final int SETTINGS_REQUEST_CODE = 1; //get Settings from SettingActivity
    private static final int SESSION_REQUEST_CODE = 2; //get Settings from SettingActivity
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings(); //retrieve settings from the DB or file later


    }

    public void startActivitySession(View v) {

        Intent sessionActivityIntent = new Intent(MainActivity.this, SessionActivity.class);
       // String fileNameTimeStamp = getTimeStampForName();

        settings.setFileTimeStamp(getTimeStampForName());
       // sessionActivityIntent.putExtra("timestamp", fileNameTimeStamp);
        Bundle bundle = new Bundle();
        bundle.putSerializable("settings", settings );
        sessionActivityIntent.putExtras(bundle);
        startActivity(sessionActivityIntent);

        startSensorService(settings.getFileTimeStamp());


    }


    public void startAppSettingActivity(View v) {

        //Log.i("###MainActivity", "Starting App Setting Activity");

        Intent appSettingIntent = new Intent(MainActivity.this, SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("settings", settings );
        appSettingIntent.putExtras(bundle);

        startActivityForResult(appSettingIntent, SETTINGS_REQUEST_CODE);
    }

    protected void onResume(){
        super.onResume();

      //  System.out.println("######### Yes Activity Resumed ##########");

        if(isSessionActive) {
            stopSensorService();
            isSessionActive = false;
        }
        else {
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void startSensorService(String fileNameTimeStamp){
        //Intent serviceIntent = new Intent(MainActivity.this, SensorDataCollectionService.class );
        //String sensorName = "MPU6500 Acceleration Sensor";
        //serviceIntent.putExtra("sensorName", sensorName);

        System.out.println("### Starting services");
        isSessionActive = true;

        // startService(serviceIntent);
        //### Temperature Record Service
        if(settings.isTemperatureSensorOn()) {
            Intent temperatureServiceIntent = new Intent(this, TemperatureRecordService.class);
            temperatureServiceIntent.putExtra("timestamp", fileNameTimeStamp);
            Bundle temperatureServiceBundle = new Bundle();
            temperatureServiceBundle.putSerializable("settings", settings);
            temperatureServiceIntent.putExtras(temperatureServiceBundle);
            startService(temperatureServiceIntent);
        }
        else {
            Log.i("###MainActivity", "Temperature Data is off so service not started");
        }


        //### Sound Record Service
        if(settings.isNoiseSensorOn()) {
            Intent soundRecordServiceIntent = new Intent(this, SoundRecordService.class);
            //Bundle soundServiceExtras = soundRecordServiceIntent.getExtras();
            // soundServiceExtras.putString("timestamp", fullTimeStamp);
            soundRecordServiceIntent.putExtra("timestamp", fileNameTimeStamp);
            Bundle soundRecordServiceBundle = new Bundle();
            soundRecordServiceBundle.putSerializable("settings", settings);
            soundRecordServiceIntent.putExtras(soundRecordServiceBundle);
            startService(soundRecordServiceIntent);
        }
        else {
            Log.i("###MainActivity", "Noise Data is off so service not started");
        }

        //### GPS Record Service
        if(settings.isGPSsensorOn()) {
            Intent gpsServiceIntent = new Intent(this, GPSRecordService.class);
            gpsServiceIntent.putExtra("timestamp", fileNameTimeStamp);
            Bundle gpsServiceBundle = new Bundle();
            gpsServiceBundle.putSerializable("settings", settings);
            gpsServiceIntent.putExtras(gpsServiceBundle);
            startService(gpsServiceIntent);
        }
        else {
            Log.i("###MainActivity", "GPS Data is off so service not started");
        }




    }

    //#####################################################
    private void stopSensorService() {

        System.out.println("### Stopping Services");

        //### Stopping Sound Record Service
        Intent soundRecordServiceIntent =   new Intent(this, SoundRecordService.class );
        stopService(soundRecordServiceIntent);

        //##@ Stopping GPS Record Service
        Intent gpsServiceIntent =   new Intent(this, GPSRecordService.class );
        stopService(gpsServiceIntent);

        //##@ Stopping Temperature Record Service
        Intent temperatureServiceIntent =   new Intent(this, TemperatureRecordService.class );
        stopService(temperatureServiceIntent);

    }

    private String getTimeStampForName() {
        String fileNameTimeStampFormat = "yyyy-MM-dd-HHmmss";
        Date currentDateTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(fileNameTimeStampFormat);
        return sdf.format(currentDateTime);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK  && requestCode == SETTINGS_REQUEST_CODE) {
            if (data.hasExtra("settings")) {
                // Toast.makeText(this, data.getExtras().getString("myData1"),
                //         Toast.LENGTH_SHORT).show();
                this.settings = (Settings) data.getSerializableExtra("settings");
                // String timeStamp = data.getExtras().getString("timeStamp");

                Log.i("###MainActivity", "The value received on the MainActivity page " );
                if(settings.isGPSsensorOn()) {
                    Log.i("###MainActivity", "GPS On");
                }
                else {
                    Log.i("###MainActivity", "GPS Off");
                }

                if(settings.isNoiseSensorOn()) {
                    Log.i("###MainActivity", "NoiseSensor On");
                }
                else {
                    Log.i("###MainActivity", "NoiseSensor Off");
                }

                if(settings.isOrdinalDataOn()) {
                    Log.i("###MainActivity", "Ordinal Data On");
                }
                else {
                    Log.i("###MainActivity", "Ordinal Data Off");
                }

                Log.i("###MainActivity", "GPS interval : " + settings.getGPSdataScheduleTime());
                Log.i("###MainActivity", "Noise Data interval : " + settings.getNoiseDataScheduleTime());

                Log.i("###MainActivity", "No. of Ordinal Values : " + settings.getNoOfOridnalValues());


                ArrayList<String> ordinalValues = settings.getOrdinalValues();
                for(int i = 0; i < settings.getNoOfOridnalValues(); i++) {
                    Log.i("###MainActivity", " : " +  ordinalValues.get(i));
                    //System.out.println("### " + ordinalValues.get(i));
                }

            }
        }
    }
}
