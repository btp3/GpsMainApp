package com.developer.iron_man.gpsmain.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.Others.HistoryRecycleGrid;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import models.History;
import models.Journey;
import models.ListVehicleLocations;
import models.Transport;
import models.UserJourneyList;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar on 27/7/17.
 */

public class HistoryFragment extends Fragment {

    private View view;
    private RecyclerView.LayoutManager layoutManager;
    HistoryRecycleGrid adapter;
    RecyclerView recyclerView;
    List<History> historyList;
    APIServices mApiService;
    PrefManager prefManager;
    List<List<Transport>> listOfTransportList;
    List<Transport> transportList;
    List<Journey> journeyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.history_layout,container,false);


        recyclerView=(RecyclerView)view.findViewById(R.id.history_recycle_grid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mApiService= APIUtil.getAPIService();
        prefManager=new PrefManager(getActivity());
        prefManager.setFragmentFlag(null);
        getCards(prefManager.getUsername());

        return view;
    }

    public void getCards(String username){

        historyList=new ArrayList<>();


        mApiService.getUserHistory(username).enqueue(new Callback<UserJourneyList>() {
            @Override
            public void onResponse(Call<UserJourneyList> call, Response<UserJourneyList> response) {

                String slash[]= new String[3];
                String cord[] = new String[2];
                String time[] =  new String[2];
                String date[] = new String[3];
                String ti , da;
                if(response.isSuccessful()) {

                    listOfTransportList=response.body().getTransport();
                    journeyList=response.body().getJourney();

                    for(int i=0;i<journeyList.size();i++)
                    {
                        Log.e(" journey get i ",journeyList.get(i).getStart());
                        slash = (journeyList.get(i).getStart()).split("-");
                        Log.e("slash ",slash[0]+"  "+slash[1]+ " "+slash[2]);
                        transportList = listOfTransportList.get(i);
                        cord = slash[0].split(":");

                        Log.e("cord",cord[0]+"  "+cord[1]);
                        String source=getAddress(Double.parseDouble(cord[0]),Double.parseDouble(cord[1]));
                        time = slash[1].split(",");
                        date = slash[2].split(",");
                        ti = time[0]+":"+time[1];
                        da = date[2]+"/"+date[1]+"/"+date[0]+" ";
                        slash = (journeyList.get(i).getEnd()).split("-");
                        cord = slash[0].split(":");
                        String dest=getAddress(Double.parseDouble(cord[0]),Double.parseDouble(cord[1]));
                        for(int j=0;j<transportList.size();j++)
                        historyList.add(new History(da,ti,"Auto",transportList.get(j).getLicensePlate(),source,dest));
                    }
                    adapter=new HistoryRecycleGrid(getActivity(), historyList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<UserJourneyList> call, Throwable t) {

                Toast.makeText(getActivity(), "History failed", Toast.LENGTH_LONG).show();

            }
        });

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        String add="";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = " ";
            add = add + obj.getAddressLine(0);
            //add = add + "\n" + obj.getCountryName();
           // add = add + "\n" + obj.getCountryCode();

           // add = add + "\n" + obj.getPostalCode();
           // add = add + "\n" + obj.getSubAdminArea();
            add = add + ", " + obj.getLocality();
            add = add + ", " + obj.getAdminArea();
           // add = add + "\n" + obj.getSubThoroughfare();

            Log.e("IGA", "Address" + add);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

       return add;
    }
}
