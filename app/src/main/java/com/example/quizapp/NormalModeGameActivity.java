package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.mysql.jdbc.PreparedStatement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import javax.crypto.AEADBadTagException;

public class NormalModeGameActivity extends AppCompatActivity {

    private ArrayList<Question> quiz = new ArrayList<Question>();
    private RequestQueue requestQueue;
    private static int currQuestions;
    private static int correctAnswers = 0;
    Button varA;
    Button varB;
    Button varC;
    Button varD;
    TextView question;

    public class Question{
        String question;
        String varA;
        String varB;
        String varC;
        String varD;
        String answer;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_mode_game);
        requestQueue = Volley.newRequestQueue(this);

        quiz = getQuestions();
        System.out.println(quiz.get(0).question);
/*
        Button varA = (Button)findViewById(R.id.varA);
        Button varB = (Button)findViewById(R.id.varB);
        Button varC = (Button)findViewById(R.id.varC);
        Button varD = (Button)findViewById(R.id.varD);
        TextView question = (TextView)findViewById(R.id.question);
        question.setText(quiz.get(quizN.get(i)).question);
        varA.setText(quiz.get(quizN.get(i)).varA);
        varB.setText(quiz.get(quizN.get(i)).varB);
        varC.setText(quiz.get(quizN.get(i)).varC);
        varD.setText(quiz.get(quizN.get(i)).varD);
        String correctAnswer = quiz.get(quizN.get(i)).answer;
/*
        varA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(varA.getText().toString().compareTo(correctAnswer) == 0){
                    correctAnswers += 1;
                }
                quizN.get(i) += 1;
                if(quizN.get(i) == 10){
                    Intent intent = new Intent(NormalModeGameActivity.this, ProfilActivity.class);
                    startActivity(intent);
                }
                question.setText(quiz.get(quizN.get(i)).question);
                varA.setText(quiz.get(quizN.get(i)).varA);
                varB.setText(quiz.get(quizN.get(i)).varB);
                varC.setText(quiz.get(quizN.get(i)).varC);
                varD.setText(quiz.get(quizN.get(i)).varD);

            }
        });
        varB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(varB.getText().toString().compareTo(correctAnswer) == 0){
                    correctAnswers += 1;
                }
                quizN.get(i) += 1;
                if(quizN.get(i) == 11){
                    Intent intent = new Intent(NormalModeGameActivity.this, ProfilActivity.class);
                    startActivity(intent);
                }
                question.setText(quiz.get(quizN.get(i)).question);
                varA.setText(quiz.get(quizN.get(i)).varA);
                varB.setText(quiz.get(quizN.get(i)).varB);
                varC.setText(quiz.get(quizN.get(i)).varC);
                varD.setText(quiz.get(quizN.get(i)).varD);

            }
        });
        varC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(varC.getText().toString().compareTo(correctAnswer) == 0){
                    correctAnswers += 1;
                }
                quizN.get(i) += 1;
                if(quizN.get(i) == 11){
                    Intent intent = new Intent(NormalModeGameActivity.this, ProfilActivity.class);
                    startActivity(intent);
                }
                question.setText(quiz.get(quizN.get(i)).question);
                varA.setText(quiz.get(quizN.get(i)).varA);
                varB.setText(quiz.get(quizN.get(i)).varB);
                varC.setText(quiz.get(quizN.get(i)).varC);
                varD.setText(quiz.get(quizN.get(i)).varD);

            }
        });
        varD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(varD.getText().toString().compareTo(correctAnswer) == 0){
                    correctAnswers += 1;
                }
                quizN.get(i) += 1;
                if(quizN.get(i) == 11){
                    Intent intent = new Intent(NormalModeGameActivity.this, ProfilActivity.class);
                    startActivity(intent);
                }
                question.setText(quiz.get(quizN.get(i)).question);
                varA.setText(quiz.get(quizN.get(i)).varA);
                varB.setText(quiz.get(quizN.get(i)).varB);
                varC.setText(quiz.get(quizN.get(i)).varC);
                varD.setText(quiz.get(quizN.get(i)).varD);

            }
        });*/
    }

    ArrayList<Question> getQuestions(){
        ArrayList<Question> quizN = new ArrayList<Question>();
        for(int i = 0; i < 10; i++)
            quizN.add(new Question());
        requestQueue = Volley.newRequestQueue(this);
        String url = new String("https://opentdb.com/api.php?amount=10&category=9&type=multiple");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try{
                JSONArray dataArray = response.getJSONArray("results");
                for (int i = 0; i < dataArray.length(); i++){
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    String question = dataObject.getString("question");
                   
                    quizN.get(i).question = String.valueOf(dataObject.getString("question"));

                    /*quizN.get(i).answer = dataObject.getString("correct_answer");

                    ArrayList<String> possibilities = new ArrayList<String>();
                    possibilities.add(quizN.get(i).answer);

                    JSONArray incorrectAnswers = dataObject.getJSONArray("incorrect_answers");
                    possibilities.add(incorrectAnswers.get(0).toString());
                    possibilities.add(incorrectAnswers.get(1).toString());
                    possibilities.add(incorrectAnswers.get(2).toString());

                    Collections.shuffle(possibilities);
                    quizN.get(i).varA = possibilities.get(0);
                    quizN.get(i).varB = possibilities.get(1);
                    quizN.get(i).varC = possibilities.get(2);
                    quizN.get(i).varD = possibilities.get(3);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        System.out.println(quizN.get(0).question);
        requestQueue.add(request);
        return quizN;
    }
}
