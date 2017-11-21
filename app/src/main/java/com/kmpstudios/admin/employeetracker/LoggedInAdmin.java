package com.kmpstudios.admin.employeetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoggedInAdmin extends AppCompatActivity implements LocationListener {
    ListView lv;
    Button som,logout,refresh;
    JSONArray a;
    double la,ln;
    boolean gpsprovider,netprovider;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 10000;
    protected LocationManager locM;
    ProgressDialog dialog;
    String name[],lat[],lng[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_admin);
        lv=(ListView)findViewById(R.id.listview);
        som=(Button)findViewById(R.id.som);
        logout=(Button)findViewById(R.id.logout);
        refresh=(Button)findViewById(R.id.refresh);
        latlng();
        dialog = new ProgressDialog(LoggedInAdmin.this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Searching for GPS");
        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        getJSON("http://well-balanced-petro.000webhostapp.com/loggedinemployees.php");
        som.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   Intent i = new Intent(LoggedInAdmin.this, MapsActivityAdmin.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("Name", name);
                bundle.putStringArray("Lat", lat);
                bundle.putStringArray("Lng", lng);
                bundle.putInt("Length", a.length());
                bundle.putDouble("LatAdmin", la);
                bundle.putDouble("LngAdmin", ln);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   try
                {   locM.removeUpdates(LoggedInAdmin.this);
                }
                catch(SecurityException e)
                {
                }
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   getJSON("http://well-balanced-petro.000webhostapp.com/loggedinemployees.php");
            }
        });
    }
    public void getJSON(final String urlWebService)
    {   class GetJSON extends AsyncTask<Void, Void, String> {
        String geturl=urlWebService;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(), s+" = JSON Array", Toast.LENGTH_SHORT).show();
            try {
                check(s);
            }
            catch(Exception e)
            {   Toast.makeText(getApplicationContext(),e.toString()+" = JSONException",Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(geturl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            }
            catch (Exception e)
            {   return null;
            }
        }
    }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    public void latlng()
    {   try
    {   locM = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsprovider=locM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        netprovider=locM.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(gpsprovider)
        {   locM.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
        else if(netprovider)
        {   locM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
    }
    catch(SecurityException e)
    {
    }
    }
    public void check(String s)throws JSONException {
        a = new JSONArray(s);
        name = new String[a.length()];
        lat = new String[a.length()];
        lng = new String[a.length()];
        for (int i = 0; i < a.length(); i++) {
            JSONObject obj = a.getJSONObject(i);
            name[i] = obj.getString("Name");
            lat[i] = obj.getString("Latitude");
            lng[i] = obj.getString("Longitude");
        }
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        lv.setAdapter(arrayAdapter);
    }
    @Override
    public void onLocationChanged(Location location)
    {   try {
        la = location.getLatitude();
        ln = location.getLongitude();
        if(dialog.isShowing())
        {   dialog.dismiss();
        }
        double acc = location.getAccuracy();
        String i = acc + "";
        if (gpsprovider)
        {   if (acc < 0) {
            try {
                locM.removeUpdates(this);
            } catch (SecurityException e) {
            }
        }
        } else
        {   if (acc < 0) {
            try {
                locM.removeUpdates(this);
            } catch (SecurityException e) {
            }
        }
        }
    }
    catch(Exception e)
    {   Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
