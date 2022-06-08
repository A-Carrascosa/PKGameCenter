package com.example.gamecenter;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecenter.Login.DataBase.DBHelper;

import java.io.Serializable;

public class Score extends AppCompatActivity implements Serializable {

    private int score;
    private String game;
    private String user_name;

    private RecyclerView recyclerView;
    private RecyclerViewAdapterScore recyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listrecord_score);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewScore);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        recyclerViewAdapter = new RecyclerViewAdapterScore(dbHelper.showScore());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public Score() {
    }

    public Score(int score, String game, String user_name) {
        this.score = score;
        this.game = game;
        this.user_name = user_name;


    }
    public int getScore() {
        return score;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getGame() {
        return game;
    }
}
