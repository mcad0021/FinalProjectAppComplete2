package com.example.tdog1.finalprojectapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class processRequest extends Activity {

    /** class variables*/

    String search_string = SearchActivity.search_string;  /** get string entered in search activity by user*/

    protected static final String ACTIVITY_NAME = "processRequest";
    private ProgressBar progressBar;
    public static TextView movie_info;

    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> movieList = new ArrayList<String>();

    private TextView main_text;
    private ImageView movie_poster;
    private ImageButton helpButton;
    private ImageButton favouritesButton;
    private ListView movie_list;
    MovieDatabaseHelper movieDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("search_string", search_string);  /** tell me that i have the right string entered in the search activity*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request);

        /** xml attributes*/
        helpButton = (ImageButton) findViewById(R.id.help);
        favouritesButton = (ImageButton) findViewById(R.id.favourites);
        movie_poster = (ImageView) findViewById(R.id.poster);
        movie_list = (ListView) findViewById(R.id.movie_list);
        progressBar = (ProgressBar) findViewById(R.id.search_progress);
        main_text = (TextView) findViewById(R.id.mainText);
        SearchQuery search = new SearchQuery(); /** SearchQuery inner class object*/
        movieDatabaseHelper = new MovieDatabaseHelper(this);  /** database object*/
        String urlString = "http://www.omdbapi.com/?apikey=6c9862c2&r=xml&t=";  /** set the first part of the url */

        Log.i("search_string", search_string);  /** tell me that i have the right string entered in the search activity*/

        /** if the search string has a space concate the search string with a + instead else just add it to the end of urlString*/
        if (search_string.contains(" ")) {
            search_string = search_string.replace(" ", "+");
            urlString = urlString + search_string;
        } else {
            urlString = urlString + search_string;
        }

        search.execute(urlString);  /** use the urlString as a parameter in SearchQuery*/

        Log.i("URL", urlString);  /** tell me that i have the right url in the debug */

        movie_poster.setOnClickListener(new View.OnClickListener() {  /** onclick listener of the poster*/
            @Override
            public void onClick(View view) {  /** when clicked display an alert in this activity*/
                AlertDialog.Builder builder = new AlertDialog.Builder(processRequest.this);
                /**set the text and title of the alert and create a dialoge button to save the movie*/
                builder.setMessage("\n" + getString(R.string.save_message)).setTitle(R.string.save).setNegativeButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newEntry = movieList.get(0);  /** set the string im inputting in to the database as the 0th index of the movieList*/
                        Log.i("newEntry", newEntry);  /** tell me that i have the right value to be entered in the database*/

                            AddData(newEntry);  /** add the movie to the database*/

                    }
                }).show();  /** show the alert*/
            }
        });



        helpButton.setOnClickListener(new View.OnClickListener() {  /**onclick listener for the help button at the top right on top of the tool bar*/
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(processRequest.this);  /** create alert in this activity */
                /** set the body, and title of the alert and create an exit button*/
                builder.setMessage(getString(R.string.movie_author)+ "\n" + getString(R.string.version) + "\n" + getString(R.string.help_message2)).setTitle(R.string.help).setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        /** dont do anything when pressing exit except hide the alert*/
                    }
                }).show();  /** show the alert*/

            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {  /** onclick listener for favourites*/
            @Override
            public void onClick(View view) {
                /** set the intent */
                Intent intent = new Intent(processRequest.this, favouritesActivity.class);
                Log.i("favoritesbuttonb", "Calling startActivityfavoritesActivity");
                startActivity(intent); /**change the intent from processRequest to favouritesActivity*/
            }
        });

    }

    public void AddData (String newEntry) {
        boolean insertData = movieDatabaseHelper.addData(newEntry);  /** add the data using MovieDatabaseHelper's data object*/
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public class SearchQuery extends AsyncTask<String, Integer, String> {  /** AsyncTask to process the URL and get all the attributes*/


        /** attributes of the movie request*/
        public String movieName;
        public String movieYear;
        public String movieRating;
        public String mainActors;
        public String plot;
        public String moviePoster_URL;
        public Bitmap imageBitmap;


        @Override
        protected String doInBackground(String... args) {

            Log.i(ACTIVITY_NAME, "In doInBackground");  /** tell me im using the doInBackground method*/
            try {
                /** establish connection */
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                Log.i(ACTIVITY_NAME, "Connection");
                conn.connect();
                /** create parser object and the input stream and then set the input*/
                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);
                /** parse until the end of the xml api*/
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "In movie getEventType");
                        continue;
                    }

                    if (parser.getName().equals("movie")) {

                        movieName = parser.getAttributeValue(null, "title");/** find attribute with title and get it's content*/
                        publishProgress(0);/** set the progress bar to progress of 0*/
                        movieYear = parser.getAttributeValue(null, "year");/** find attribute with year and get it's content*/
                        publishProgress(20);/** set the progress bar to progress of 20*/
                        movieRating = parser.getAttributeValue(null, "rated");/** find attribute with rating and get it's content*/
                        publishProgress(40);/** set the progress bar to progress of 40*/
                        mainActors = parser.getAttributeValue(null, "actors");/** find attribute with actors and get it's content*/
                        publishProgress(60);/** set the progress bar to progress of 60*/
                        plot = parser.getAttributeValue(null, "plot");/** find attribute with plot and get it's content*/
                        publishProgress(80);/** set the progress bar to progress of 80*/
                        moviePoster_URL = parser.getAttributeValue(null, "poster");/** find attribute with poster and get it's content*/
                        publishProgress(100);/** set the progress bar to progress of 100*/
                        URL imageURL = new URL(moviePoster_URL); /** set the url that was parsed to a new url to get the bitmap*/
                        imageBitmap = getImage(imageURL); /** get the image using the url by using the getImage method*/

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
            progressBar.setVisibility(View.INVISIBLE); /** set the progress bar to invisible*/
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            /** after the parsing is  finish do this*/
            movieList.clear(); /** clear movie list to not have old values after changing intents*/
            /** add all the values to the arraylist*/
            movieList.addAll( Arrays.asList(movieName , getString(R.string.year)+" "+ movieYear,
                    getString(R.string.rating)+ " " + movieRating, getString(R.string.actors) + " " +  mainActors, getString(R.string.plot) +" "+
                            plot, getString(R.string.poster_url) +" "+  moviePoster_URL) );
            listAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, movieList); /** set the array addapter layout to simple_list_item_1 and movieList*/
            movie_list.setAdapter(listAdapter); /** set the movie_list's adapter*/
            listAdapter.notifyDataSetChanged(); /**make it so that the data isnt lost when switching intent*/
            movie_poster.setImageBitmap(imageBitmap); /**set the xml movie poster to the bitmap image*/


        }

    }
    /**get the bitmap image method*/
    protected static Bitmap getImage(URL url) {
        /**establish a new connection*/
        Log.i(ACTIVITY_NAME, "Downloading movie poster");
        HttpURLConnection iconConn = null;
        try {
            iconConn = (HttpURLConnection) url.openConnection();
            iconConn.connect();
            int response = iconConn.getResponseCode();
            if (response == 200) {
                return BitmapFactory.decodeStream(iconConn.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (iconConn != null) {
                iconConn.disconnect();
            }
        }


    }



}