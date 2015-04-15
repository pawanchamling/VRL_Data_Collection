package no.ntnu.pawanchamling.vrldatacollection.main;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import no.ntnu.pawanchamling.vrldatacollection.AppSettings.SettingActivity;
import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;
import no.ntnu.pawanchamling.vrldatacollection.session.SessionActivity;
import no.ntnu.pawanchamling.vrldatacollection.session.service.GPSRecordService;
import no.ntnu.pawanchamling.vrldatacollection.session.service.SoundRecordService;


public class MainActivity extends ActionBarActivity {
   // protected SensorDataCollectionService mServiceIntent;
    //private ResponseBroadcastReceiver receiver;
    private boolean isSessionActive = false;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings(); //retrieve settings frome the DB or file later


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

        //System.out.println("###MainActivity: Starting App Setting Activity");

        Intent appSettingIntent = new Intent(MainActivity.this, SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("settings", settings );
        appSettingIntent.putExtras(bundle);

        startActivity(appSettingIntent);
    }

    protected void onResume(){
        super.onResume();

      //  System.out.println("######### Yes Activity Resumed ##########");

        if(isSessionActive) {
            stopSensorService();
            //System.out.println("### Stopping Service");
            isSessionActive = false;
        }
        else {
           // System.out.println("### Doing nothing");
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

        //### Sound Record Service
        Intent soundRecordServiceIntent = new Intent(this, SoundRecordService.class );
        //Bundle soundServiceExtras = soundRecordServiceIntent.getExtras();
       // soundServiceExtras.putString("timestamp", fullTimeStamp);
        soundRecordServiceIntent.putExtra("timestamp", fileNameTimeStamp);
        Bundle soundRecordServiceBundle = new Bundle();
        soundRecordServiceBundle.putSerializable("settings", settings );
        soundRecordServiceIntent.putExtras(soundRecordServiceBundle);
        startService(soundRecordServiceIntent);


        //### GPS Record Service
        Intent gpsServiceIntent = new Intent(this, GPSRecordService.class );
        gpsServiceIntent.putExtra("timestamp", fileNameTimeStamp);
        Bundle gpsServiceBundle = new Bundle();
        gpsServiceBundle.putSerializable("settings", settings );
        gpsServiceIntent.putExtras(gpsServiceBundle);
        startService(gpsServiceIntent);


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


    }

    private String getTimeStampForName() {
        String fileNameTimeStampFormat = "yyyy-MM-dd-HHmmss";
        Date currentDateTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(fileNameTimeStampFormat);
        return sdf.format(currentDateTime);

    }


}
