package com.example.findu;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.findu.databinding.ActivityMapsBinding;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    BottomNavigationView bottomNav;
    Spinner spinner_mapType;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private EditText editText_search;
    private ImageView imageview_locateMe;
    // TODO 1: x Search works on emulator not physical device
    // TODO 2: x keyboard new line overwrite
    // TODO 3: x locate me button works on physical device not emulator
    // TODO 4: x Remove former marker


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Initialize the SDK
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        editText_search = findViewById(R.id.editText_search);
//        editText_search.setImeOptions(EditorInfo.IME_ACTION_GO);
        imageview_locateMe = findViewById(R.id.imageview_locateMe);
        imageview_locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });

        // set bottom navigation connection
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.map_nav_button);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.map_nav_button:
                    return true;
                case R.id.post_nav_button:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile_nav_button:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        // set spinner
        spinner_mapType = findViewById(R.id.spinnerMap);
        String[] precision = getResources().getStringArray(R.array.mapType);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, precision);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mapType.setAdapter(arrayAdapter);
        // set spinner for different map type
        spinner_mapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getItemAtPosition(i).toString();
                if (itemSelected.equals("Normal")) {
                    Log.v("map type select", itemSelected);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (itemSelected.equals("Satellite")) {
                    Log.v("map type select", itemSelected);
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (itemSelected.equals("Terrain")) {
                    Log.v("map type select", itemSelected);

                    if (itemSelected.equals("Normal")) {
                        Log.v("maptype select", itemSelected);
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    } else if (itemSelected.equals("Satellite")) {
                        Log.v("maptype select", itemSelected);
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    } else if (itemSelected.equals("Terrain")) {
                        Log.v("maptype select", itemSelected);

                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void geoLocate() {
        String searchString = editText_search.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("Map Activity ", "IOException: " + e);
        }
        if (addressList.size() > 0) {
            mMap.clear();
            Address address = addressList.get(0);
            LatLng add = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(add, 10));
            MarkerOptions options = new MarkerOptions().position(add).title(searchString);
            mMap.addMarker(options);
        }
        hideSoftKeyBoard();
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
        // Add a marker in Kyiv and move the camera
//        LatLng Kyiv = new LatLng(50.450992306749434, 30.51388704315086);
//        mMap.addMarker(new MarkerOptions().position(Kyiv).title("Marker in Kyiv"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Kyiv));
//
        getLocationPermission();

        // enable current location
        if (locationPermissionGranted) {
            getCurrentLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    // search for geolocation
                    geoLocate();
                }
                return false;
            }
        });
    }



    private void getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            LatLng cur = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 12));
                        } else {
                            Toast.makeText(MapsActivity.this, "unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Map activity", e.getMessage());
        }
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void hideSoftKeyBoard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}