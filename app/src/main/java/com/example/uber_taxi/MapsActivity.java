package com.example.uber_taxi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

    }

    protected synchronized void buildGoogleApiClient(){


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

//        moving the map/ camera in the same pace in which the user moves so that user always stays in the center of the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15)); //ITS VALUE RANGES FROM (1-21) you can play around with these indices i.e 11



        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

        //geogire automatically , in its own way puts value in the database
        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error!=null)
                {
                    Toast.makeText(MapsActivity.this,"Can't go Active",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MapsActivity.this,"You are Active",Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //when the map is connected and is ready to get started

        //getting location second to second

        mLocationRequest = new LocationRequest();
        mLocationRequest.setFastestInterval(1000); //1000 = 1second
//        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //This helps in getting the best location accuracy that mobile can get

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //This onStop() method is when the user closes the application then SUCH drives are regarded as inactive driver and removing its location values

    @Override
    protected void onStop() {
        super.onStop();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

        //geogire automatically , in its own way puts value in the database
        GeoFire geoFire = new GeoFire(ref);

        //removing the location of this particular driver from the database when he goes out of the activity
        geoFire.removeLocation(userId);
    }
}
