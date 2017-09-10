package com.developer.iron_man.gpsmain.Fragments;

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

import java.util.ArrayList;
import java.util.List;

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
        getCards("archit2693");

        return view;
    }

    public void getCards(String username){

        historyList=new ArrayList<>();

        mApiService.getUserHistory(username).enqueue(new Callback<UserJourneyList>() {
            @Override
            public void onResponse(Call<UserJourneyList> call, Response<UserJourneyList> response) {


                if(response.isSuccessful()) {

                    transportList=response.body().getTransport();
                    journeyList=response.body().getJourney();
                    Log.e("History response:",response.body().toString());
                    for(int i=0;i<journeyList.size();i++)
                        historyList.add(new History("Tue","Oct 11","9:52 PM","Auto",transportList.get(i).getLicensePlate(),journeyList.get(i).getStart(),journeyList.get(i).getEnd()));
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
}
