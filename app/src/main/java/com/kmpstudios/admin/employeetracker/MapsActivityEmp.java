package com.kmpstudios.admin.employeetracker;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityEmp extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    double j,k;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_emp);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
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
        LatLng loc = new LatLng(j, k);
        mMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(j,k) , 14) );
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
        j=bundle.getDouble("Lat");
        k=bundle.getDouble("Lng");
    }
}
