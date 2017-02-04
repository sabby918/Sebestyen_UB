package com.example.sarah.sebestyen_ub;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import android.view.ContextMenu.*;


public class MainActivity extends AppCompatActivity implements OnClickListener   {

///////////////////////////////////////////////////////////////////////////////////////

    private static final String TAG = "Count Much More";

    private static final String PREFS_NAME = "PrefsFile";

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


        if (savedInstanceState != null) {
            numBall = savedInstanceState.getInt("balls");
            numStrike = savedInstanceState.getInt("strikes");
            numOut = savedInstanceState.getInt("outs");

        }

        Button LC = (Button) findViewById(R.id.long_click);
        registerForContextMenu(LC);


        View strikeButton = findViewById(R.id.strike_button);
        strikeButton.setOnClickListener(this);

        View ballButton = findViewById(R.id.ball_button);
        ballButton.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        SharedPreferences.Editor editor = settings.edit();

        updateCount();
        editor.putInt("balls", numBall);
        editor.putInt("strikes", numStrike);
        editor.putInt("balls", numBall);
        editor.commit();
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
        switch (view.getId()) {
            case R.id.strike_button:
                numStrike++;
                validateCount();
                break;
            case R.id.ball_button:
                numBall++;
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
                Toast.makeText(getApplicationContext(),"You Need To Long Click", Toast.LENGTH_LONG).show();

        }
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Long Click Options");
        menu.add(0, v.getId(), 0, "STRIKE");
        menu.add(0, v.getId(), 0, "BALL");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="STRIKE"){
            numStrike++;
            validateCount();
            updateCount();
        }

        else if(item.getTitle()=="BALL"){
            numBall++;
            validateCount();
            updateCount();
        }
        else {
            return false;
        }
        return true;
    }

    boolean validateCount(){
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
        else
            return false;

    }



}
