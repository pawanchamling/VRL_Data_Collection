package no.ntnu.pawanchamling.vrldatacollection.session.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import no.ntnu.pawanchamling.vrldatacollection.R;

/**
 * Created by Pawan Chamling on 13/05/15.
 */
public class TemperatureListener implements SensorEventListener {
    private SensorManager senseManage;
    private Sensor envSense;
    private float sensorValue;

    Context mContext;

    /** Called when the activity is first created. */

    public boolean start(Context mContext) {
        this.mContext = mContext;
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.main);
//
//        ambientBtn = (Button)findViewById(R.id.ambient_btn);
//
//        ambientBtn.setOnClickListener(this);
//
//        ambientTemperatureValue = (TextView)findViewById(R.id.ambient_text);
//        valueFields[AMBIENT]=ambientTemperatureValue;

        senseManage = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        envSense = senseManage.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if(envSense==null){
            Log.i("###TemperatureListener", "Sorry - your device doesn't have an " +
                    "ambient temperature sensor!");
            return false;
        }
        else {
            senseManage.registerListener(this, envSense, SensorManager.SENSOR_DELAY_NORMAL);
            return true;
        }

    }



    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        //find out the accuracy
        String accuracyMsg = "";
        switch(accuracy){
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accuracyMsg="Sensor has high accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accuracyMsg="Sensor has medium accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accuracyMsg="Sensor has low accuracy";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                accuracyMsg="Sensor has unreliable accuracy";
                break;
            default:
                break;
        }
        //output it
       // Toast accuracyToast = Toast.makeText(this.getApplicationContext(), accuracyMsg, Toast.LENGTH_SHORT);
       // accuracyToast.show();
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        //retrieve sensor information
        sensorValue = event.values[0];
      //  TextView currValue = ambientTemperatureValue;
        String envInfo="";

        //check type
        int currType=event.sensor.getType();
        switch(currType){
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                envInfo= sensorValue + " degrees Celsius";
               // currValue=valueFields[AMBIENT];
                break;
            default: break;
        }
        //output and reset
        //currValue.setText(envInfo);
        envSense = null;
        senseManage.unregisterListener(this);
    }


    public float getSensorValue() {
        return sensorValue;
    }

    //@Override
    public void stop() {
       // super.onPause();
        senseManage.unregisterListener(this);
        //senseManage.cancelTriggerSensor()
    }


}
