package com.developer.iron_man.gpsmain;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import models.LocationModel;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationDetect extends AppCompatActivity {

    Button loactionButton;
    TextView locationText;
    // GPSTracker class
    GPSTracker gps;
    // ApiServices Object
    private APIServices mAPIService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_detect);

        loactionButton = (Button) findViewById(R.id.locationButton);
        locationText = (TextView) findViewById(R.id.locationText);
        mAPIService = APIUtil.getAPIService();
        // Show location button click event
        loactionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Create class object
                gps = new GPSTracker(LocationDetect.this);

                // Check if GPS enabled
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    locationText.setText(String.valueOf(latitude)+"  "+String.valueOf(longitude));

                    LocationModel locationModel = new LocationModel();
                    locationModel.setUser("archit");
                    locationModel.setLatitude(String.valueOf(latitude));
                    locationModel.setLongitude(String.valueOf(longitude));

                    // Sending coordinated to Server
                    sendLocation(locationModel);
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
        }
    }

    public void sendLocation(LocationModel locationModel){
        mAPIService.savePost(locationModel).enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {

                if(response.isSuccessful()) {
                    Log.i("Response from", "post submitted to API : " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Log.e("SendLocation : ", "Unable to submit post to API.");
            }
        });
    }

}
