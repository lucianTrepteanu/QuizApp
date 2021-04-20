package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class SurvivalModeGameActivity extends AppCompatActivity {
    ArrayList<Question> quiz = new ArrayList<>();
    private RequestQueue requestQueue;
    private static int currQuestions;
    private static int correctAnswers = 0;
    Button varA;
    Button varB;
    Button varC;
    Button varD;
    TextView questionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_mode_game);
        requestQueue = Volley.newRequestQueue(this);

        getQuestions();
    }

    void getQuestions(){
        ArrayList<Question> quizN = new ArrayList<Question>();
        requestQueue = Volley.newRequestQueue(this);
        String url = new String("https://opentdb.com/api.php?amount=50&category=9&type=multiple");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try{
                JSONArray dataArray = response.getJSONArray("results");
                for (int i = 0; i < dataArray.length(); i++){
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    String question = dataObject.getString("question");
                    Question question1 = new Question();
                    question1.question = question;

                    question1.answer = dataObject.getString("correct_answer");

                    ArrayList<String> possibilities = new ArrayList<String>();
                    possibilities.add(question1.answer);

                    JSONArray incorrectAnswers = dataObject.getJSONArray("incorrect_answers");
                    possibilities.add(incorrectAnswers.get(0).toString());
                    possibilities.add(incorrectAnswers.get(1).toString());
                    possibilities.add(incorrectAnswers.get(2).toString());

                    Collections.shuffle(possibilities);
                    question1.varA = possibilities.get(0);
                    question1.varB = possibilities.get(1);
                    question1.varC = possibilities.get(2);
                    question1.varD = possibilities.get(3);

                    quizN.add(question1);
                }
                QuestionFragment fragment = new QuestionFragment();
                fragment.quiz = quizN;
                fragment.lives = 3;
                setFragment(fragment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });

        requestQueue.add(request);
    }

    void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
