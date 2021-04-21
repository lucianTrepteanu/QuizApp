package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.zip.Inflater;

public class QuestionFragment extends Fragment {


    ArrayList<Question> quiz = new ArrayList<>();
    int currQuestion = 0;
    int correctQuestions = 0;
    int lives;
    Button varA;
    Button varB;
    Button varC;
    Button varD;
    TextView questionView;
    View view;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_question, container, false);

        varA = (Button) view.findViewById(R.id.varA_fragment);
        varB = (Button) view.findViewById(R.id.varB_fragment);
        varC = (Button) view.findViewById(R.id.varC_fragment);
        varD = (Button) view.findViewById(R.id.varD_fragment);
        questionView = (TextView) view.findViewById(R.id.questionId_fragment);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Question question = quiz.get(currQuestion);
        setQuestion();
    }

    private void  setQuestion(){
        Question question = quiz.get(currQuestion);
        questionView.setText(question.question);
        varA.setText(question.varA);
        varB.setText(question.varB);
        varC.setText(question.varC);
        varD.setText(question.varD);
        varA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currQuestion += 1;
                if(varA.getText().toString().compareTo(question.answer) == 0){
                    correctQuestions += 1;
                    Toast.makeText(getActivity(),"Correct!",Toast.LENGTH_SHORT).show();
                }
                else{
                    lives -= 1;
                    Toast.makeText(getActivity(),"WROOOONG!",Toast.LENGTH_SHORT).show();
                }
                if(currQuestion < quiz.size() && lives > 0){
                    setQuestion();
                }
                else{
                    updateLeaderboard(correctQuestions);
                    Intent intent = new Intent(getContext(), LeaderboardActivity.class);
                    startActivity(intent);
                }
            }
        });
        varB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currQuestion += 1;
                if(varB.getText().toString().compareTo(question.answer) == 0){
                    correctQuestions += 1;
                    Toast.makeText(getActivity(),"Correct!",Toast.LENGTH_SHORT).show();
                }
                else{
                    lives -= 1;
                    Toast.makeText(getActivity(),"Incorrect!",Toast.LENGTH_SHORT).show();
                }
                if(currQuestion < quiz.size() && lives > 0){
                    setQuestion();
                }
                else{
                    updateLeaderboard(correctQuestions);
                    Intent intent = new Intent(getContext(), LeaderboardActivity.class);
                    startActivity(intent);
                }
            }
        });
        varC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currQuestion += 1;
                if(varC.getText().toString().compareTo(question.answer) == 0){
                    correctQuestions += 1;
                    Toast.makeText(getActivity(),"Correct!",Toast.LENGTH_SHORT).show();
                }
                else{
                    lives -= 1;
                    Toast.makeText(getActivity(),"WROOOONG!",Toast.LENGTH_SHORT).show();
                }
                if(currQuestion < quiz.size() && lives > 0){
                    setQuestion();
                }
                else{
                    updateLeaderboard(correctQuestions);
                    Intent intent = new Intent(getContext(), LeaderboardActivity.class);
                    startActivity(intent);
                }
            }
        });
        varD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currQuestion += 1;
                if(varD.getText().toString().compareTo(question.answer) == 0){
                    correctQuestions += 1;
                    Toast.makeText(getActivity(),"Correct!",Toast.LENGTH_SHORT).show();
                }
                else{
                    lives -= 1;
                    Toast.makeText(getActivity(),"WROOOONG!",Toast.LENGTH_SHORT).show();
                }
                if(currQuestion < quiz.size() && lives > 0){
                    setQuestion();
                }
                else{
                    updateLeaderboard(correctQuestions);
                    Intent intent = new Intent(getContext(), LeaderboardActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    void updateLeaderboard(int correctQuestions){
        System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
        System.out.println(correctQuestions);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String dbUrl="jdbc:mysql://database-android-quizapp.cvpqptukxwik.eu-west-2.rds.amazonaws.com/AndroidDatabase";
        String dbUser="admin";
        String dbPass="adminandroid";
        try{
            Connection dbConn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            PreparedStatement statement = (PreparedStatement) dbConn.prepareStatement("UPDATE UserData SET highscore = " + Integer.toString(correctQuestions) + " WHERE userId = '" + userId + "' AND highscore < " + Integer.toString(correctQuestions) + ";");
            statement.execute();

            statement.close();
            dbConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
