package com.anto.berthamandatory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anto.berthamandatory.models.Data;
import com.anto.berthamandatory.models.PostData;
import com.anto.berthamandatory.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class UserScreen extends AppCompatActivity  {
public static String Logged_In_User="LIUser";
private static final String GET_URL = "https://berthawristbandrestprovider.azurewebsites.net/api/wristbanddata";
private Data currentData;
private User loggedIn;
private String toaststring;
private Location location;
private static final String POST_URL = "https://berthabackendrestprovider.azurewebsites.net/api/data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        Intent intent = getIntent();
        loggedIn = MainActivity.Companion.GetUserByMail(intent.getStringExtra(Logged_In_User), this);
        final GetDataFromRest task = new GetDataFromRest();
        task.execute(GET_URL);
        Button logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent2);
            }
        });

    }

    public void SeeRecord(View view){

        Intent intents = new Intent(getBaseContext(), UserRecord.class);

        intents.putExtra(Logged_In_User, loggedIn.getMail());
        Log.d("INTENT12", loggedIn.getMail() + "  passed");
        startActivity(intents);

    }
    public void Refresh (View view){
        final GetDataFromRest task = new GetDataFromRest();
        task.execute(GET_URL);
    }
    public void Upload(View view){
        try{
            PostData data = createPostData();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId", data.getDeviceID());
            jsonObject.put("pm25", data.getPm25());
            jsonObject.put("pm10", data.getPm10());
            jsonObject.put("co2", data.getCo2());
            jsonObject.put("o3", data.getO3());
            jsonObject.put("pressure", data.getPressure());
            jsonObject.put("temperature", data.getTemperature());
            jsonObject.put("humidity", data.getHumidity());
            jsonObject.put("utc", data.getUtc());
            jsonObject.put("latitude", data.getLatitude());
            jsonObject.put("longitude", data.getLongtitude());
            jsonObject.put("noise", data.getNoise());
            jsonObject.put("userId", loggedIn.getMail());

            String json = jsonObject.toString();

            final PostDataToRest pdtr = new PostDataToRest();
            pdtr.execute(POST_URL, json);
            //Toast.makeText(this, toaststring, Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public PostData createPostData(){
        PostData rData = new PostData();
        Log.d("UPLOAD", Double.toString(currentData.getHumidity()));
        try{
            rData.setCo2(currentData.getCo2()); rData.setUserID(loggedIn.getMail());
            rData.setDeviceID(123213); rData.setUtc(Long.toString(new Date().getTime()));
            rData.setO3(currentData.getO3()); rData.setHumidity(currentData.getHumidity());
            rData.setPm10(currentData.getPm10()); rData.setPm25(currentData.getPm25());
            rData.setPressure(currentData.getPressure()); rData.setTemperature(currentData.getTemperature());

        }catch (NullPointerException e) {
            Toast.makeText(this, "No data provided", Toast.LENGTH_LONG).show();
            Log.e("UPLOAD", "null exception in createPostData");
            return null;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else{
            Log.e("GPS", "Permission issue");
            //return null;
        }
        if (location!=null){
            rData.setLatitude(location.getLatitude());
            rData.setLongtitude(location.getLongitude());
        }else{
            rData.setLongtitude(0);
            rData.setLatitude(0);
        }
        Log.d("UPLOAD", rData.getUtc());
        return rData;
    }



    private class PostDataToRest extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String urlS = strings[0];
            String json = strings[1];
            String returns = "0";
            try{
                URL url = new URL(urlS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(json);
                Log.d("UPLOAD", json);
                osw.close();
                returns = Integer.toString(conn.getResponseCode());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return returns;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("0")){
                toaststring = "Failed to upload data";
            }else if (s.equals("200")){
                toaststring="Data uploaded";
            }else toaststring="?????  " + s;
        }
    }
    private class GetDataFromRest extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                return getJson(strings[0]);
            }catch (IOException e){
                cancel(true);
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String json) {
            //super.onPostExecute(s);
            final TextView pm25TV = findViewById(R.id.pm25text);
            final TextView pm10TV = findViewById(R.id.pm10text);
            final TextView co2TV = findViewById(R.id.co2text);
            final TextView o3TV = findViewById(R.id.o3text);
            final TextView pressureTV = findViewById(R.id.pressureText);
            final TextView temperatureTV = findViewById(R.id.temperatureText);
            final TextView humidityTV = findViewById(R.id.humidityText);


            Gson gson = new Gson();
            Type type = new TypeToken<Data>(){}.getType();
            Data returnedData =gson.fromJson(json, type);
            currentData = returnedData;
            pm25TV.setText(Double.toString(returnedData.getPm25()));
            pm10TV.setText(Double.toString(returnedData.getPm10()));
            co2TV.setText(Double.toString(returnedData.getCo2()));
            o3TV.setText(Double.toString(returnedData.getO3()));
            pressureTV.setText(Double.toString(returnedData.getPressure()));
            temperatureTV.setText(Double.toString(returnedData.getTemperature()));
            humidityTV.setText(Double.toString(returnedData.getHumidity()));

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
                return httpConn.getInputStream();
            } else {
                throw new IOException("HTTP response not OK");
            }

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
            return json;
        }

    }

}
