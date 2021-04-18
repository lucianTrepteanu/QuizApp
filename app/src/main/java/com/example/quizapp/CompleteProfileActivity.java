package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class CompleteProfileActivity extends AppCompatActivity {

    Button profileButton;
    EditText firstName;
    EditText lastName;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        profileButton = (Button)findViewById(R.id.profile);
        firstName = (EditText)findViewById(R.id.firstname);
        lastName = (EditText)findViewById(R.id.lastname);
        username = (EditText)findViewById(R.id.username);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataUsername = username.getText().toString();
                String dataFirstName = firstName.getText().toString();
                String dataLastName = lastName.getText().toString();

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
                    PreparedStatement statement = (PreparedStatement) dbConn.prepareStatement("INSERT INTO UserData (userDataId, username, highscore, profileImage, userId, firstname, lastname) VALUES ( ?, ?, ?, ?, ?, ?, ?)");
                    statement.setString(1, userId);
                    statement.setString(2, dataUsername);
                    statement.setString(3, null);
                    statement.setString(4, null);
                    statement.setString(5, userId);
                    statement.setString(6, dataFirstName);
                    statement.setString(7, dataLastName);
                    statement.execute();

                    Toast.makeText(getApplicationContext(), "Data completed succesfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CompleteProfileActivity.this, LogInActivity.class);
                    startActivity(intent);

                    dbConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
