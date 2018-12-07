package com.example.tdog1.finalprojectapp;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FoodDatabase extends Activity {
    private int count = 0;
    protected static final String ACTIVITY_NAME = "FoodDatabase";
    private ArrayList<String> array = new ArrayList<>();
    private static SQLiteDatabase foodDB;

    private static TextView food_info;

    String search_string = SearchFood.search_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_database);
        SearchQuery search = new SearchQuery();
        String urlString = "https://api.edamam.com/api/food-database/parser?app_id=bf2b6630&app_key=a33f0fcd4d9b8a8f65a9295e2b164210&ingr=";



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("query");
            Log.i("Value of value:", value); //works
            if (value.contains(" ")) {
                value = value.replace(" ", "+");
                urlString = urlString+value;
            }
            else {
                urlString = urlString+value;
            }

            search.execute(urlString);
            Log.i("URL", urlString); //works

        }
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

    public class SearchQuery extends AsyncTask<String, Integer, String> {

        String foodName;
        String foodNutrition;
        String foodCategory;
        String foodCategoryLabel;


        @Override
        protected String doInBackground(String... args) {
            Log.i(ACTIVITY_NAME, "In doInBackground");
            try {
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                Log.i(ACTIVITY_NAME, "Connection");
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "In movie getEventType");
                        continue;
                    }
                    if (parser.getName().equals("movie")) {

                        foodName = parser.getAttributeValue(null, "name");

                        foodNutrition = parser.getAttributeValue(null, "nutrition");
                        //publishProgress(20);
                        foodCategory = parser.getAttributeValue(null, "Category");
                        //publishProgress(30);
                        foodCategoryLabel = parser.getAttributeValue(null, "Label");
                        //publishProgress(40);
                    }


                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            food.setText(food.getText()+" "+foodName);
            nutrients.setText(nutrients.getText()+" "+foodNutrition);
            category.setText(category.getText()+" "+foodCategory);
            categoryLabel.setText(categoryLabel.getText()+" "+foodCategoryLabel);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
