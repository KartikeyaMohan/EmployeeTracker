package com.kmpstudios.admin.employeetracker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityAdmin extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {
    String name[],lat[],lng[];
    private GoogleMap mMap;
    int len;
    double j,k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_admin);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    protected void onStart()
    {   try
        {   super.onStart();
            //googleApiClient.connect();
        }
        catch(Exception e)
        {   Toast.makeText(getApplicationContext(),e.toString()+"onStart",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStop()
    {   try
        {   super.onStop();
            //googleApiClient.disconnect();
        }
        catch(Exception e)
        {   Toast.makeText(getApplication(),e.toString()+"onStop",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getValues();
        mMap = googleMap;
        for(int i=0;i<len;i++) {
            LatLng loc = new LatLng(Double.parseDouble(lat[i]),Double.parseDouble(lng[i]));
            mMap.addMarker(new MarkerOptions().position(loc).title(name[i]));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.markergreen);
        LatLng loc1 = new LatLng(j,k);
        mMap.addMarker(new MarkerOptions().position(loc1).title("Your Location").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(loc1 , 14) );
    }
    @Override
    protected void onResume()
    {   super.onResume();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
    }
    @Override
    public void onConnectionSuspended(int i)
    {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
    }
    @Override
    public void onMapLongClick(LatLng latLng)
    {
    }
    @Override
    public void onMarkerDragStart(Marker marker)
    {
    }
    @Override
    public void onMarkerDrag(Marker marker)
    {
    }
    @Override
    public void onMarkerDragEnd(Marker marker)
    {
    }
    @Override
    public void onDestroy()
    {   super.onDestroy();
    }
    public void getValues()
    {   Bundle bundle=getIntent().getExtras();
        name=bundle.getStringArray("Name");
        lat=bundle.getStringArray("Lat");
        lng=bundle.getStringArray("Lng");
        len=bundle.getInt("Length");
        j=bundle.getDouble("LatAdmin");
        k=bundle.getDouble("LngAdmin");
    }
}
