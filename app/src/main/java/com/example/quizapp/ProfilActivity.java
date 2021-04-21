package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class ProfilActivity extends AppCompatActivity {

    TextView usernameView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        imageView = (ImageView)findViewById(R.id.imageID);
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
            String query = "Select profileImage, username from UserData where userId = '" + userId + "';";
            Statement statement = dbConn.createStatement();
            ResultSet res = statement.executeQuery(query);
            res.next();
            if(res == null){
                System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            }
            else{
                usernameView = (TextView)findViewById(R.id.usernameView);
                usernameView.setText(res.getString("username"));

                Blob blob = res.getBlob("profileImage");
                byte[] bytes = blob.getBytes(1, (int)blob.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageView.setImageBitmap(bitmap);
                statement.close();
                dbConn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.start_game:
                        startActivity(new Intent(getApplicationContext(),
                                StartGameActivity.class));
                        overridePendingTransition(0,0);
                    case R.id.leaderboard:
                        startActivity(new Intent(getApplicationContext(),
                                LeaderboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profil:
                        return true;
                    case R.id.exit:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),
                                LogInActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}
