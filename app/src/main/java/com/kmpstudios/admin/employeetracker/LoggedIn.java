package com.kmpstudios.admin.employeetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoggedIn extends AppCompatActivity implements LocationListener {
    TextView latitude,longitude,username;
    Button som,update,logout;
    double lat,lng;
    String logval;
    String name,lat1="0.0",lng1="0.0";
    private static final String url="http://well-balanced-petro.000webhostapp.com/loginstatus.php";
    private static final String url1="http://well-balanced-petro.000webhostapp.com/logout.php";
    public static final String Key_name="Name";
    public static final String Key_log="Logged";
    public static final String Key_lat="Latitude";
    public static final String Key_long="Longitude";
    boolean gpsprovider=false,netprovider=false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 10000;
    protected LocationManager locM;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        latitude=(TextView)findViewById(R.id.latitude);
        longitude=(TextView)findViewById(R.id.longitude);
        username=(TextView)findViewById(R.id.name);
        som=(Button)findViewById(R.id.som);
        update=(Button)findViewById(R.id.update);
        logout=(Button)findViewById(R.id.logout);
        getInfo();
        latlng();
        dialog = new ProgressDialog(LoggedIn.this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Searching for GPS");
        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        username.setText(name);
        som.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   Intent i = new Intent(LoggedIn.this, MapsActivityEmp.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("Lat", lat);
                bundle.putDouble("Lng", lng);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   Intent i = new Intent(LoggedIn.this, UpdateProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   onLogOut();
                try
                {   locM.removeUpdates(LoggedIn.this);
                }
                catch(SecurityException e)
                {
                }
                finish();
            }
        });
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
    public void getInfo()
    {   Bundle bundle=getIntent().getExtras();
        name=bundle.getString("n");
    }
    private void process()
    {   logval = "1";
        lat1 = lat + "";
        lng1 = lng + "";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response)
        {   Toast.makeText(LoggedIn.this, response, Toast.LENGTH_SHORT).show();
        }
        }, new Response.ErrorListener()
        {   @Override
        public void onErrorResponse(VolleyError error)
        {   Toast.makeText(LoggedIn.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
        })
        {
        @Override
        protected Map<String, String> getParams()
        {   Map<String, String> params = new HashMap<>();
            params.put(Key_name, name);
            params.put(Key_log, logval);
            params.put(Key_lat, lat1);
            params.put(Key_long, lng1);
            return params;
        }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
    private void onLogOut()
    {   StringRequest sr = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {   Toast.makeText(LoggedIn.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener()
        {   @Override
        public void onErrorResponse(VolleyError error)
        {   Toast.makeText(LoggedIn.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {   Map<String, String> params = new HashMap<>();
                params.put(Key_name, name);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
    @Override
    public void onLocationChanged(Location location)
    {   try {
        lat = location.getLatitude();
        lng = location.getLongitude();
        process();
        if(dialog.isShowing())
        {   dialog.dismiss();
        }
        latitude.setText(lat+"");
        longitude.setText(lng+"");
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
