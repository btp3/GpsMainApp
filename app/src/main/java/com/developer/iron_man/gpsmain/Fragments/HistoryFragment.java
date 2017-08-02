package com.developer.iron_man.gpsmain.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.developer.iron_man.gpsmain.Others.HistoryRecycleGrid;
import com.developer.iron_man.gpsmain.R;

import java.util.ArrayList;
import java.util.List;

import models.History;

/**
 * Created by sagar on 27/7/17.
 */

public class HistoryFragment extends Fragment {

    private View view;
    private RecyclerView.LayoutManager layoutManager;
    HistoryRecycleGrid adapter;
    RecyclerView recyclerView;
    List<History> historyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.history_layout,container,false);

        recyclerView=(RecyclerView)view.findViewById(R.id.history_recycle_grid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getCards();

        return view;
    }

    public void getCards(){

        //setting the data in history layout/cards (Data from APIs will be received here and filled in cards)
        historyList=new ArrayList<>();
        historyList.add(new History("Tue","Oct 11","09:52 PM","Auto :","KRN 186827515","112,University Road, Govindpuri,Gwalior,Mad...","Char Sahar Ka Naka Road,New Grasim Vihar..."));
        historyList.add(new History("Tue","Oct 11","09:52 PM","Auto :","KRN 186827515","112,University Road, Govindpuri,Gwalior,Mad...","Char Sahar Ka Naka Road,New Grasim Vihar..."));
        historyList.add(new History("Tue","Oct 11","09:52 PM","Auto :","KRN 186827515","112,University Road, Govindpuri,Gwalior,Mad...","Char Sahar Ka Naka Road,New Grasim Vihar..."));
        historyList.add(new History("Tue","Oct 11","09:52 PM","Auto :","KRN 186827515","112,University Road, Govindpuri,Gwalior,Mad...","Char Sahar Ka Naka Road,New Grasim Vihar..."));
        historyList.add(new History("Tue","Oct 11","09:52 PM","Auto :","KRN 186827515","112,University Road, Govindpuri,Gwalior,Mad...","Char Sahar Ka Naka Road,New Grasim Vihar..."));
        historyList.add(new History("Tue","Oct 11","09:52 PM","Auto :","KRN 186827515","112,University Road, Govindpuri,Gwalior,Mad...","Char Sahar Ka Naka Road,New Grasim Vihar..."));
        adapter=new HistoryRecycleGrid(getActivity(), historyList);
        recyclerView.setAdapter(adapter);
    }
}
