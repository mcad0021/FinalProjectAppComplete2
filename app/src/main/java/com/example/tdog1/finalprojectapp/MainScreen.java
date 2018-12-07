package com.example.tdog1.finalprojectapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

public class MainScreen extends Activity {
    protected static final String ACTIVITY_NAME = "MainScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar myToolBar = findViewById(R.id.my_toolbar);
        setActionBar(myToolBar);
        Log.i(ACTIVITY_NAME, "In onCreate()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.launch_movie_app:
                intent = new Intent(MainScreen.this, SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.launch_food_app:
                intent = new Intent(MainScreen.this, SearchFood.class);
                startActivity(intent);
                return true;

            case R.id.info_settings:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
                builder.setMessage(getString(R.string.authors) + "\n" + getString(R.string.main_activity_help)).setTitle(R.string.help).setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
