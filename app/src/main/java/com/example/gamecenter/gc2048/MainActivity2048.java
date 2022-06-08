package com.example.gamecenter.gc2048;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.R;

public class MainActivity2048 extends AppCompatActivity {

    Button play2048;

    private TextView highScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2048);
        this.highScoreTextView = (TextView) this.findViewById(R.id.mainHighScore);
        this.highScoreTextView.setText(this.getHighScore() + "");

        play2048 = (Button) findViewById(R.id.play2048);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.highScoreTextView.setText(this.getHighScore() + "");
    }

    public void startPlaying(View caller) {
        Intent startPlating = new Intent(MainActivity2048.this, BoardActivity2048.class);
        this.startActivity(startPlating);
    }

    public int getHighScore() {
        SharedPreferences preferences = this.getSharedPreferences(this.getString(R.string.preference_key), Context.MODE_PRIVATE);
        return preferences.getInt(this.getString(R.string.high_score_key), 0);
    }

    public void resetHighScore() {
        SharedPreferences preferences = this.getSharedPreferences(this.getString(R.string.preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(this.getString(R.string.high_score_key), 0);
        editor.apply();
    }
}