package com.example.gamecenter.PegSolitaire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.gamecenter.R;


public class MainMenuActivityPegSolitaire extends Activity {

    Button playPeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_peg_solitaire);

        playPeg = (Button) findViewById(R.id.playPeg);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_peg_solitaire, menu);
        return true;
    }

    public void openGameActivity(View view) {
        Intent intent = new Intent(MainMenuActivityPegSolitaire.this, GameActivityPegSolitaire.class);
        this.startActivity(intent);

    }



}