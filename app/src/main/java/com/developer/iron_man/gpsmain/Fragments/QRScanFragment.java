package com.developer.iron_man.gpsmain.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.iron_man.gpsmain.Others.CircleTrasform;
import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;
import com.google.android.gms.drive.Drive;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import models.Driver;
import models.LocationModel;
import models.QRModel;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar on 27/7/17.
 */

public class QRScanFragment extends Fragment implements View.OnClickListener {

    private View view;
    Button scan;
    ImageView imageView,photo;
    TextView text,name,contact,address,licence_no,aadhar;
    //qr code scanner object
    private IntentIntegrator qrScan;
    ProgressDialog dialog;
    APIServices mAPIService;
    RelativeLayout info,scanner;
    PrefManager pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.qr_activity,container,false);
        scan = (Button) view.findViewById(R.id.buttonScan);
        imageView=(ImageView)view.findViewById(R.id.qr_image);
        text=(TextView)view.findViewById(R.id.text1);
        name=(TextView)view.findViewById(R.id.name);
        contact=(TextView)view.findViewById(R.id.contact);
        address=(TextView)view.findViewById(R.id.address);
        licence_no=(TextView)view.findViewById(R.id.license);
        aadhar=(TextView)view.findViewById(R.id.aadhar);
        photo=(ImageView) view.findViewById(R.id.photo);
        pref=new PrefManager(getActivity());


        dialog=new ProgressDialog(getActivity());
        scanner=(RelativeLayout)view.findViewById(R.id.scanner);
        info=(RelativeLayout)view.findViewById(R.id.info);
        scanner.setVisibility(View.VISIBLE);
        info.setVisibility(View.GONE);
        mAPIService = APIUtil.getAPIService();
        //intializing scan object
        qrScan = IntentIntegrator.forSupportFragment(this);
        //attaching onclick listener
        scan.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();
        qrScan.setBeepEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    // Expected JSON file
                    // {"gps_id":"121"}
                    scanner.setVisibility(View.GONE);
                    dialog = ProgressDialog.show(getActivity(),null,"Loading...", true);
                    getQRData(obj.getString("gps_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    public void getQRData(final String gps_id){

        mAPIService.getDriverDetails(gps_id).enqueue(new Callback<QRModel>() {
            @Override
            public void onResponse(Call<QRModel> call, Response<QRModel> response) {

                if(response.isSuccessful()) {

                    dialog.dismiss();
                    Driver driver = response.body().getDriver();
                    name.setText(""+driver.getDname());
                    contact.setText("Contact: "+driver.getDcontact());
                    address.setText(""+driver.getDaddress());
                    aadhar.setText("Aadhar Number : "+driver.getDaadhar());
                    licence_no.setText("Driving License No : "+driver.getDlicense());

                    // Loading profile image
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    getImage(driver.getDphoto()).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(getActivity())
                            .load(stream.toByteArray())
                            .asBitmap()
                            .thumbnail(0.5f)
                            .transform(new CircleTrasform(getActivity()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(photo);
                    info.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<QRModel> call, Throwable t) {

                Toast.makeText(getActivity(), "Result Not Found", Toast.LENGTH_LONG).show();

            }
        });

    }

    public Bitmap getImage(String encodedimage){
        byte[] decodedString = Base64.decode(encodedimage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
