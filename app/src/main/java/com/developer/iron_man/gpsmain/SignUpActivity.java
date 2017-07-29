package com.developer.iron_man.gpsmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import models.UserModel;

/**
 * Created by Iron_Man on 25/06/17.
 */

public class SignUpActivity extends AppCompatActivity {

    EditText email, password;
    Button submit;
    UserModel userModel;
    String userEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);

        // Creating a User with information provided by the user
        userModel = new UserModel();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userModel.setEmail(userEmail);
                userModel.setPassword(password.getText().toString());
                userModel.setUsername(userEmail.split("@")[0]);
                Log.d("Signup : ", userEmail+" "+userModel.getPassword()+" "+ userModel.getUsername());
                Intent i = new Intent(SignUpActivity.this, CompleteProfileActivity.class);
                i.putExtra("email",userEmail);
                i.putExtra("password",userModel.getPassword());
                i.putExtra("username",userModel.getUsername());
                startActivity(i);
            }
        });
    }
}
