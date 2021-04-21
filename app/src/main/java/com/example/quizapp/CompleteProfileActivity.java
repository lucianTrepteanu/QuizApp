package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class CompleteProfileActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    Button profileButton;
    EditText firstName;
    EditText lastName;
    EditText username;
    EditText country;
    EditText city;
    FloatingActionButton fab;

    String filePath;
    Bitmap photoTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        filePath = null;

        profileButton = (Button)findViewById(R.id.profile);
        firstName = (EditText)findViewById(R.id.firstname);
        lastName = (EditText)findViewById(R.id.lastname);
        username = (EditText)findViewById(R.id.username);
        country = (EditText)findViewById(R.id.countryProfile);
        city = (EditText)findViewById(R.id.cityProfile);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(CompleteProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    requestStoragePermission();
                }
                selectImage(CompleteProfileActivity.this);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataUsername = username.getText().toString();
                String dataFirstName = firstName.getText().toString();
                String dataLastName = lastName.getText().toString();
                String dataCountry = country.getText().toString();
                String dataCity = city.getText().toString();

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String dbUrl="jdbc:mysql://database-android-quizapp.cvpqptukxwik.eu-west-2.rds.amazonaws.com/AndroidDatabase";
                String dbUser="admin";
                String dbPass="adminandroid";
                try {
                    Connection dbConn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    PreparedStatement statement = (PreparedStatement) dbConn.prepareStatement("INSERT INTO UserData (userDataId, username, highscore, profileImage, userId, firstname, lastname, country, city) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    statement.setString(1, userId);
                    statement.setString(2, dataUsername);
                    statement.setInt(3, 0);

                    try{
                        if(filePath != null){
                            File file = new File(filePath);
                            FileInputStream stream = new FileInputStream(file);
                            statement.setBinaryStream(4, stream);
                        } else {
                            System.out.println("AYAYAYYAYYAYA");
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            photoTaken.compress(Bitmap.CompressFormat.PNG,100,bos);
                            byte[] bytes = bos.toByteArray();
                            Blob blob = null;
                            blob.setBytes(1, bytes);
                            statement.setBlob(4, blob);
                        }
                    } catch (Exception e){
                        statement.setString(4, null);
                        e.printStackTrace();
                    }

                    statement.setString(5, userId);
                    statement.setString(6, dataFirstName);
                    statement.setString(7, dataLastName);
                    statement.setString(8, dataCountry);
                    statement.setString(9, dataCity);
                    statement.execute();

                    Toast.makeText(getApplicationContext(), "Data completed succesfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CompleteProfileActivity.this, ProfilActivity.class);
                    startActivity(intent);

                    dbConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Permission needed for selecting profile picture")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CompleteProfileActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted",Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, "Permission denied",Toast.LENGTH_SHORT);
            }
        }
    }

    private void selectImage(Context context){
        final CharSequence[] options = {"Take photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals("Take photo")){
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture,0);
                } else if(options[which].equals("Choose from gallery")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if(options[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        //imageView.setImageBitmap(selectedImage);
                        photoTaken = selectedImage;
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                filePath = picturePath;
                                System.out.println(picturePath);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}