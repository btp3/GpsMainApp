package com.developer.iron_man.gpsmain;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import models.UserModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Iron_Man on 25/06/17.
 */

public class CompleteProfileActivity extends AppCompatActivity {

    EditText firstname, lastName, address, contact, aadhar;
    ImageView photo;
    Button save;
    UserModel userModel;
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_profile_details);
        firstname = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        address = (EditText) findViewById(R.id.address);
        contact = (EditText) findViewById(R.id.contact);
        aadhar = (EditText) findViewById(R.id.aadhar);
        photo = (ImageView) findViewById(R.id.imageViewPhoto);
        save = (Button) findViewById(R.id.saveButton);
        Bundle bundle = getIntent().getExtras();
        userModel = new UserModel();
        userModel.setUsername(bundle.getString("username"));
        userModel.setEmail(bundle.getString("email"));
        userModel.setPassword(bundle.getString("password"));

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setFirst_name(firstname.getText().toString());
                userModel.setLast_name(lastName.getText().toString());
                userModel.setAddress(address.getText().toString());
                userModel.setContact(contact.getText().toString());
                userModel.setAadhar(aadhar.getText().toString());
                Log.d("User : ", userModel.getUsername() + "  " + userModel.getAadhar());

                // Send user details
                //sendUser();
                RequestBody fullName = RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name");
                String json = new Gson().toJson(userModel);

                Intent intent = new Intent(CompleteProfileActivity.this, QRScanner.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Log.d("Image FilePAth ", getRealPathFromURI(filePath)+ " ");
                //Setting the Bitmap to ImageView
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}