package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText confirmPassword;
    EditText username;
    Button backButton;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        username = (EditText)findViewById(R.id.username_register);
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
                String dataUsername = username.getText().toString();
                if(dataEmail.equals("") || dataPassword.equals("") || dataConfirmedPassword.equals("")){
                    Toast.makeText(getApplicationContext(), "No empty box allowed!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(dataPassword.equals(dataConfirmedPassword)){
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            String dbUrl="jdbc:mysql://database-android-quizapp.cvpqptukxwik.eu-west-2.rds.amazonaws.com/AndroidDatabase";
                            String dbUser="admin";
                            String dbPass="adminandroid";
                            Connection dbConn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                            try {
                                //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                Statement stmt = (Statement) dbConn.createStatement();
                                ResultSet res = stmt.executeQuery("Select * from Users");
                                boolean emailExists = false;
                                while (res.next()) {
                                    if(res.getString("email").equals(dataEmail)){
                                        emailExists = true;
                                        Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                if(!emailExists){
                                    //System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                                    ResultSet res2 = stmt.executeQuery("Select count(*) as cnt from Users");
                                    res2.next();
                                    Integer countUsers = res2.getInt("cnt") + 1;

                                    PreparedStatement statement = (PreparedStatement) dbConn.prepareStatement("INSERT INTO Users (userId, username, password, email) VALUES ( ?, ?, ?, ?)");
                                    statement.setString(1, countUsers.toString());
                                    statement.setString(2, dataUsername);
                                    statement.setString(3, dataPassword);
                                    statement.setString(4, dataEmail);
                                    statement.execute();
                                    Toast.makeText(getApplicationContext(), "Registered succesfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                }
                            }
                            catch(SQLException e){
                                e.printStackTrace();
                            }

                            dbConn.close();

                        } catch (ClassNotFoundException | SQLException e) {
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
            }
        });
    }
}
