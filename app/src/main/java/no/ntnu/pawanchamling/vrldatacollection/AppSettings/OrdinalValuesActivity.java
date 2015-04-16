package no.ntnu.pawanchamling.vrldatacollection.AppSettings;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;

public class OrdinalValuesActivity extends ActionBarActivity {

    private Settings settings;

    private int noOfOrdinalValues;
    private ArrayList<String> ordinalValues;

    private LinearLayout containerLayout;

    private EditText editTextOrdinal1;
    private EditText editTextOrdinal2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordinal_values);

        Bundle id = getIntent().getExtras();
        settings = (Settings)id.getSerializable("settings");

        noOfOrdinalValues = settings.getNoOfOridnalValues();
        ordinalValues = settings.getOrdinalValues();

        setUpOrdinalValues();
    }


    private void setUpOrdinalValues(){
        editTextOrdinal1 = (EditText) findViewById(R.id.editText_ordinal1);
        editTextOrdinal2 = (EditText) findViewById(R.id.editText_ordinal2);
        containerLayout = (LinearLayout) findViewById(R.id.layout_container);


        for(int i = 0; i < noOfOrdinalValues; i++){
            if(i == 0) {
                editTextOrdinal1.setText("" + ordinalValues.get(i), TextView.BufferType.EDITABLE);
            }
            else if(i == 1) {
                editTextOrdinal2.setText("" + ordinalValues.get(i), TextView.BufferType.EDITABLE);
            }
            else {


                TableRow tableRow = new TableRow(this);

//                ActionBar.LayoutParams lp =
//                        new ActionBar.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lp =   new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, (float) 1.0);

                tableRow.setLayoutParams(lp);
                tableRow.setWeightSum(1.0f);
               // tableRow.setBackgroundColor(Color.BLUE);
                tableRow.setOrientation(TableRow.HORIZONTAL);

                EditText editTextOrdinalValue = new EditText(this);
                editTextOrdinalValue.setText("" + ordinalValues.get(i), TextView.BufferType.EDITABLE);
                TableRow.LayoutParams lp2 =   new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT, 0.7f);
                editTextOrdinalValue.setLayoutParams(lp2);
//                editTextOrdinalValue.setW


                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
               // TableRow.LayoutParams lp4 = new TableRow.LayoutParams()
                TableRow.LayoutParams lp3 =   new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
                deleteButton.setLayoutParams(lp3);

                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });

                tableRow.addView(editTextOrdinalValue);
                tableRow.addView(deleteButton);

                containerLayout.addView(tableRow);

            }


        }


    }


    public void saveOrdinalValues(View v) {

        this.finish();
    }

    public void cancelOrdinalValues(View v) {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ordinal_values, menu);
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
