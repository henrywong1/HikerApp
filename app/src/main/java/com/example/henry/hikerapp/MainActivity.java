package com.example.henry.hikerapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView currentLat;
    TextView currentLon;
    TextView currentAcc;
    TextView currentAlt;
    TextView currentAdd;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLat = findViewById(R.id.latitudeTextView);
        currentLon = findViewById(R.id.longitudeTextView);
        currentAcc = findViewById(R.id.accuracyTextView);
        currentAlt = findViewById(R.id.altitudeTextView);
        currentAdd = findViewById(R.id.addressTextView);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());
                currentLat.setText("Latitude: " + location.getLatitude());
                currentLon.setText("Longitude: " + location.getLongitude());
                currentAcc.setText("Accuracy: " + location.getAccuracy());
                currentAlt.setText("Altitude: " + location.getAltitude());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                    if (listAddress != null && listAddress.size() > 0) {
                        String address = " ";

                        if (listAddress.get(0).getThoroughfare() != null) {
                            address += listAddress.get(0).getThoroughfare() + " ";
                        }


                        if (listAddress.get(0).getLocality() != null) {
                            address += listAddress.get(0).getLocality() + " ";
                        }

                        if (listAddress.get(0).getPostalCode() != null) {
                            address += listAddress.get(0).getPostalCode() + " ";
                        }

                        if (listAddress.get(0).getAdminArea() != null) {
                            address += listAddress.get(0).getAdminArea();
                        }
                        currentAdd.setText("Address: " + address);

                    }
                }catch (Exception e) {
                    e.printStackTrace();
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
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }



}
