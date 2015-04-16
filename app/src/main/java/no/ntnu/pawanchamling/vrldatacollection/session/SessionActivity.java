package no.ntnu.pawanchamling.vrldatacollection.session;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import no.ntnu.pawanchamling.vrldatacollection.R;
import no.ntnu.pawanchamling.vrldatacollection.model.Settings;


public class SessionActivity extends ActionBarActivity implements SessionView {

    private SessionPresenter presenter;
    private static final int REQUEST_CODE = 1; //Get note from EnterANoteDialogActivity
    private TextView userActionsMessages;
    private ScrollView userNotes_scroller;

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

        presenter = new SessionPresenter(this, settings.getFileTimeStamp());

        userActionsMessages = (TextView) findViewById(R.id.textView_user_notes);
        userNotes_scroller = (ScrollView)findViewById(R.id.userNotes_scroller);
        userActionsMessages.setMovementMethod(new ScrollingMovementMethod());
        //userNotes_scroller.addView(userActionsMessages);

        //startSensorService();

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

        //Stopping the service
       // stopSensorService();

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
            if (data.hasExtra("noteString") && data.hasExtra("timeStamp")) {
               // Toast.makeText(this, data.getExtras().getString("myData1"),
               //         Toast.LENGTH_SHORT).show();
                String noteString = data.getExtras().getString("noteString");
                String timeStamp = data.getExtras().getString("timeStamp");

               // System.out.println("############################");
                Log.i("###SessionActivity", "The value received = " + timeStamp + ": "+ noteString);

                presenter.addNominalNote(timeStamp, noteString);
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

}
