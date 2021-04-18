package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProfilActivity extends AppCompatActivity {

    TextView dummyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        dummyData = (TextView)findViewById(R.id.dummyData);
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dummyData.setText(email);

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
