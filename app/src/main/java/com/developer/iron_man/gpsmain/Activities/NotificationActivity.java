package com.developer.iron_man.gpsmain.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.Others.PrefManager;
import com.developer.iron_man.gpsmain.R;

/**
 * Created by sagar on 12/9/17.
 */

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    Button sendLocation,notSendLocation;
    PrefManager prefManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_notification_layout);
        sendLocation=(Button)findViewById(R.id.send_location);
        notSendLocation=(Button)findViewById(R.id.not_send_location);
        prefManager=new PrefManager(getApplicationContext());
        sendLocation.setOnClickListener(this);
        notSendLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.send_location:
                                    prefManager.setLocationF("1");
                                    startActivity(new Intent(NotificationActivity.this,MainActivity.class));
                                    finish();
            case R.id.not_send_location:
                                        prefManager.setLocationF(null);
                                        sendSMS("","");
                                        finish();
        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+918989561895", null, "Hii", null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
