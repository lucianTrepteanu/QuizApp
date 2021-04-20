package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    ArrayList<String> s1 = new ArrayList<String>();
    ArrayList<String> s2 = new ArrayList<String>();
    ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //int imag[] = {R.drawable.common_google_signin_btn_icon_dark,R.drawable.common_google_signin_btn_icon_light};

        try{
            Class.forName("com.mysql.jdbc.Driver");
            String dbUrl="jdbc:mysql://database-android-quizapp.cvpqptukxwik.eu-west-2.rds.amazonaws.com/AndroidDatabase";
            String dbUser="admin";
            String dbPass="adminandroid";
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPass);
            Statement statement=connection.createStatement();
            String sqlQuery="select username, highscore, profileImage from UserData order by highscore desc";
            ResultSet rs=statement.executeQuery(sqlQuery);
            while(rs.next()){
                System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
                s1.add(rs.getString("username"));
                s2.add(rs.getString("highscore"));
                Blob blob = rs.getBlob("profileImage");
                if(blob == null){
                    Bitmap currImage = BitmapFactory.decodeResource(getResources(), R.drawable.common_google_signin_btn_icon_light);
                    images.add(currImage);
                }
                else{
                    byte[] bytes = blob.getBytes(1, (int)blob.length());
                    Bitmap currImage = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    images.add(currImage);
                }
            }
            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
            System.out.println(s1.size());
            System.out.println(s2.size());
            System.out.println(images.size());
            statement.close();
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }

        recyclerView = findViewById(R.id.leaderRecycle);
        RecAdapter recAdapter = new RecAdapter(this, s1, s2, images);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.leaderboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.start_game:
                        startActivity(new Intent(getApplicationContext(),
                                StartGameActivity.class));
                        overridePendingTransition(0,0);
                    case R.id.leaderboard:
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(getApplicationContext(),
                                ProfilActivity.class));
                        overridePendingTransition(0,0);
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
