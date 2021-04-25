package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText confirmPassword;
    Button backButton;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        email = (EditText)findViewById(R.id.email_register);
        password = (EditText)findViewById(R.id.password_register);
        confirmPassword = (EditText)findViewById(R.id.confirm_password_register);
        registerButton = (Button)findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataEmail = email.getText().toString();
                String dataPassword = password.getText().toString();
                String dataConfirmedPassword = confirmPassword.getText().toString();

                if(dataEmail.equals("") || dataPassword.equals("") || dataConfirmedPassword.equals("")){
                    Toast.makeText(getApplicationContext(), "No empty box allowed!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(dataPassword.equals(dataConfirmedPassword)){
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
                            String hashedPass = hashPassword(dataPassword);
                            System.out.println(hashedPass);
                            PreparedStatement statement = (PreparedStatement) dbConn.prepareStatement("INSERT INTO Users (userId, password, email) VALUES ( ?, ?, ?)");
                            statement.setString(1, "2");
                            statement.setString(2, hashedPass);
                            statement.setString(3, dataEmail);
                            statement.execute();

                            Toast.makeText(getApplicationContext(), "Registered succesfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, CompleteProfileActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            dbConn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password and confirmed password do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public String hashPassword(String password){
        return BCrypt.withDefaults().hashToString(10, password.toCharArray());
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}