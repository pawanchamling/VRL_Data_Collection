package no.ntnu.pawanchamling.vrldatacollection.AppSettings;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;

public class OrdinalValuesActivity extends ActionBarActivity {

    private Settings settings;

    private int noOfOrdinalValues;
    private ArrayList<String> ordinalValues;

    private LinearLayout containerLayout;

    private EditText editTextOrdinal1;
    private EditText editTextOrdinal2;

    private int addedOrdinalValueIndex;
    private LinkedHashMap<Integer, Integer> addedOrdinalValues = new LinkedHashMap<Integer, Integer>();

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


        for(int i = 0; i < noOfOrdinalValues; i++) {
            if(i == 0) {
                editTextOrdinal1.setText("" + ordinalValues.get(i), TextView.BufferType.EDITABLE);
            }
            else if(i == 1) {
                editTextOrdinal2.setText("" + ordinalValues.get(i), TextView.BufferType.EDITABLE);
            }
            else {
                addTableRowAndValues(i, "" + ordinalValues.get(i));
            }
        }

        addedOrdinalValueIndex = noOfOrdinalValues - 1;//this is the highest index so far

    }



    private void addTableRowAndValues(int index, String text) {
        TableRow tableRow = new TableRow(this);
        LinearLayout.LayoutParams lp =   new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, (float) 1.0);

        tableRow.setLayoutParams(lp);
        tableRow.setWeightSum(1.0f);
        tableRow.setId(1000 + index);
        //  tableRow.setTag();
        // tableRow.setBackgroundColor(Color.BLUE);
        tableRow.setOrientation(TableRow.HORIZONTAL);

        EditText editTextOrdinalValue = new EditText(this);
        editTextOrdinalValue.setId(2000 + index);
        editTextOrdinalValue.setText(text, TextView.BufferType.EDITABLE);
        TableRow.LayoutParams lp2 =   new TableRow.LayoutParams(
                0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f);
        editTextOrdinalValue.setLayoutParams(lp2);


        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setId(index);
        TableRow.LayoutParams lp3 =   new TableRow.LayoutParams(
                0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
        deleteButton.setLayoutParams(lp3);

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //delete the textView that the button is contained
                int index = v.getId();
                TableRow tr = (TableRow) findViewById(1000 + index);
                ViewGroup parentView = (ViewGroup) v.getParent().getParent();
                parentView.removeView(tr);
                //--noOfOrdinalValues;
                addedOrdinalValues.remove(index);

                System.out.println("###OrdinalValuesActivity: A TableRow with Id = " + 1000 + index + " Deleted");
                System.out.println("###OrdinalValuesActivity: Current Ordinal Value Index = " + addedOrdinalValueIndex);
            }
        });

        //adding the textview and the deletebutton to the TableRow
        tableRow.addView(editTextOrdinalValue);
        tableRow.addView(deleteButton);
        System.out.println("###OrdinalValuesActivity: A TableRow with Id = " + 1000 + index + " Added");

        addedOrdinalValues.put(index, index);

        containerLayout.addView(tableRow);


    }

    public void addOrdinalValue(View v) {
        addTableRowAndValues(++addedOrdinalValueIndex , "");
        ++noOfOrdinalValues;
        System.out.println("###OrdinalValuesActivity: Current Ordinal Value Index = " + addedOrdinalValueIndex);
    }

    public void saveOrdinalValues(View v) {

        System.out.println("###OrdinalValuesActivity: No.Of extra Ordinal Values = " + addedOrdinalValues.size());
        // addedOrdinalValues.keySet()
        ArrayList<String> ordinalValues = new ArrayList<String>();

        ordinalValues.add(editTextOrdinal1.getText().toString());
        ordinalValues.add(editTextOrdinal2.getText().toString());

        for(Integer key: addedOrdinalValues.keySet()) {
            // System.out.println("### "+ key + " - " + addedOrdinalValues.get(key));
            EditText et = (EditText) findViewById(2000 + key);
            ordinalValues.add(et.getText().toString());

            System.out.println("###OrdinalValuesActivity: value : " + et.getText().toString());
        }

        settings.setOrdinalValues(ordinalValues);
        settings.setNoOfOridnalValues(ordinalValues.size());

        Intent dataReturnIntent = new Intent();
        dataReturnIntent.putExtra("settings", settings);
        // Activity finished ok, return the data
        setResult(RESULT_OK, dataReturnIntent);

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
