package com.anto.berthamandatory;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anto.berthamandatory.models.Data;
import com.anto.berthamandatory.models.PostData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class UserRecord extends AppCompatActivity implements GestureDetector.OnGestureListener {
//private ArrayAdapter adapter;
private ArrayList<PostData> recordList;
private String user;
private GestureDetector gestureDetector;
private static String RECORD_URL ="https://berthabackendrestprovider.azurewebsites.net/api/data/";
private static String SPEC_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
        GetDataFromRest task = new GetDataFromRest();
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        gestureDetector = new GestureDetector(this, this);
        Intent intent = getIntent();
        user = intent.getStringExtra(UserScreen.Logged_In_User);
        SPEC_URL = RECORD_URL + user;
        Log.d("RECORD", SPEC_URL);
        task.execute(SPEC_URL);
        Button logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent2);
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("SWIPE", "onTuch: " + event);
        boolean eventHandlingFinished = true;
        //return eventHandlingFinished;
        return gestureDetector.onTouchEvent(event);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Toast.makeText(this, "onFling", Toast.LENGTH_SHORT).show();


        boolean swipe = e1.getY() < e2.getY();

        if (swipe) {
            Log.d("SWIPE", "Swipe down");
            GetDataFromRest task = new GetDataFromRest();
            task.execute(SPEC_URL);
            Toast.makeText(this, "Refreshing...", Toast.LENGTH_LONG).show();
        }
        return true; // done
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    private class GetDataFromRest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try{
                Log.d("REST", "Started");
                return getJson(strings[0]);
            }catch (IOException e){
                Log.e("RECORD", "IOException");
                cancel(true);
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String json) {
            //super.onPostExecute(s);
            Log.d("REST", "OnPost");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PostData>>(){}.getType();
            ArrayList<PostData> list =gson.fromJson(json, type);
            recordList = list;

            Log.d("RECORD", "List size:"+recordList.size());
            ListView listview =findViewById(R.id.listView);
            ArrayAdapter<PostData> adapter = new ArrayAdapter<PostData>(getBaseContext(),
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        private InputStream openHttpConnection(final String urlString) throws IOException{
            final URL url = new URL(urlString);
            final URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");
            final HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            // No user interaction like dialog boxes, etc.
            httpConn.setInstanceFollowRedirects(true);
            // follow redirects, response code 3xx
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            final int response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {

                InputStream returned = httpConn.getInputStream();
                //httpConn.disconnect();
                return httpConn.getInputStream();
            } else {
                throw new IOException("HTTP response not OK");


        }
            //httpConn.disconnect();
        }
        private String getJson(String url) throws IOException{
            StringBuilder SBuild = new StringBuilder();
            final InputStream content = openHttpConnection(url);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            while (true){
                final String line = reader.readLine();
                if (line == null) break;
                SBuild.append(line);
            }
            String json = SBuild.toString();
            Log.d("RECORD", "JSON: " +json);
            return json;
        }

    }

}
