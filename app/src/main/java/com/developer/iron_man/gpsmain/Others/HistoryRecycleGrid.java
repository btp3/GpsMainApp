package com.developer.iron_man.gpsmain.Others;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.developer.iron_man.gpsmain.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import models.History;

/**
 * Created by sagar on 2/8/17.
 */

public class HistoryRecycleGrid extends RecyclerView.Adapter<HistoryRecycleGrid.MyHolder>{

    public RecyclerView re;
    private List<History> dataSet ;
    public Context context=null;


    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView weekday;
        TextView date;
        TextView time;
        TextView vehicle;
        TextView veh_num;
        TextView source;
        TextView dest;

        public MyHolder(View itemView)
        {
            super(itemView);
            this.weekday = (TextView) itemView.findViewById(R.id.week);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.time=(TextView) itemView.findViewById(R.id.time);
            this.vehicle=(TextView)itemView.findViewById(R.id.vehicle_type);
            this.veh_num=(TextView)itemView.findViewById(R.id.vehicle_num);
            this.source=(TextView)itemView.findViewById(R.id.source);
            this.dest=(TextView)itemView.findViewById(R.id.dest);
        }
    }

    public HistoryRecycleGrid(Context c, List<History> data)
    {

        this.dataSet = data;
        context=c;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card_layout, parent, false);
        MyHolder myHolder=new MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.history_recycle_grid);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        TextView weekday=holder.weekday;
        TextView date=holder.date;
        TextView time=holder.time;
        TextView vehicle=holder.vehicle;
        TextView veh_num=holder.veh_num;
        TextView source=holder.source;
        TextView dest=holder.dest;

        //weekday.setText(dataSet.get(position).getWeekday());
        date.setText(dataSet.get(position).getDate());
        time.setText(dataSet.get(position).getTime());
        vehicle.setText(dataSet.get(position).getVehicle_type());
        veh_num.setText(dataSet.get(position).getVehicle_num());
        source.setText(dataSet.get(position).getSource());
        dest.setText(dataSet.get(position).getDest());

        }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}

