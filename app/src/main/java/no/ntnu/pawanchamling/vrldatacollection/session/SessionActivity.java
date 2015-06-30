package no.ntnu.pawanchamling.vrldatacollection.session;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.helper.FlowLayout;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;


public class SessionActivity extends ActionBarActivity implements SessionView {

    private SessionPresenter presenter;
    private static final int REQUEST_CODE = 1; //Get note from EnterANoteDialogActivity

    private TextView userActionsMessages;
    private ScrollView userNotes_scroller;
    private ScrollView ordinalValueScroller;
    private FlowLayout ordinalValuesContainer;

   // private String theMainTimeStamp;
    private boolean isSessionActive = false;

    private Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session);

        Bundle id = getIntent().getExtras();
        //this.theMainTimeStamp = id.getString("timestamp");
        settings = (Settings)id.getSerializable("settings");
        //this.theMainTimeStamp = settings.getFileTimeStamp();


        Log.i("###SessionActivity", "Serializable object received");
        Log.i("###SessionActivity", "no. of ordinal values = " + settings.getNoOfOridnalValues());

        //this.theMainTimeStamp = savedInstanceState.getString("timestamp");
        Log.i("###SessionActivity", "timestamp received: "+ settings.getFileTimeStamp());

        presenter = new SessionPresenter(this, settings.getFileTimeStamp(), settings);

        userActionsMessages = (TextView) findViewById(R.id.textView_userNotes);
        userNotes_scroller = (ScrollView)findViewById(R.id.scroller_userNotes);
        userActionsMessages.setMovementMethod(new ScrollingMovementMethod());

        ordinalValueScroller = (ScrollView) findViewById(R.id.scroller_ordinalButtons);
        ordinalValuesContainer = (FlowLayout) findViewById(R.id.layout_ordinalButtonsContainer);

        String simplerTimeStamp = settings.getFileTimeStamp();
        String timeString = simplerTimeStamp.substring(11, simplerTimeStamp.length());
        Log.i("###SessionActivity", "timeString: "+ timeString);
        simplerTimeStamp = simplerTimeStamp.substring(0, 10);
        timeString = timeString.substring(0, 2) + ":" + timeString.substring(2,4) + ":" + timeString.substring(4,timeString.length());
        simplerTimeStamp = simplerTimeStamp + " " + timeString;

        userActionsMessages.append("Session Started at [ " + simplerTimeStamp + " ] \n");

        if(settings.isOrdinalDataOn()) {
            loadOrdinalButtons();
        }
    }


    private void loadOrdinalButtons(){
        ArrayList<String> ordinalValues = settings.getOrdinalValues();

        Log.i("###OrdinalValuesActivity", "No. of Ordinal values : " + settings.getNoOfOridnalValues());
        for(int i = 0; i < ordinalValues.size(); i++) {
            String text = ordinalValues.get(i);
            String name = text;
            if(name.length() > 15) {
                name = text.substring(0, 12) + "...";
            }
            createButton(i, name, text);

        }

    }

    private void createButton(int index, String name, final String text){

        Log.i("###OrdinalValuesActivity", "Adding a button with name " + name);
        Button ordinalValueButton = new Button(this);
        ordinalValueButton.setText(name);
        ordinalValueButton.setId(index);


//        FlowLayout.LayoutParams lp =   new FlowLayout.LayoutParams(
//                120, 50);
//        ordinalValueButton.setLayoutParams(lp);

        Paint mPaint = new Paint();/*
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(64);
        mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        */
// ...
        float w = mPaint.measureText(name, 0, name.length());

        Log.i("###OrdinalValuesActivity", "Text " + name + " covers " + w + "px");

        ordinalValueButton.setWidth(600);
        ordinalValueButton.setHeight(250);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
        //        FlowLayout.LayoutParams.WRAP_CONTENT);
       // ordinalValueButton.setLayoutParams(params);


        ordinalValueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                  // v.getT
                //presenter.addOrdinalData(getFullTimeStamp(), text);//
                presenter.addOrdinalData( String.valueOf(new Date().getTime()), text);
                presenter.appendMessagePanel("[Ordinal] " + text);

                Log.i("###OrdinalValuesActivity", "Ordinal Button with text : " + text);
            }
        });

        ordinalValuesContainer.addView(ordinalValueButton);

    }


    //#####################################################

    public String getTheMainTimeStamp(){
        return settings.getFileTimeStamp();
    }

    /* When "Enter a note" button is clicked */
    public void onClickStartANoteDialogBtn(View v) {
        presenter.startANoteDialog();
    }


    public void startEnterANoteDialog(){
        //start the "Enter a note dialog activity"
        Intent intent = new Intent(SessionActivity.this, EnterANoteDialogActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /* When "Stop Session" button is clicked */
    public void onClickStopSession(View v){
        presenter.stopSession();


        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session, menu);
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
            if (data.hasExtra("noteString") && data.hasExtra("timeStamp") && data.hasExtra("javaTimeStamp")) {
               // Toast.makeText(this, data.getExtras().getString("myData1"),
               //         Toast.LENGTH_SHORT).show();
                String noteString = data.getExtras().getString("noteString");
                String timeStamp = data.getExtras().getString("timeStamp");
                String javaTimeStamp = data.getExtras().getString("javaTimeStamp");

               // System.out.println("############################");
                Log.i("###SessionActivity", "The value received = " + timeStamp + ": " + javaTimeStamp +" : " + noteString);

                presenter.addNominalNote(javaTimeStamp, noteString);
                presenter.appendMessagePanel(noteString);
            }
        }
    }

    @Override
    public void appendMessagePanel(String message){
        userActionsMessages.append(message);


        // find the amount we need to scroll.  This works by
        // asking the TextView's internal layout for the position
        // of the final line and then subtracting the TextView's height
        final int scrollAmount = userActionsMessages.getLayout().getLineTop(
                userActionsMessages.getLineCount()) - userActionsMessages.getHeight();
        // if there is no need to scroll, scrollAmount will be <=0
        if (scrollAmount > 0)
            userActionsMessages.scrollTo(0, scrollAmount);
        else
            userActionsMessages.scrollTo(0, 0);

        scrollToBottom();
        //String note = noteEditText.getText().toString();

    }


    private void scrollToBottom() {
        userNotes_scroller.post(new Runnable()
        {
            public void run()
            {
                userNotes_scroller.smoothScrollTo(0, userActionsMessages.getBottom());
            }
        });
    }


    private String getFullTimeStamp() {
        String fullTimeStampFormat = "yyyy-MM-dd-HHmmss.SS";

        Date currentDateTime = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat(fullTimeStampFormat);

        return sdf.format(currentDateTime);

    }
}
