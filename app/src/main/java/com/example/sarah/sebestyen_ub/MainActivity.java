package com.example.sarah.sebestyen_ub;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import android.view.ContextMenu.*;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements OnClickListener , TextToSpeech.OnInitListener {

///////////////////////////////////////////////////////////////////////////////////////

    private static final String TAG = "Umpire Buddy";

    private static final String PREFS_NAME = "PrefsFile";
    private static final String TEXT_KEY = "TextKey";

    private TextToSpeech mTts;

    private int numStrike = 0;
    private int numBall = 0;
    private int numOut = 0;

//////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //save
        // The following will print to LogCat.
        Log.i(TAG, "Starting onCreate...");
        setContentView(R.layout.activity_main);
        //save

        mTts = new TextToSpeech(this, this);


        if (savedInstanceState != null) {
            numBall = savedInstanceState.getInt("balls");
            numStrike = savedInstanceState.getInt("strikes");
            numOut = savedInstanceState.getInt("outs");

        }

        CheckBox checkText = (CheckBox) findViewById(R.id.checkBox);


        Button LC = (Button) findViewById(R.id.long_click);
        registerForContextMenu(LC);


        View strikeButton = findViewById(R.id.strike_button);
        strikeButton.setOnClickListener(this);

        View ballButton = findViewById(R.id.ball_button);
        ballButton.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        final boolean t_S = settings.getBoolean(TEXT_KEY, false);
        checkText.setChecked(t_S);

        SharedPreferences.Editor editor = settings.edit();

        updateCount();
        editor.putInt("balls", numBall);
        editor.putInt("strikes", numStrike);
        editor.putInt("balls", numBall);
        editor.commit();

        checkText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getApplicationContext(), "Box is clicked", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Unclicked", Toast.LENGTH_LONG).show();

                }
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(TEXT_KEY, ((CheckBox) v).isChecked());
                // Commit the edits
                editor.commit();
            }
        });


    }


    private void updateCount() {
        TextView strikeValue = (TextView) findViewById(R.id.strike_value);
        TextView ballValue = (TextView) findViewById(R.id.ball_value);
        TextView outValue = (TextView) findViewById(R.id.out_value);

        strikeValue.setText(Integer.toString(numStrike));
        ballValue.setText(Integer.toString(numBall));
        outValue.setText(Integer.toString(numOut));


    }

    // Lifecycle methods follow

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);

        Log.i(TAG, "onSaveInstanceState()");
        icicle.putInt("strikes", numStrike);
        icicle.putInt("balls", numBall);
        icicle.putInt("outs", numOut);

    }

    @Override
    protected void onRestoreInstanceState(Bundle icicle) {
        super.onRestoreInstanceState(icicle);
        Log.i(TAG, "onRestoreInstanceState()");
    }


    @Override
    public void onClick(View view) {

        CheckBox click = (CheckBox) findViewById(R.id.checkBox);
        click.setOnClickListener(this);
        switch (view.getId()) {
            case R.id.strike_button:
                numStrike++;
                if (numStrike == 3 && click.isChecked()){
                    mTts.speak("out", TextToSpeech.QUEUE_FLUSH, null);
                }
                validateCount();
                break;
            case R.id.ball_button:
                numBall++;
                if (numBall == 4 && click.isChecked()){
                    mTts.speak("walk", TextToSpeech.QUEUE_FLUSH, null);
                }
                validateCount();
                break;
        }
        updateCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                numBall = 0;
                numStrike = 0;
                numOut = 0;
                updateCount();
                return true;
            case R.id.action_about:

                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            case R.id.long_click:
                Toast.makeText(getApplicationContext(), "You Need To Long Click", Toast.LENGTH_LONG).show();

        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Long Click Options");
        menu.add(0, v.getId(), 0, "STRIKE");
        menu.add(0, v.getId(), 0, "BALL");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CheckBox click = (CheckBox) findViewById(R.id.checkBox);
        click.setOnClickListener(this);
        if (item.getTitle() == "STRIKE") {
            numStrike++;
            if (numStrike == 3 && click.isChecked()){
                mTts.speak("out", TextToSpeech.QUEUE_FLUSH, null);
            }
            validateCount();
            updateCount();
        } else if (item.getTitle() == "BALL") {
            numBall++;
            if (numBall == 4&& click.isChecked()){
                mTts.speak("walk", TextToSpeech.QUEUE_FLUSH, null);
            }
            validateCount();
            updateCount();
        } else {
            return false;
        }
        return true;
    }

    boolean validateCount() {
        if (numStrike == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Out!")
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            numStrike = 0;
                            numBall = 0;
                            numOut++;

                            dialog.dismiss();
                            updateCount();
                        }
                    })
                    .create()
                    .show();

            return true;
        }
        if (numBall == 4) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Walk!")
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            numStrike = 0;
                            numBall = 0;
                            dialog.dismiss();
                            updateCount();
                        }
                    })
                    .create()
                    .show();


            return true;
        }
        else {

            return false;
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {

                // Language data is missing or the language is not supported.
                Log.e(TAG, "Language is not available.");
            }
            else {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
            }

        }

        else {
            // Initialization failed.
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

}
