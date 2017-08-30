package com.developer.iron_man.gpsmain.Services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.Activities.LocationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import models.LocationModel;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar on 21/8/17.
 */

public class LocationService extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 1000 * 2;
    private static final long FASTEST_INTERVAL = 1000 * 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;
    static double distance = 0;
    double speed;
    private APIServices mAPIService;


    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mAPIService= APIUtil.getAPIService();
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        return mBinder;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,  this);
        distance = 0;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {
        LocationActivity.locate.dismiss();
        mCurrentLocation = location;
        if (lStart == null) {
            lStart = mCurrentLocation;
            lEnd = mCurrentLocation;
        } else
            lEnd = mCurrentLocation;

        //Calling the method below updates the  live values of distance and speed to the TextViews.
        updateUI();
        //calculating the speed with getSpeed method it returns speed in m/s so we are converting it into kmph
        speed = location.getSpeed() * 18 / 5;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class LocalBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }


    }

    //The live feed of Distance and Speed are being set in the method below .
    private void updateUI() {
        if (LocationActivity.p == 0) {
            distance = distance + (lStart.distanceTo(lEnd) / 1000.00);
            LocationActivity.endTime = System.currentTimeMillis();
            long diff = LocationActivity.endTime - LocationActivity.startTime;
            diff = TimeUnit.MILLISECONDS.toMinutes(diff);

            LocationModel locationModel=new LocationModel();
            locationModel.setLatitude(lEnd.getLatitude()+"");
            locationModel.setLongitude(lEnd.getLongitude()+"");
            locationModel.setSpeed(speed+"");
            locationModel.setLocType("user");
            locationModel.setTypeId(1);

            //posting location model on the server
            sendLocation(locationModel);

            LocationActivity.time.setText("Total Time: " + diff + " minutes");
            if (speed > 0.0)
                LocationActivity.speed.setText("Current speed: " + new DecimalFormat("#.##").format(speed) + " km/hr");

            else
                LocationActivity.speed.setText(".......");

            LocationActivity.dist.setText(new DecimalFormat("#.###").format(distance) + " Km's.");

            lStart = lEnd;

        }

    }


    @Override
    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        lStart = null;
        lEnd = null;
        distance = 0;
        return super.onUnbind(intent);
    }

    public void sendLocation(LocationModel locationModel){
        Gson g = new Gson();
        Log.e("In sendLocation : ", g.toJson(locationModel));
        mAPIService.savePost(locationModel).enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                Log.e("In response : ", response.toString());
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Done posting",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Log.e("SendLocation : ", "Unable to submit post to API.");
            }
        });
    }
}
