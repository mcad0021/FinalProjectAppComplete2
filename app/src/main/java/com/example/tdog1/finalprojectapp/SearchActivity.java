package com.example.tdog1.finalprojectapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends Activity {

    private ImageButton helpButton;
    private EditText editText;
    public static String search_string;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> titleList = new ArrayList<String>();
    private static ListView list;



    /** when activity starts*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**  make it so that the editext isn't automatically in typing mode after starting app, user must actually press on the edittext*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //https://stackoverflow.com/questions/2496901/android-on-screen-keyboard-auto-popping-up
        setContentView(R.layout.activity_search);

        /** xml items */

        helpButton = (ImageButton) findViewById(R.id.help);
        /* Search activity variables */ /** Search activity variables */
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        editText = (EditText) findViewById(R.id.movie_name);
        list = (ListView) findViewById(R.id.main_list);

        list.setClickable(true);


        /** onclick listener for search button. when user presses it and then edit text contains text it parses it and searches for the movie's elements*/

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_string = editText.getText().toString(); /** turn the text entered into the edit text into the string that will go at the end of the url in processRequest*/

                if (search_string.isEmpty()) { /** don't allow user to press the search button if the edit text is empty */
                    Toast.makeText(SearchActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show(); /** display toast to tell user they cant press button without text */
                    return;
                }
                else { /** do intended if user entered text into editext*/
                    Intent intent = new Intent(SearchActivity.this, processRequest.class); /** set the intent */
                    Toast.makeText(SearchActivity.this, getString(R.string.results), Toast.LENGTH_SHORT).show(); /** display Results in toast form */
                    if (!titleList.contains(search_string)) { /** In case user enters the same movie again, don't add it to the listview*/
                        titleList.addAll( Arrays.asList(search_string) ); /** add what the user entered so they can press it another time and search it again */
                    }
                    listAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, titleList);
                    list.setAdapter(listAdapter);/** set the listview to the adapter */
                    listAdapter.notifyDataSetChanged(); /** used to stop the list items from disapearing after activity change */

                    SearchActivity.this.startActivity(intent); /** change the intent to the new intent (SearchActivity to processRequest) */
                    search_string = editText.getText().toString(); /** update the search string*/
                    intent.putExtra("search_string", search_string); /** used to debug and check if the search string is the intended value*/

                }


            }
        });

        //https://stackoverflow.com/questions/9596663/how-to-make-items-clickable-in-list-view
        /** onclick listener for the items in the list (previous searches the user can click once more to display the items) */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, processRequest.class);/** set the intent */
                SearchActivity.this.startActivity(intent);/** change the intent to the new intent (SearchActivity to processRequest) */
                search_string = titleList.get(position); /** determine which item in the list the user pressed */
                intent.putExtra("search_string", search_string); /** make the search string available in the next intent (never got this working as intended) */
            }
        });
        /** onclick listener for the help button in the top right on the toolbar*/
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /** create a dialogue alert box to instruct user in this activity*/
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                //shows help menu
                /** change the title and information of the dialogue box and also create a dialog button*/
                builder.setMessage(getString(R.string.movie_author) +"\n"+ getString(R.string.help_message1)+ "\n" + getString(R.string.version)).setTitle(R.string.help).setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        /** onclick listener for the exit button which just closes the alert so no code needed*/
                    }
                }).show(); /** display alert*/

            }
        });


    }


}