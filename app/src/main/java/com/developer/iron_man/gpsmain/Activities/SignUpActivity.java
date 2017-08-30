package com.developer.iron_man.gpsmain.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.iron_man.gpsmain.CompleteProfileActivity;
import com.developer.iron_man.gpsmain.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import models.LoginModel;
import models.User;
import models.UserModel;
import retrofit.APIServices;
import retrofit.APIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Iron_Man on 25/06/17.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, password,confirm_password;
    TextView sign_in,sign_up,sign_in_text,sign_up_text;
    LinearLayout layout1,layout2;
    Button submit,signInButton;
    UserModel userModel;
    String userEmail;
    APIServices mAPIService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        submit = (Button) findViewById(R.id.submit);
        sign_in=(TextView)findViewById(R.id.sign_in);
        sign_up=(TextView)findViewById(R.id.sign_up);
        sign_in_text=(TextView)findViewById(R.id.sign_in_text);
        sign_up_text=(TextView)findViewById(R.id.sign_up_text);
        layout1=(LinearLayout)findViewById(R.id.layout1);
        layout2=(LinearLayout)findViewById(R.id.layout2);
        signInButton=(Button)findViewById(R.id.sign_in_button);
        mAPIService= APIUtil.getAPIService();

        // Creating a User with information provided by the user
        userModel = new UserModel();

        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirm_password.getText().toString())){
                userEmail = email.getText().toString();
                userModel.setEmail(userEmail);
                userModel.setPassword(password.getText().toString());
                userModel.setUsername(userEmail.split("@")[0]);
                Log.d("Signup : ", userEmail+" "+userModel.getPassword()+" "+ userModel.getUsername());
                Intent i = new Intent(SignUpActivity.this, CompleteProfileActivity.class);
                i.putExtra("email",userEmail);
                i.putExtra("password",userModel.getPassword());
                i.putExtra("username",userModel.getUsername());
                startActivity(i);finish();
                }
                else {

                    Toast.makeText(getApplicationContext(),"Passwords not matching",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.sign_up:

                sign_up_text.setVisibility(View.VISIBLE);
                sign_in_text.setVisibility(View.GONE);
                confirm_password.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);



                break;
            case R.id.sign_in:

                //to turn it into sign in layout

                sign_up_text.setVisibility(View.GONE);
                sign_in_text.setVisibility(View.VISIBLE);
                confirm_password.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);

                break;

            case R.id.sign_in_button:

                LoginModel loginModel=new LoginModel();
                loginModel.setUsername("amar");
                loginModel.setPassword("baikunth");

                JsonObject j=new JsonObject();
                j.addProperty("username","amar");
                j.addProperty("password","baikunth");

                getLoginToken(j);

        }
    }

    void getLoginToken(JsonObject loginModel){

        mAPIService.getToken(loginModel).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("In response : ", response.toString());

                if(response.isSuccessful()) {
                    Log.e("Response from", "post submitted to API : " + response.body());
                    String s=response.body().toString();
                    try {
                        JSONObject obj=new JSONObject(s);
                        Log.e("Token: ",obj.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SendLocation : ", "Unable to submit post to API.");
            }
        });

    }
}
