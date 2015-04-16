package no.ntnu.pawanchamling.vrldatacollection.AppSettings;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;

public class SettingActivity extends ActionBarActivity {

    private Settings settings;

    private static final int REQUEST_CODE = 1; //get ordinal values from the OrdinalValuesActivity

    private Switch switchGPSSetting;
    private Switch switchNoiseDataSetting;
    private Switch switchOrdinalDataSetting;

    private EditText editTextGPSInterval;
    private EditText editTextNoiseDataInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Bundle id = getIntent().getExtras();
        settings = (Settings)id.getSerializable("settings");

        switchGPSSetting = (Switch) findViewById(R.id.switch_gpsSetting);
        switchNoiseDataSetting = (Switch) findViewById(R.id.switch_noiseSensor_setting);
        switchOrdinalDataSetting = (Switch) findViewById(R.id.switch_ordinalData_setting);

        editTextGPSInterval = (EditText) findViewById(R.id.editText_gps_interval);
        editTextNoiseDataInterval = (EditText) findViewById(R.id.editText_noiseSensor_interval);

        //###Set the setting components based on the 'settings'
        switchGPSSetting.setChecked(settings.isGPSsensorOn());
        switchNoiseDataSetting.setChecked(settings.isNoiseSensorOn());
        switchOrdinalDataSetting.setChecked(settings.isOrdinalDataOn());

        editTextGPSInterval.setText("" + settings.getGPSdataScheduleTime(), TextView.BufferType.EDITABLE);
        editTextNoiseDataInterval.setText("" + settings.getNoiseDataScheduleTime(), TextView.BufferType.EDITABLE);


    }



    //### When 'Cancel' button is clicked
    public void cancelSetting(View v) {
        this.finish();
    }

    //### When 'Save Settings' button is clicked
    public void saveSetting(View v) {

        settings.setGPSsensorOn(switchGPSSetting.isChecked());
        settings.setNoiseSensorOn(switchNoiseDataSetting.isChecked());
        settings.setOrdinalDataOn(switchOrdinalDataSetting.isChecked());

        settings.setGPSdataScheduleTime(Integer.parseInt(editTextGPSInterval.getText().toString()));
        settings.setNoiseDataScheduleTime(Integer.parseInt(editTextNoiseDataInterval.getText().toString()));


        Intent dataReturnIntent = new Intent();
        dataReturnIntent.putExtra("settings", settings);
        // Activity finished ok, return the data
        setResult(RESULT_OK, dataReturnIntent);

        this.finish();
    }


    public void startOrdinalValuesActivity(View v) {

        //System.out.println("###MainActivity: Starting App Setting Activity");

        Intent ordinalValuesSettingIntent = new Intent(this, OrdinalValuesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("settings", settings );
        ordinalValuesSettingIntent.putExtras(bundle);

        startActivityForResult(ordinalValuesSettingIntent, REQUEST_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK  && requestCode == REQUEST_CODE) {
            if (data.hasExtra("settings")) {
                // Toast.makeText(this, data.getExtras().getString("myData1"),
                //         Toast.LENGTH_SHORT).show();
                this.settings = (Settings) data.getSerializableExtra("settings");
               // String timeStamp = data.getExtras().getString("timeStamp");

                // System.out.println("############################");
                Log.i("###SettingActivity", "The value received on the settings page ");

                ArrayList<String> ordinalValues = settings.getOrdinalValues();
                for(int i = 0; i < settings.getNoOfOridnalValues(); i++) {
                    Log.i("###SettingActivity", "### " + ordinalValues.get(i));
                }

            }
        }
    }


}
