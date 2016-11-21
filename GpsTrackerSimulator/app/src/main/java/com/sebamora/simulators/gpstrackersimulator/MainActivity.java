package com.sebamora.simulators.gpstrackersimulator;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.location.LocationManager.GPS_PROVIDER;


public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Location lastLocation;
    int id = 0;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    lastLocation = location;
                    String lat = String.format("%.12f", lastLocation.getLatitude());
                    String lng = String.format("%.12f", lastLocation.getLongitude());
                    String timestamp = getDate(lastLocation.getTime(), "dd/MM/yyyy kk:mm:ss.SSS");
                    String accuracy = String.format("%.2f",lastLocation.getAccuracy());
                    TextView txtLatLng = (TextView) findViewById(R.id.txtLatLng);
                    txtLatLng.setText(lat+":"+lng);
                    TextView txtAccuracy = (TextView) findViewById(R.id.txtAccuracy);
                    txtAccuracy.setText(accuracy+" m");
                    TextView txtTimestamp = (TextView) findViewById(R.id.txtTimestamp);
                    txtTimestamp.setText(timestamp);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        ToggleButton tglGPS = (ToggleButton) findViewById(R.id.tglGPS);
        tglGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    locationManager.requestLocationUpdates(GPS_PROVIDER, 500, 0, locationListener);
                }
                else {
                    locationManager.removeUpdates(locationListener);
                }
            }
        });
        ToggleButton tglSendPosition = (ToggleButton) findViewById(R.id.tglSendPosition);
        tglSendPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(timer!=null) {
                        timer.cancel();
                        timer = null;
                    }
                    timer = new Timer();
                    timer.schedule(new SendPositionTimerTask(), 0, 5000);
                }
                else {

                    if(timer!=null) {
                        timer.cancel();
                        timer = null;
                    }
                }
            }
        });

        GetLastIdTask lastIdTask = new GetLastIdTask();
        lastIdTask.execute("1000");
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    private void ShowSendResult(String jsonItems) {
        try {
            TextView txtResult = (TextView) findViewById(R.id.txtResult);
            txtResult.setText(jsonItems);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void ShowNewId(int id) {

        try {
            TextView txtId = (TextView) findViewById(R.id.txtNewId);
            txtId.setText(id);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
    }
    public class SendPositionTimerTask extends TimerTask{
        @Override
        public void run() {
            if (lastLocation==null) {
                return;
            }
            Marker marker = new Marker();
            marker.id = ++id;
            marker.device_id = 9999;
            marker.unit_id = 1000;
            marker.lat = lastLocation.getLatitude();
            marker.lng = lastLocation.getLongitude();
            marker.accuracy = lastLocation.getAccuracy();
            marker.type = "move";
            marker.timestamp = lastLocation.getTime();
            SendPositionTask sendTask = new SendPositionTask();
            sendTask.execute(marker);
            ShowNewId(id);
        }
    }

    public class SendPositionTask extends AsyncTask<Marker, String, String> {
        @Override
        protected String doInBackground(Marker... params) {
            try {
                String url = "http://www.trackingnorte.co.nf/createMarker.php";
                Marker marker = params[0];
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", String.valueOf(marker.id))
                        .appendQueryParameter("device_id", String.valueOf(marker.device_id))
                        .appendQueryParameter("unit_id", String.valueOf(marker.unit_id))
                        .appendQueryParameter("lat", String.valueOf(marker.lat))
                        .appendQueryParameter("lng", String.valueOf(marker.lng))
                        .appendQueryParameter("accuracy", String.valueOf(marker.accuracy))
                        .appendQueryParameter("type", marker.type)
                        .appendQueryParameter("timestamp", String.valueOf(marker.timestamp));
                String postParams = builder.build().getEncodedQuery();

                String jsonItems;
                try
                {
                    jsonItems = new WebConnection().GetJsonItems(url, postParams);
                    publishProgress(jsonItems);
                }
                catch (Exception e)
                {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                    return "Error requesting json items";
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return e.getMessage();
            }
            return "Ok";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "Ok") {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... jsonItems){
            ShowSendResult(jsonItems[0]);
            ShowNewId(id);
        }
    }

    public class GetLastIdTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String unit_id = params[0];
                String url = "http://www.trackingnorte.co.nf/getLastMarkerJSON.php";
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("unit_id", unit_id);
                String postParams = builder.build().getEncodedQuery();

                String jsonItems;
                try
                {
                    jsonItems = new WebConnection().GetJsonItems(url, postParams);
                    publishProgress(jsonItems);
                }
                catch (Exception e)
                {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                    return "Error requesting json items";
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return e.getMessage();
            }
            return "Ok";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "Ok") {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... jsonItems){
            try {
                JSONObject marker = new JSONObject(jsonItems[0]);
                id = marker.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ShowNewId(id);
        }
    }
}
