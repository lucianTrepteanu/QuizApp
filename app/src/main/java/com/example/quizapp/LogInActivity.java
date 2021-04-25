package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LogInActivity extends AppCompatActivity {

    Button loginButton;
    Button backButton;
    Button registerButton;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        email = (EditText)findViewById(R.id.email_login);
        password = (EditText)findViewById(R.id.password_login);

        loginButton = (Button)findViewById(R.id.button_to_home);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataEmail = email.getText().toString();
                String dataPassword = password.getText().toString();
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
                    Statement stmt = (Statement) dbConn.createStatement();
                    ResultSet res = stmt.executeQuery("Select * from Users");
                    while (res.next()) {
                        System.out.println(dataEmail);
                        System.out.println(email);
                        if (res.getString("email").equals(dataEmail)) {
                            String hashedPassword = res.getString("password");
                            boolean ok = checkPassword(dataPassword, hashedPassword);
                            if(ok){
                                Toast.makeText(getApplicationContext(), "Login with success", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Email doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        registerButton = (Button)findViewById(R.id.button_to_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public boolean checkPassword(String password, String hashedPassword){
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
