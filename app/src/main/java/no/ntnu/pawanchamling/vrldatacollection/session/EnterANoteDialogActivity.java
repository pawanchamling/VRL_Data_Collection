package no.ntnu.pawanchamling.vrldatacollection.session;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import no.ntnu.pawanchamling.vrldatacollection.R;


public class EnterANoteDialogActivity extends ActionBarActivity implements NoteDialogView {

    private DialogPresenter dialogPresenter;

    private EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_enter_a_note_dialog);

        dialogPresenter = new DialogPresenter(this);
        noteEditText = (EditText) findViewById(R.id.editText_note);

        //Trying to have the soft keyboard shown //isn't workin
//        noteEditText.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(noteEditText, InputMethodManager.SHOW_IMPLICIT);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_a_note_dialog, menu);
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


    public void cancelDialog(View v) {
        EnterANoteDialogActivity.this.finish();
    }

    public void saveText(View v) {
       //EditText noteEditText = (EditText) findViewById(R.id.editText_note);
        String note = noteEditText.getText().toString();

        dialogPresenter.saveNote(note);
    }

    @Override
    public void saveNote(String value) {

        Intent dataReturnIntent = new Intent();
        dataReturnIntent.putExtra("noteString", value);
        dataReturnIntent.putExtra("timeStamp", getFullTimeStamp());
        dataReturnIntent.putExtra("javaTimeStamp", String.valueOf(new Date().getTime()));
        // Activity finished ok, return the data
        setResult(RESULT_OK, dataReturnIntent);

        finish();

       // EnterANoteDialogActivity.this.finish();
    }


    private String getFullTimeStamp() {
        String fullTimeStampFormat = "yyyy-MM-dd-HHmmss.SS";

        Date currentDateTime = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat(fullTimeStampFormat);

        return sdf.format(currentDateTime);

    }

}
