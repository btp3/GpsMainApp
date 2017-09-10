package com.developer.iron_man.gpsmain.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.developer.iron_man.gpsmain.Others.MyTextView;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import models.UserModel;

/**
 * Created by sagar on 27/7/17.
 */

public class ProfileFragment extends Fragment {

    private View view;
    PrefManager pref;
    UserModel obj;
    MyTextView name,state;
    ImageView profileImage,coverPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.profile_layout,container,false);
        pref=new PrefManager(getActivity());
        name=(MyTextView)view.findViewById(R.id.name);
        profileImage=(ImageView)view.findViewById(R.id.profile_image);
        coverPhoto=(ImageView)view.findViewById(R.id.cover_image);
        state=(MyTextView) view.findViewById(R.id.state);

        //Getting user details
        Gson gson = new Gson();
        String user = pref.getUser();
        obj = gson.fromJson(user, UserModel.class);

        name.setText(obj.getName());
        String address[]=obj.getAddress().split(",");
        int l=address.length;
        state.setText(address[l-2]+","+address[l-1]);
        profileImage.setImageBitmap(getImage(obj.getPhoto().trim()));
        coverPhoto.setImageBitmap(getImage(obj.getPhoto().trim()));
        return view;
    }

    public Bitmap getImage(String encodedimage){
        byte[] decodedString = Base64.decode(encodedimage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
