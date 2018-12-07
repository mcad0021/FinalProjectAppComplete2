package com.example.tdog1.finalprojectapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class favouritesActivity extends Activity {

    /** variables for the array adapter, the database object, the movie name string, the listview, and the textview*/
    private ListView favourites_list;
    private String movie;
    private ArrayAdapter<String> listAdapter;
    private TextView info_text;

    MovieDatabaseHelper movieDatabaseHelper;

    @Override
    /** occurs when activity is opened*/
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("FavoritesActivity", "Calling onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        /** xml attributes*/
        favourites_list = (ListView) findViewById(R.id.favourites_list);
        info_text = (TextView) findViewById(R.id.info);

        movieDatabaseHelper = new MovieDatabaseHelper(this);/** database object*/

        Cursor data = movieDatabaseHelper.getData();/** set the data of the database to this cursor object*/

        final ArrayList<String> movieList = new ArrayList<>(); /**initialize the arraylist*/


        while (data.moveToNext()) { /** add the data to the list until nothing is left*/
            movieList.add(data.getString(0));/** add the data at index 0 to the movie list*/
            listAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, movieList); /** set the array adapter to have a layout of simple_list_item_1*/
            favourites_list.setAdapter(listAdapter); /** set the favourites list's adapter to listAdapter*/
            //.notifyDataSetChanged(); /** prevent the info on screen to be changed based on activity change*/
        }
//        Log.i("FavoritesActivity Movie", movieList.get(0));
        favourites_list.setOnItemClickListener(new AdapterView.OnItemClickListener() { /** on click listener for favourites list  */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { /** on click listener for favourites list items*/
                info_text.setText(""); /** clear the textview so that it doesn't keep old info*/
                movie = movieList.get(position); /**get the item which the user pressed*/
                Log.i("Movie selected", movie); /** tell me when i debug what the value of movie is*/

                info_text.setText(getString(R.string.movie_info) + info_text.getText()+"\n"+getString(R.string.movie_enter) +" "+movie);  /** set the string of info text */

            }
        });
    }

}


/** Delete does not work and I couldn't get the statistics of the movies to work.*/