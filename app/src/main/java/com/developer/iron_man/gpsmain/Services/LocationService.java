package com.developer.iron_man.gpsmain.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.Activities.MainActivity;
import com.developer.iron_man.gpsmain.Activities.NotificationActivity;
import com.developer.iron_man.gpsmain.Activities.SplashActivity;
import com.developer.iron_man.gpsmain.Activities.SplashActivity;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

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
    PrefManager prefManager;
    int check=0,source=0,timer;
    

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAPIService= APIUtil.getAPIService();
        prefManager=new PrefManager(getApplication());
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        return START_STICKY;
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

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

    //The live feed of Distance and Speed are being set in the method below .
    private void updateUI() {
         {
            distance = distance + (lStart.distanceTo(lEnd) / 1000.00);

            LocationModel locationModel=new LocationModel();
            locationModel.setLatitude(lEnd.getLatitude()+"");
            locationModel.setLongitude(lEnd.getLongitude()+"");
            locationModel.setSpeed(speed+"");
            locationModel.setLocType("user");
            locationModel.setTypeId(1);

             if(check==1)
             {
                 if(speed==0.0)
                 {
                     timer++;
                     if(timer>=12)
                     {
                         sendLocation(new LocationModel("+1","+1",speed+"","user",1));
                         prefManager.setLocationF(null);
                         source=0;
                         prefManager.setNotificationFlag(null);
                         timer=0;
                         check=0;
                     }
                 }
                 else
                 {
                     if (speed > 5.0){

                         //posting location model on the server
                         timer=0;
                         if(prefManager.getLocationF()!=null)
                         {
                                 sendLocation(locationModel);
                         }
                     }
                 }
             }
             else

             {
                 if (speed > 5.0){

                    if(prefManager.getNotificationFlag()==null)
                     {
                         addNotification();
                         prefManager.setNotificationFlag("1");
                     }
                     if(prefManager.getLocationF()!=null)
                     {
                         if(source==0){
                             sendLocation(new LocationModel("-1","-1",speed+"","user",1));
                             check=1;
                             source++;
                         }
                         else
                             sendLocation(locationModel);
                     }
                 }
             }

             lStart = lEnd;

        }

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

    private void addNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_location_on_black_24dp)
                        .setContentTitle("Gps Tracker App")
                        .setContentText("Are you travelling?");
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.coins);
        mBuilder.setSound(sound);

        // mNotificationId is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify(1, mBuilder.build());

    }
}
