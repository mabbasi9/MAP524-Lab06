package com.example.student.myfriends;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;




public class MainActivity extends ActionBarActivity{

    SqlLiteDbHelper dbHelper;
    Friends contacts;
    public String dbname = "";
    EditText mytext;
    List name1;
    ListView lv;
    Context my;
    boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        my = this;
        //dbHelper = new SqlLiteDbHelper(this);

        final Intent myIntent = new Intent(this, ShowList.class);
        mytext = (EditText) findViewById(R.id.editText);


        mytext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                        keyCode == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    dbname = mytext.getText().toString();

                    //checks for user-entered database exists
                    AssetManager assetManager = getAssets();
                    try {
                        // for assets folder add empty string
                        String[] filelist = assetManager.list("");
                        // for assets/subFolderInAssets add only subfolder name
                        String[] filelistInSubfolder = assetManager.list("subFolderInAssets");
                        if (filelist == null) {
                            // dir does not exist or is not a directory
                        } else {
                            for (int i = 0; i < filelist.length; i++) {
                                if (filelist[i].equals(dbname)) {
                                    ok = true;
                                    break;
                                }
                            }
                        }

                        // if(filelistInSubfolder == null) ............

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (ok) {
                        dbHelper = new SqlLiteDbHelper(my, dbname);
                        mytext.setText("");
                        dbHelper.openDataBase();
                        contacts = new Friends();

                        name1 = dbHelper.Get_Name();
                        lv = (ListView) findViewById(R.id.listView);


                        ArrayAdapter adapter = new ArrayAdapter(my,
                                android.R.layout.simple_list_item_1, name1);

                        lv.setAdapter(adapter);

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView parent, View view, int position, long id) {


                                dbHelper.openDataBase();
                                contacts = dbHelper.Get_ContactDetails(position + 1);
                                String s = "Email: " + contacts.getEmail() + "\n" + "Phone Number: " +contacts.getMobileNo();
                                myIntent.putExtra("key",s);
                                startActivity(myIntent);
                                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                            }
                        });
                        return false;
                    } else {
                        mytext.setText("");
                        Toast.makeText(getApplicationContext(), "No database exists", Toast.LENGTH_SHORT).show();

                        return false;
                    }

                } else {

                    return false;
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_help) {
            startActivity(new Intent(this, Help.class));
        }

        if (id == R.id.action_about) {
            startActivity(new Intent(this, About.class));
        }

        return super.onOptionsItemSelected(item);
    }
}




