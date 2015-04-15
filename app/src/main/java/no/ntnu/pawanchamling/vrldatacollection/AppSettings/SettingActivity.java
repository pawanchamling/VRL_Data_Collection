package no.ntnu.pawanchamling.vrldatacollection.AppSettings;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;

public class SettingActivity extends ActionBarActivity {

    private Settings settings;

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
}
