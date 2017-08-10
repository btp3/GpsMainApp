package com.developer.iron_man.gpsmain.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

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
    //qr code scanner object
    private IntentIntegrator qrScan;

    APIServices mAPIService;
    QRModel qrModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.qr_activity,container,false);
        scan = (Button) view.findViewById(R.id.buttonScan);
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
                    // {"plate":"5600MP0596"}
                    getQRData(obj.getString("plate"));
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
            Log.e("Plate: ","565656");
        }
    }

    public void getQRData(String license_num){

        mAPIService.getDriverDetails(license_num).enqueue(new Callback<QRModel>() {
            @Override
            public void onResponse(Call<QRModel> call, Response<QRModel> response) {

                if(response.isSuccessful()) {
                    Log.i("Response from", "post submitted to API : " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<QRModel> call, Throwable t) {
                Log.e("SendLocation : ", "Unable to submit post to API.");
            }
        });

    }
}
