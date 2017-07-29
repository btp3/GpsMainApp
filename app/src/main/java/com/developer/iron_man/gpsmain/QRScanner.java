package com.developer.iron_man.gpsmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Iron_Man on 03/07/17.
 */

public class QRScanner extends AppCompatActivity implements View.OnClickListener{

    Button scan;
    TextView plate, aadhar;
    //qr code scanner object
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity);
        scan = (Button) findViewById(R.id.buttonScan);
        plate = (TextView) findViewById(R.id.textViewPlate);
        aadhar = (TextView) findViewById(R.id.textViewAadhar);
        //intializing scan object
        qrScan = new IntentIntegrator(this);
        //attaching onclick listener
        scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        qrScan.initiateScan();
    }
    //Getting the scan results

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
