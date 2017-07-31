package com.developer.iron_man.gpsmain.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by sagar on 27/7/17.
 */

public class QRScanFragment extends Fragment implements View.OnClickListener {

    private View view;
    Button scan;
    TextView plate, aadhar;
    //qr code scanner object
    private IntentIntegrator qrScan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.qr_activity,container,false);
        scan = (Button) view.findViewById(R.id.buttonScan);
        plate = (TextView) view.findViewById(R.id.textViewPlate);
        aadhar = (TextView) view.findViewById(R.id.textViewAadhar);
        //intializing scan object
        qrScan = new IntentIntegrator(getActivity());
        //attaching onclick listener
        scan.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();
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
                    // {"plate":"RJ06SA2546", "aadhar":"123456123456"}
                    //setting values to textviews
                    plate.setText(obj.getString("plate"));
                    aadhar.setText(obj.getString("aadhar"));
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
}
